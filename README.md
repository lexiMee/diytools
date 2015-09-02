diytools
========
这边会慢慢收录一些工作上自己diy的小工具，主要是帮助自己分析和测试以及获取相关手机信息的小工具。

******************************************************************
工具一、gps_nmea_to_excel.py

一、环境搭建
安装好python环境之后，安装setuptools、pynmea2、xlwt这三个库。python相关库下载地址http://pan.baidu.com/s/1hqzMBnm
pynmea2内有该库的使用说明。

二、使用场景
对于含有nmea日志的文本文件，该文件可以解析nmea，并统计fix2D和fix3D的情况，然后输出到excel，我们可以借住excel工具将其导成图表来显示整个导航过程，丢定位的情况。

三、使用方法
python main.log excelfilename
******************************************************************
工具二、pcm2wav

一、环境搭建
只要安装了JDK即可。

二、使用场景
可以将pcm数据转换成wav，方便播放而不用每次都需要选择采样率、位宽、通道数。
mtk平台下面audio dump出来的数据中有以下几支文件，对应的采样率、位宽和通道数：
audiotrack 16 bit, 2ch, 48000 Hz 
audio mixer 32 bit, 2ch, 44100 Hz 
AudioALSAStreamOut 32 bit, 2ch, 44100 Hz
AudioALSAPlaybackHandlerNormal 24 bit, 2ch, 44100 Hz 
BTSCO  2ch 24bit 8k

mtk特殊场景，AudioALSAPlayback出来的数据是32bit的，但是如果要转成可以听的wav需要将数据转为24bit。
此外采样率需要根据具体场景来看，对于audio_policy.conf下为44100的情况，那应该都会转成44.1但是如果有不同配置则需要看如何修改。

******************************************************************


