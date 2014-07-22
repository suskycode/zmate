package im.xun.zmate

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome._

object TmsRun {
  def run(): Boolean = {
    var driver = new ChromeDriver()
    driver.get("http://baidu.com")
    driver.quit
    return true
  }
}
