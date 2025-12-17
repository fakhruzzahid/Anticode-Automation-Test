import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

import cms.HomepageKeywords as HK

// Load schema
HK.loadSchema('Data Files/homepage.json')

// Open CMS
WebUI.openBrowser('https://your-cms-url.com/admin/homepage')
WebUI.maximizeWindow()

// Define data for each section
def heroData = [
	status: true,
	background_image: 'Test Data/TestImages/hero.jpg',
	scroll_indicator: true
]

def aboutData = [
	status: true,
	section_title: 'About Us',
	description: 'We provide excellent services'
]

def galleryData = [
	section_title: 'Gallery',
	gallery_images: [
		[image: 'Test Data/TestImages/img1.jpg', caption: 'Image 1'],
		[image: 'Test Data/TestImages/img2.jpg', caption: 'Image 2']
	]
]

// Fill sections using dynamic method
HK.fillSection('hero', heroData)
HK.fillSection('about', aboutData)
HK.fillSection('gallery', galleryData)

// Submit
WebUI.click(new com.kms.katalon.core.testobject.TestObject().addProperty('xpath',
	com.kms.katalon.core.testobject.ConditionType.EQUALS, "//button[@type='submit']"))

WebUI.closeBrowser()