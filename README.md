# AndroidOtgUSBMtpSample

最近在做一个手机连接单反相机，最终目的是实现相册直播边拍边传功能。

经过一番尝试，发现 Android 应用获取外部设备文件一共有这样四种方式

1. 内容提供器 （Content-Provider） ---已测试,独占模式, 而且需要手动点击导入到手机系统相册中才能使用

2. USB 传输协议 ----------------已测试,不支持单反相机，仅支持单反相机取出来内存卡数据读取

3. MTP传输协议 （Media Transfer Protocol）-----已测试,独占模式,可导出真实图片和缩略图

4. PTP 传输协议 （Picture Transfer Protocol）Digital Camera 数码相机----有同行测试成功

据说，手机连接数码相机想边拍边传只能使用PTP传输协议，其他两种都不行。

于是我去了单反官网，找官方客服寻找资料，客服提供了一个线索 EOS Utility PC端软件可以，移动端暂时没有配套软件。

> EOS Utility 是一款用于与您的 EOS DIGITAL 相机进行通信的软件。 通过将相机与电脑连接,您可将 相机内存卡中所存储的图像下载至电脑,还可进行各种相机设置或通过电脑上的 EOS Utility 进行远程拍摄
 
据客服所说 EOS Utility 可以通过Wifi 连接模式或者WLAN USB 桥接模式实现单反相机控制，当然边拍边传也可以实现

EOS Utility PC版下载地址： http://support-cn.canon-asia.com/contents/CN/ZH/0200570705.html

经过测试，将USB数据线将数码相机和单反相机连接后，确实可以实现实现边拍边传效果，但是我们需要的是移动端哈

# 最终解决方案

直到今天，终于有了最终解决方案，参考以下类库可实现相册直播边拍边传效果~

https://github.com/terencehonles/Android_USB_PTP_Lib

https://github.com/michaelzoech/remoteyourcam-usb

PS: 

1. 一个同行测试成功了，但是我测试报一个空指针异常，不知道什么原因~ 

2. 虽然据说的这是使用的PTP协议，但我觉得更像是模拟PC端软件的驱动指令以Java NDK/JNI 方式封装，然后移动端调用。

3. 另外关于相机操作指令还有一种叫做Jphoto 的Java 类库，但是我没看懂怎么使用，有兴趣的朋友可以研究下。

## 成熟产品
如果企业想使用稳定成熟产品，请移步：https://y.camera360.com/

------------------------------------------------------------------

## 测试报告如下

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

https://github.com/Fimagena/libptp

https://sourceforge.net/projects/jphoto/

https://github.com/terencehonles/Android_USB_PTP_Lib

https://github.com/michaelzoech/remoteyourcam-usb

```
