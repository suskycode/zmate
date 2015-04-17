## 申明

1. 本项目（Zmate）目的仅为概念验证（proof of concept），请勿在正式环境中使用。
2. 版权为MIT

## 原理

通过[webdriver](https://code.google.com/p/selenium/wiki/InternetExplorerDriver)驱动IE浏览器（我司高大上的IT网站只支持IE），访问相关网站，并模拟用户登录，点击链接，在验证码输入界面，利用google的ocr工具[tesseract](https://code.google.com/p/tesseract-ocr/)识别验证码，完成网上刷卡。

## 使用方法

### IE配置(InternetExplorerDriver驱动限制）

a) 确保IE各个zone的安全级别相同。

b) 确保IE的View窗口放大比例为1:1。

### scheduler配置

默认的checkpoint为周一到周五每天的早上8点和晚上6点（前后5分钟某个随机时刻），提供3个文件用于修改配置：

1. holiday.txt 设置节假日。
2. deholiday.txt 设置需要上班的双休日。
3. rulezero.txt  直接指定具体的刷卡时间，优先级最高。

举个小栗子：

> 如2014年的五一是星期四，则节假日安排为周四，周五，周六放假，周日上班。对应的配置为：
> 
> holiday.txt中增加：
> 
> 2014-05-01
> 
> 2014-05-02
> 
> deholiday.txt中增加：
> 
> 2014-05-04

zmate会自动检测配置文件改动，按照新的规则生成scheduler，并打印出前5次触发点，用于核对。

### 关键技术

1.  [Selenium Webdriver](www.seleniumhq.org/projects/webdriver/)：通过webdriver协议驱动浏览器执行操作，除了IE，还支持其他所有主流浏览器，网站无法从数据包层面区分浏览点击是webdriver驱动的还是真实用户发起的，理论上可以突破最严格的爬虫限制网站。
2. [tesseract](https://code.google.com/p/tesseract-ocr/)：开源OCR工具，用于识别校验码。默认识别效果不好（正确率80%左右），通过[训练](https://code.google.com/p/tesseract-ocr/wiki/TrainingTesseract3)后，识别率可以达到99%左右（抓了几百张校验码测试）。训练生成的文件（https://github.com/wuhx/zmate/blob/master/ocr/tessdata/kng.traineddata)
3. [akka](http://akka.io/)：一个借鉴erlang的OTP模型的scala库，是zmate代码架构基础。
