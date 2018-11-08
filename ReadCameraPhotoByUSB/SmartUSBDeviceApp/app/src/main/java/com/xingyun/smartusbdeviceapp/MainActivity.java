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

import static com.xingyun.smartusbdeviceapp.constant.USBReceiverConstant.READ_USB_DEVICE_PERMISSION;

public class MainActivity extends AppCompatActivity {

    //USB MTP 设备监控广播
    USBMTPReceiver usbmtpReceiver = null;
    @BindView(R.id.checkedPicFile_ImageView)
    ImageView checkedPicFileImageView;
    @BindView(R.id.showURI_TextView)
    TextView showURITextView;

    @BindView(R.id.check_USB_Status_Button)
    Button checkUSBStatusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //注册USB设备广播
        registerUSBReceiver();

        sendUSBBroadcast();

        checkUSBStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求读写权限
                requestReadAndWriteAccess();
                //发送读取照片广播
                sendUSBBroadcast();
            }
        });

    }

    //发送USB广播
    private void sendUSBBroadcast() {
        //发送广播
        Intent intent=new Intent(READ_USB_DEVICE_PERMISSION);
        //发送标准广播
        sendBroadcast(intent);
    }

    /*****
     * 动态注册USB 设备监听
     * */
    private void registerUSBReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        //自定义USB设备读取照片
        intentFilter.addAction(READ_USB_DEVICE_PERMISSION);
        //USB连接状态发生变化时产生的广播
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        USBMTPReceiver usbmtpReceiver = new USBMTPReceiver();
        registerReceiver(usbmtpReceiver, intentFilter);
    }

    /***
     * 请求读写权限
     * ****/
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static int REQUEST_PERMISSION_CODE = 1;
    private void requestReadAndWriteAccess(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }
    //读写权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                SmartToastUtils.showShort(MainActivity.this,"申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
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
