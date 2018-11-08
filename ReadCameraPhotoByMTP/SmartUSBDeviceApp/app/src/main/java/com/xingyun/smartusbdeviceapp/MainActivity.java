package com.xingyun.smartusbdeviceapp;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingyun.smartusbdeviceapp.receiver.USBMTPReceiver;
import com.xingyun.smartusbdeviceapp.util.SmartToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.xingyun.smartusbdeviceapp.constant.USBReceiverConstant.READ_USB_DEVICE_PERMISSION;

/*
* 单反相机参考资料
https://github.com/magnusja/libaums
https://blog.csdn.net/csdn635406113/article/details/70146041
https://developer.android.google.cn/guide/topics/providers/document-provider#create
https://github.com/ynyao/cameraphoto
https://www.jianshu.com/p/55eae30d133c
https://github.com/michaelzoech/remoteyourcam-usb
* ************/
//public class MainActivity extends SessionActivity implements Camera.CameraListener {
public class MainActivity extends AppCompatActivity {

    private static final int DIALOG_PROGRESS = 1;
    private static final int DIALOG_NO_CAMERA = 2;

    private final String TAG = MainActivity.class.getSimpleName();

    //USB MTP 设备监控广播
    USBMTPReceiver usbmtpReceiver = null;
    @BindView(R.id.checkedPicFile_ImageView)
    ImageView checkedPicFileImageView;
    @BindView(R.id.showURI_TextView)
    TextView showURITextView;

    @BindView(R.id.check_USB_Access_Button)
    Button checkUSBAccessButton;
    @BindView(R.id.scan_MTP_Pic_Button)
    Button scanMTPPicButton;

    private static final int READ_REQUEST_CODE = 42;
    @BindView(R.id.scan_USB_Pic_Button)
    Button scanUSBPicButton;

    @BindView(R.id.find_USB_Pic_Button)
    Button findUSBPicButton;

    //PTP 服务
//    private PtpService ptpService;
//    //数码相机
//    private Camera camera;
//    private boolean isLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //注册USB读取单反设备广播
        registerUSBReceiver();

        //PTP协议进行通信
//        settings = new AppSettings(this);
//        ptpService = PtpService.Singleton.getInstance(this);

        //申请读写权限
        checkUSBAccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求读写权限
                requestReadAndWriteAccess();
            }
        });

        //发送读取USB广播方法
        scanMTPPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送读取照片广播
                sendUSBBroadcast();
            }
        });

//        //内容提供器访问相册方法
//        scanUSBPicButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //内容提供器
//                //content://com.android.providers.media.documents/document/image%3A61
//                //content://com.android.providers.media.documents/document/image%3A60
//                //content://com.android.providers.media.documents/document/image%3A62
//                //Android 官方方法 通过打开一个内置的文件管理器方式回调方法中可以获取选择图片的信息
//                startPrivoder();
//            }
//        });
//
//        findUSBPicButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //使用自定义的内容提供器通过传入一个URL尝试获取
//               findPicFromDevice(Uri.parse("content://com.android.providers.media.documents/document/"));
//            }
//        });
    }

    /**
     * 谷歌官方提供的方法
     * 通过打开一个内置的文件管理器的方式搜索结果通过回调方法显示选中的文件信息
     * ********/
//    public void startPrivoder(){
//        //如果小于 4.3 API 18
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("image/*");
//            startActivityForResult(intent, READ_REQUEST_CODE);
//        }else{
//            //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);// 大于4.4
//            //开始搜索图片
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// 大于4.4
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("image/*");
//            startActivityForResult(intent, READ_REQUEST_CODE);
//        }
//    }
//    /**
//     * 选中回调方法
//     * *******/
//    @Override
//    public void onActivityResult(int requestCode, int resultCode,
//                                 Intent resultData) {
//        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
//        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
//        // response to some other intent, and the code below shouldn't run at all.
//        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            // The document selected by the user won't be returned in the intent.
//            // Instead, a URI to that document will be contained in the return intent
//            // provided to this method as a parameter.
//            // Pull that URI using resultData.getData().
//            Uri uri = null;
//            if (resultData != null) {
//                uri = resultData.getData();
//                SmartLogUtils.showDebug("Uri: " + uri.toString(),true);
//                //解析URI 获取宣红图片的名字和图片
//                dumpImageMetaData(uri);
//                try {
//                    getBitmapFromUri(uri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    /******
     * 解析URI 显示图片名称和大小
     * *****/
