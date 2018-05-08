import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as MobileBuiltInKeywords
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WSBuiltInKeywords
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

/**
 * This code demonstrates how effectively DriverFactory.changeWebDriver() method can be used. 
 * 
 * As "https://forum.katalon.com/discussion/6150/google-chrome-crashed-on-my-pc-----2-reasons-found" records,
 * When Katalon Studio opens Google Chrome browser may crashed if Force-Installed-Extension is installed in Chrome.
 * It is because Katalon Studio tries to start Chrome with '--disable-extensions' and '--disable-extensions-except'
 * switches while Force-Installed-Extensions resist to be disabled. Contradiction!
 * 
 * DriverFactory.changeWebDriver() enables us to make a workaround for this issue. We instanciates ChromeDriver
 * for ourself (rather than relying on the default ChromeDriver instanciated by Katalon Studio),
 * and let Katalon to use the manually-instanciated ChromeDriver. This way works! 
 */
// https://forum.katalon.com/discussion/comment/15164#Comment_15164
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities
import com.kms.katalon.core.webui.driver.DriverFactory

System.setProperty('webdriver.chrome.driver', 'C:/Katalon_Studio_Windows_64-5.4.1/configuration/resources/drivers/chromedriver_win32/chromedriver.exe')
System.setProperty('webdriver.chrome.logfile', 'C:/Users/qcq0264/tmp/chromedriver.log')
WebDriver driver = new ChromeDriver()
DriverFactory.changeWebDriver(driver)

// 単純なテストを実行する
WebUI.navigateToUrl('http://demoaut.katalon.com')
WebUI.verifyElementPresent(findTestObject("Page_CURA Healthcare Service/a_Make Appointment"),20)
WebUI.closeBrowser()
