./tesseract.exe CheckCode\[1\].gif num  -l kng

set TESSDATA_PREFIX=.\lib\ocr
set PATH=.\lib\ocr;.\lib\img;%PATH%
rem convert -crop "60x20+426+340" tms.jpg  num.jpg 
tesseract num.png num -l kng