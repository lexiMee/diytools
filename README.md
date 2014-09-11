diytools
========
这边会慢慢收录一些工作上自己diy的小工具，主要是帮助自己分析和测试以及获取相关手机信息的小工具。

******************************************************************
gps_nmea_to_excel.py

一、环境搭建
安装好python环境之后，安装setuptools、pynmea2、xlwt这三个库。python相关库下载地址http://pan.baidu.com/s/1hqzMBnm，pynmea2内有该库的使用说明。

二、使用场景
对于含有nmea日志的文本文件，该文件可以解析nmea，并统计fix2D和fix3D的情况，然后输出到excel，我们可以借住excel工具将其导成图表来显示整个导航过程，丢定位的情况。

三、使用方法
python main.log excelfilename
******************************************************************
