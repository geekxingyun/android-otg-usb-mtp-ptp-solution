# AndroidOtgUSBMtpSample
一个实现读取USB设备所有图片并复制到到手机内部存储中的Android解决方案

这个解决方案可以解决这样几个场景下的问题:

1. 读取单反相机上的所有图片并保存到手机自带的存储中

2. 读取USB 设备上的所有图片并保存到手机自带的存储卡中

3. MTP协议的一个替代解决方案，可读取MTP设备中的图片并保存到手机自带的存储卡中

代码文字讲解：https://blog.csdn.net/hadues/article/details/83153615

代码视频讲解：https://github.com/geekxingyun/AndroidOtgUSBMtpSample/tree/master/VideoIntroduceForCodeSample

# 使用方法

## 添加项目依赖

  implementation 'com.github.mjdev:libaums:0.5.5'

## 添加权限

  <!-- USB 读写权限 -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

   <!-- 部分手机上是没有otg的所以需要加入特性-->
    <uses-feature android:name="android.hardware.usb.host" android:required="true" />
  
 ## 复制项目中的相关代码即可
    
