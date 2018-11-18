# AndroidOtgUSBMtpSample

最近在做一个手机连接单反相机，最终目的是实现相册直播边拍边传功能。

经过一番尝试，发现 Android 应用获取外部设备文件一共有这样四种方式

1. 内容提供器 （Content-Provider） ---已测试,独占模式, 而且需要手动点击导入到手机系统相册中才能使用

2. USB 传输协议 ----------------已测试,不支持单反相机，仅支持单反相机取出来内存卡数据读取

3. MTP传输协议 （Media Transfer Protocol）-----已测试,独占模式,可导出真实图片和缩略图

4. PTP 传输协议 （Picture Transfer Protocol）Digital Camera 数码相机----待测试

据说，手机连接数码相机想边拍边传只能使用PTP传输协议，其他两种都不行。

今天有了新的发现

> EOS Utility 是一款用于与您的 EOS DIGITAL 相机进行通信的软件。 通过将相机与电脑连接,您可将 相机内存卡中所存储的图像下载至电脑,还可进行各种相机设置或通过电脑上的 EOS Utility 进行远程拍摄
 
据说 EOS Utility 可以在电脑上远程拍摄照片

EOS Utility PC版下载地址： http://support-cn.canon-asia.com/contents/CN/ZH/0200570705.html

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
    
# 参考资料：
```

https://www.howtogeek.com/192732/android-usb-connections-explained-mtp-ptp-and-usb-mass-storage/

https://github.com/magnusja/libaums

https://blog.csdn.net/csdn635406113/article/details/70146041

https://developer.android.google.cn/guide/topics/providers/document-provider#create

https://github.com/ynyao/cameraphoto

https://www.jianshu.com/p/55eae30d133c

https://github.com/michaelzoech/remoteyourcam-usb

https://github.com/terencehonles/Android_USB_PTP_Lib

https://github.com/Fimagena/libptp

https://sourceforge.net/projects/jphoto/

```
