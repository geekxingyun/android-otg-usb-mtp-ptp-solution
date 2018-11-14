package com.xingyun.smartcontentresolveapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xingyun.smartcontentresolveapp.util.SmartLogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.startScanContentResolveButton)
    Button startScanContentResolveButton;
    @BindView(R.id.startSystemScanContentResolveButton)
    Button startSystemScanContentResolveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //开启内容提供器
        startScanContentResolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllPic();
            }
        });

        startSystemScanContentResolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanPicFromSystem();
            }
        });
    }

    private static final int READ_REQUEST_CODE = 42;
    private void startScanPicFromSystem() {
                //如果小于 4.3 API 18
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent,READ_REQUEST_CODE);
        }else{
            //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);// 大于4.4
            //开始搜索图片
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// 大于4.4
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, READ_REQUEST_CODE);
        }
    }
        @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                SmartLogUtils.showError("Uri: " + uri.toString(),true);
                //解析URI 获取宣红图片的名字和图片
                dumpImageMetaData(uri);
//                try {
//                    getBitmapFromUri(uri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }
    /******
     * 解析URI 显示图片名称和大小
     * *****/
    public void dumpImageMetaData(Uri uri) {
        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);
        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {
                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an
                // int can't be null in Java, the behavior is implementation-specific,
                // which is just a fancy term for "unpredictable".  So as
                // a rule, check if it's null before assigning to an int.  This will
                // happen often:  The storage API allows for remote files, whose
                // size might not be locally known.
                SmartLogUtils.showError("Display Name: " + displayName,true);
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                SmartLogUtils.showError(size+"",true);
            }
        } finally {
            cursor.close();
        }
    }


    private final static Uri allPicInPhone = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    private void showAllPic() {
        sendRequestPermission();
        List<String> filePathList = new ArrayList<String>();
        Log.e("xingyun", allPicInPhone + "");
        Cursor cursor = getContentResolver().query(allPicInPhone, null, null, null, null);
        while (cursor.moveToNext()) {
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String filePath = new String(data, 0, data.length - 1);
            filePathList.add(filePath);
        }
        Toast.makeText(MainActivity.this, "扫描到图片" + filePathList.size() + "张", Toast.LENGTH_SHORT).show();
        for (int i=0;i<filePathList.size();i++){
            Toast.makeText(MainActivity.this, "扫描到图片名称:" + filePathList.get(i) , Toast.LENGTH_SHORT).show();
        }
    }


    String[] allpermissions = new String[]
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

    private void sendRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //是否需要申请权限
            boolean needapply = false;
            for (int i = 0; i < allpermissions.length; i++) {
                //检查是否已经给了权限
                int checkpermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                        allpermissions[i]);
                if (checkpermission != PackageManager.PERMISSION_GRANTED) {//没有给权限
                    Log.e("permission", "动态申请");
                    needapply = true;
                }
            }
            if (needapply) {
                //参数分别是当前活动，权限字符串数组，requestcode
                ActivityCompat.requestPermissions(MainActivity.this, allpermissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //grantResults数组与权限字符串数组对应，里面存放权限申请结果
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "已授权", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "拒绝授权", Toast.LENGTH_SHORT).show();
        }
    }

}
