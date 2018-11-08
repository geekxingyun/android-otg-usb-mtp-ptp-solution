# AndroidOtgUSBMtpSample

最近在做一个手机连接单反相机，最终目的是实现相册直播功能。

经过一番尝试，发现 Android 应用获取外部设备文件一共有这样四种方式

1. 内容提供器 （Content-Provider）

2. USB 传输协议 

3. MTP传输协议 （Media Transfer Protocol）

4. PTP 传输协议 （Picture Transfer Protocol）Digital Camera 数码相机

参考：https://www.howtogeek.com/192732/android-usb-connections-explained-mtp-ptp-and-usb-mass-storage/

经测试发现，手机连接数码相机想边拍边传只能使用PTP传输协议，其他两种都不行。

PTP协议相关类库（待测试通过）

https://github.com/michaelzoech/remoteyourcam-usb

------------------------------------------------------------------

MTP模式访问单反相机照片并复制到手机存储上（测试通过）

https://github.com/geekxingyun/AndroidOtgUSBMtpSample/tree/master/ReadCameraPhotoByMTP

读取单反大卡USB设备文件并复制到手机存储上 （测试通过）

代码文字讲解：https://blog.csdn.net/hadues/article/details/83153615

代码视频讲解：https://github.com/geekxingyun/AndroidOtgUSBMtpSample/tree/master/ReadCameraPhotoByUSB

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
    
