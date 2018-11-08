package com.xingyun.smartusbdeviceapp.util;

import android.content.Context;
import android.widget.Toast;

import com.xingyun.smartusbdeviceapp.MyApplication;

/**
 * Toast 弹出信息工具类,简化代码编写
 * @author fairy
 * */
public class SmartToastUtils{
    public static void showLong(String info) {
        Toast.makeText(MyApplication.getContext(), info, Toast.LENGTH_LONG).show();
    }
    public static void showShort(String info) {
        Toast.makeText(MyApplication.getContext(), info,Toast.LENGTH_SHORT).show();
    }
}
