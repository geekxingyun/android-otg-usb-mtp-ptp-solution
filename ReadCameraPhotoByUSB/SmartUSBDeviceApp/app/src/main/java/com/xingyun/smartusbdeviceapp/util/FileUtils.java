package com.xingyun.smartusbdeviceapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileInputStream;
import com.github.mjdev.libaums.fs.UsbFileStreamFactory;
import com.xingyun.smartusbdeviceapp.MainActivity;
import com.xingyun.smartusbdeviceapp.MyApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by apple on 2018/7/13.
 */

public class FileUtils {

    //SD Card 根目录下创建文件夹
    private final static String USBTempFolder=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"usb_temp_foler";

    public final static void saveToPhoneDevice(UsbFile usbFile,FileSystem fileSystem){

        if(usbFile.isDirectory()){
            SmartLogUtils.showDebug(usbFile.getName()+"是一个文件夹",true);
            return ;
        }
        Boolean sdCardCanUse=Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if(sdCardCanUse){
            SmartLogUtils.showDebug("SD Card 可用",true);
        }else{
            SmartLogUtils.showDebug("SD Card 不可用",true);
        }
        File file=new File(USBTempFolder);
        //文件读写测试
       if(file.canRead()){
           SmartLogUtils.showError("文件可读",true);
       }else{
           SmartLogUtils.showError("文件不可读",true);
       }
        if(file.canWrite()){
            SmartLogUtils.showError("文件可写",true);
        }else{
            SmartLogUtils.showError("文件不可写",true);
        }
        //文件夹不存在就创建
        if(!file.exists()){
            file.mkdir();
            SmartLogUtils.showDebug("文件夹创建成功",true);
        }else{
            SmartLogUtils.showDebug("文件夹已存在",true);
        }
        //写入文件
        FileOutputStream os=null;
        InputStream is=null;
        String newFileName=null;
        try {
            newFileName=USBTempFolder+File.separator+usbFile.getName();
            SmartLogUtils.showError(newFileName,true);
            os = new FileOutputStream(newFileName);
            is= new UsbFileInputStream(usbFile);
            int bytesRead = 0;
            byte[] buffer = new byte[fileSystem.getChunkSize()];//作者的推荐写法是currentFs.getChunkSize()为buffer长度
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(os!=null){
                    os.flush();
                    os.close();
                }
                if(is!=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 把字节数组保存为一个文件
     * @param
     */
    public final static File bytes2File(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            if(!file.getParentFile().exists()){
                boolean mkdirs = file.getParentFile().mkdirs();
            }
            boolean newFile = file.createNewFile();
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    // 用于遍历sdcard卡上所有图片文件的类
    public static void DirAll(File dirFile) throws Exception {
        // 用于存放sdcard卡上的所有图片路径
        ArrayList<String> dirAllStrArr = new ArrayList<String>();
        if (dirFile.exists()) {
            File files[] = dirFile.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    String fileName = file.getName();
                    // 除sdcard上Android这个文件夹以外。
                    if (!fileName.endsWith("Android")) {
                        // 如果遇到文件夹则递归调用。
                        DirAll(file);
                    }
                } else {
                    // 如果是图片文件压入数组
                    String fileName = file.getName();
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                            || fileName.endsWith(".bmp")
                            || fileName.endsWith(".gif")
                            || fileName.endsWith(".png")) {
                        // 如果遇到文件则放入数组
                        if (dirFile.getPath().endsWith(File.separator)) {
                            dirAllStrArr
                                    .add(dirFile.getPath() + file.getName());
                        } else {
                            dirAllStrArr.add(dirFile.getPath() + File.separator + file.getName());
                        }
                    }
                }
            }
        }
    }
}