//    public void dumpImageMetaData(Uri uri) {
//        // The query, since it only applies to a single document, will only return
//        // one row. There's no need to filter, sort, or select fields, since we want
//        // all fields for one document.
//        Cursor cursor = MyApplication.getContext().getContentResolver()
//                .query(uri, null, null, null, null, null);
//        try {
//            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
//            // "if there's anything to look at, look at it" conditionals.
//            if (cursor != null && cursor.moveToFirst()) {
//                // Note it's called "Display Name".  This is
//                // provider-specific, and might not necessarily be the file name.
//                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
//                // If the size is unknown, the value stored is null.  But since an
//                // int can't be null in Java, the behavior is implementation-specific,
//                // which is just a fancy term for "unpredictable".  So as
//                // a rule, check if it's null before assigning to an int.  This will
//                // happen often:  The storage API allows for remote files, whose
//                // size might not be locally known.
//                SmartLogUtils.showDebug("Display Name: " + displayName,true);
//                String size = null;
//                if (!cursor.isNull(sizeIndex)) {
//                    // Technically the column stores an int, but cursor.getString()
//                    // will do the conversion automatically.
//                    size = cursor.getString(sizeIndex);
//                } else {
//                    size = "Unknown";
//                }
//                SmartLogUtils.showDebug(size+"",true);
//            }
//        } finally {
//            cursor.close();
//        }
//    }

    /***
     * 通过解析一个Uri 返回BitMap对象
     * ********/
//    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
//        ParcelFileDescriptor parcelFileDescriptor =
//                getContentResolver().openFileDescriptor(uri, "r");
//        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//        parcelFileDescriptor.close();
//        return image;
//    }


    /***
     *  通过 解析Uri 查询对象
     * *****/
//    public void findPicFromDevice(Uri uri){
//        // The query, since it only applies to a single document, will only return
//        // one row. There's no need to filter, sort, or select fields, since we want
//        // all fields for one document.
//        Cursor cursor = MyApplication.getContext().getContentResolver()
//                .query(uri, null, null, null, null, null);
//
//
//        try {
//            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
//            // "if there's anything to look at, look at it" conditionals.
//            if (cursor != null && cursor.moveToFirst()) {
//
//                // Note it's called "Display Name".  This is
//                // provider-specific, and might not necessarily be the file name.
//                String displayName = cursor.getString(
//                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//
//                SmartLogUtils.showDebug("Display Name: " + displayName,true);
//
//                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
//                // If the size is unknown, the value stored is null.  But since an
//                // int can't be null in Java, the behavior is implementation-specific,
//                // which is just a fancy term for "unpredictable".  So as
//                // a rule, check if it's null before assigning to an int.  This will
//                // happen often:  The storage API allows for remote files, whose
//                // size might not be locally known.
//                String size = null;
//                if (!cursor.isNull(sizeIndex)) {
//                    // Technically the column stores an int, but cursor.getString()
//                    // will do the conversion automatically.
//                    size = cursor.getString(sizeIndex);
//                } else {
//                    size = "Unknown";
//                }
//                SmartLogUtils.showDebug(size+"",true);
//            }
//        } finally {
//            cursor.close();
//        }
//    }


    //发送USB广播
    private void sendUSBBroadcast() {
        //发送广播
        Intent intent = new Intent(READ_USB_DEVICE_PERMISSION);
        //发送标准广播
        sendBroadcast(intent);
    }

    /*****
     * 动态注册USB 设备监听
     * */
    private void registerUSBReceiver() {
        usbmtpReceiver = new USBMTPReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //注册监听自定义广播
        intentFilter.addAction(READ_USB_DEVICE_PERMISSION);
        //设备插入
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        //设备拔出
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(usbmtpReceiver, intentFilter);
    }

    /***
     * 请求读写权限
     * ****/
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE,
    };
    private static int REQUEST_PERMISSION_CODE = 1;
    private void requestReadAndWriteAccess() {
        SmartToastUtils.showShort("申请读写权限开始");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(MyApplication.getContext(),WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        SmartToastUtils.showShort( "申请读写权限完成");
    }
    //读写权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                SmartToastUtils.showShort( "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (usbmtpReceiver != null) {
            //取消注册USB设备广播
            unregisterReceiver(usbmtpReceiver);
        }
    }
}
