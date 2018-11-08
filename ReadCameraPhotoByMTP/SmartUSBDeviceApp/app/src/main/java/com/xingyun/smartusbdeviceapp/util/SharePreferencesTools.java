package com.xingyun.smartusbdeviceapp.util;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.xingyun.smartusbdeviceapp.MyApplication;
import com.xingyun.smartusbdeviceapp.model.CameraPictureBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

public class SharePreferencesTools {

    /**
     * 方法描述: SharePreferences 保存对象
     * 调用方法SharePreferencesSaveObject(Context context,String fileName,String key ,Object obj);
     * @param context  上下文对象
     * @param fileName SharePreference 文件名称
     * @param key Key
     * @param obj 要保存的对象
     * @return void
     * */
    /*******************SharePreferences 保存对象 *****************************/
    public static void saveObjectToSharePreferences(String fileName, String key, Object obj){
        try {
            // 保存对象
            SharedPreferences.Editor sharedata = MyApplication.getContext().getSharedPreferences(fileName,0).edit();
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            ObjectOutputStream os=new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            sharedata.putString(key, bytesToHexString);
            sharedata.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * desc:将数组转为16进制
     * @param bArray
     * @return String
     */
    private static String bytesToHexString(byte[] bArray) {
        if(bArray == null){
            return null;
        }
        if(bArray.length == 0){
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
    /*******************SharePreferences 保存对象 *****************************/

    /**
     * 方法描述: 获取SharePreferences保存的Object对象
     * 调用方法     Object obj= readSharePreferencesObject(Context context,String fileName,String key );
     * @param context  上下文对象
     * @param filename 读取文件名
     * @param key
     * @return 异常返回null,成功返回该对象
     */
    /*******************读取 SharePreferences 保存对象 *****************************/
    public static List<CameraPictureBean> readObjectFromSharePreferences(String fileName, String key ){
        try {
            SharedPreferences sharedata = MyApplication.getContext().getSharedPreferences(fileName, 0);
            if (sharedata.contains(key)) {
                String string = sharedata.getString(key,"");
                if(TextUtils.isEmpty(string)){
                    return null;
                }else{
                    //将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = StringToBytes(string);
                    ByteArrayInputStream bis=new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is=new ObjectInputStream(bis);
                    //返回反序列化得到的对象
                    List<CameraPictureBean> readObject = (List<CameraPictureBean>) is.readObject();
                    return readObject;
                }
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //所有异常返回null
        return null;

    }
    /**
     * desc:将16进制的数据转为数组
     * @param  data
     * @return
     */
    private static byte[] StringToBytes(String data){
        String hexString=data.toUpperCase().trim();
        if (hexString.length()%2!=0) {
            return null;
        }
        byte[] retData=new byte[hexString.length()/2];
        for(int i=0;i<hexString.length();i++)
        {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch1;
            if(hex_char1 >= '0' && hex_char1 <='9')
                int_ch1 = (hex_char1-48)*16;   //// 0 的Ascll - 48
            else if(hex_char1 >= 'A' && hex_char1 <='F')
                int_ch1 = (hex_char1-55)*16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch2;
            if(hex_char2 >= '0' && hex_char2 <='9')
                int_ch2 = (hex_char2-48); //// 0 的Ascll - 48
            else if(hex_char2 >= 'A' && hex_char2 <='F')
                int_ch2 = hex_char2-55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1+int_ch2;
            retData[i/2]=(byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }
    /*******************读取 SharePreferences 保存对象 *****************************/
}
