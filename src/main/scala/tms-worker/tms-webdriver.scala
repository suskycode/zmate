package im.xun.zmate

import scala.collection.JavaConversions._
import java.nio.file.StandardCopyOption._
import java.nio.file.Files
import java.io.File
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.firefox._
import org.openqa.selenium.OutputType
import org.openqa.selenium._
import sys.process._

object TmsRun {
  def run(cachePath: String): Boolean = {
    val driver = new InternetExplorerDriver
    driver.get("http://tms.zte.com.cn")
    driver.switchTo.frame(5)
    driver.findElement(By.id("linkmenussb.atm.menu.item.onlinecheck")).click

    val f= getFileTree(new File(cachePath)).filter( f=> """.*CheckCode.*""".r.findFirstIn(f.getName).isDefined).head

    val newfile = new File("code.gif")
    Files.copy(f.toPath,newfile.toPath,REPLACE_EXISTING)
    "ocr/tesseract.exe code.gif num -l kng".!!

    val code = scala.io.Source.fromFile("num.txt").mkString.trim
    val ele_code = driver.findElement(By.id("txtpas"))
    println("Check code" + code)
    ele_code.sendKeys(code)
    driver.findElement(By.id("btnSubmit")).click
    val  alert = driver.switchTo.alert
    val msg = alert.getText

    /*TODO:clean. Should put in try final later*/
    driver.quit

    if(msg.contains("成功")) {
      return true
    }
    return false
  }

  def getFileTree(f: File): Stream[File] = {
    f #:: Option(f.listFiles()).toStream.flatten.flatMap(getFileTree)
  }

}
