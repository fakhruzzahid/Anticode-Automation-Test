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

// Open CMS page
WebUI.openBrowser('https://your-cms-url.com/admin/homepage')
WebUI.maximizeWindow()

// === HERO SECTION ===
WebUI.comment('=== Filling Hero Section ===')
HK.setToggle('hero', 'status', true)
// Using relative path from project folder
HK.setMedia('hero', 'background_image', 'Test Data/TestImages/hero-bg.jpg')
HK.setToggle('hero', 'scroll_indicator', true)

// === ABOUT SECTION ===
WebUI.comment('=== Filling About Section ===')
HK.setToggle('about', 'status', true)
HK.setInput('about', 'section_title', 'About Our Company')
HK.setTextarea('about', 'description', 'We are a leading company in the industry with over 20 years of experience.')

// === GALLERY SECTION ===
WebUI.comment('=== Filling Gallery Section ===')
HK.setInput('gallery', 'section_title', 'Our Gallery')

// Add 3 gallery images
HK.addRepeaterItem('gallery', 'gallery_images', 0)
HK.setRepeaterField('gallery', 'gallery_images', 0, 'image', 'Test Data/TestImages/gallery1.jpg', 'media')
HK.setRepeaterField('gallery', 'gallery_images', 0, 'caption', 'Beautiful Sunset', 'input')

HK.addRepeaterItem('gallery', 'gallery_images', 1)
HK.setRepeaterField('gallery', 'gallery_images', 1, 'image', 'Test Data/TestImages/gallery2.jpg', 'media')
HK.setRepeaterField('gallery', 'gallery_images', 1, 'caption', 'Mountain View', 'input')

HK.addRepeaterItem('gallery', 'gallery_images', 2)
HK.setRepeaterField('gallery', 'gallery_images', 2, 'image', 'Test Data/TestImages/gallery3.jpg', 'media')
HK.setRepeaterField('gallery', 'gallery_images', 2, 'caption', 'City Lights', 'input')

// === BRANDS SECTION ===
WebUI.comment('=== Filling Brands Section ===')
HK.setToggle('brands', 'status', true)
HK.setInput('brands', 'section_title', 'Our Trusted Brands')
HK.setTextEditor('brands', 'content', '<p>We partner with the best brands in the industry.</p>')

// === SERVICES SECTION ===
WebUI.comment('=== Filling Services Section ===')
HK.setToggle('services', 'status', true)
HK.setInput('services', 'section_title', 'Our Services')
HK.setPostRelated('services', 'services', ['service_1', 'service_2', 'service_3'])

// === EVENTS SECTION ===
WebUI.comment('=== Filling Events Section ===')
HK.setToggle('events', 'status', true)
HK.setInput('events', 'section_title', 'Upcoming Events')
HK.setPostRelated('events', 'events', ['event_1', 'event_2'])

// === OUR STORIES SECTION ===
WebUI.comment('=== Filling Our Stories Section ===')
HK.setToggle('our_stories', 'status', true)
HK.setInput('our_stories', 'section_title', 'Our Stories')
HK.setPostRelated('our_stories', 'stories', ['story_1', 'story_2', 'story_3'])

// === CALL TO ACTION SECTION ===
WebUI.comment('=== Filling Call to Action Section ===')
HK.setToggle('call_to_action', 'status', true)
HK.setInput('call_to_action', 'section_title', 'Get Started Today')
HK.setTextEditor('call_to_action', 'content', '<h2>Ready to join us?</h2><p>Contact us now!</p>')

// Submit form
WebUI.click(new com.kms.katalon.core.testobject.TestObject().addProperty('xpath',
	com.kms.katalon.core.testobject.ConditionType.EQUALS, "//button[@type='submit']"))

WebUI.delay(2)
WebUI.closeBrowser()
