package com.xingyun.smartcontentresolveapp.util;

import android.widget.Toast;

import com.xingyun.smartcontentresolveapp.MyApplication;

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
