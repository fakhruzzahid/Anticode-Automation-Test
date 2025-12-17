package cms

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable
import com.kms.katalon.core.testobject.ConditionType
import groovy.json.JsonSlurper

public class HomepageKeywords {
	
	private static def homepageSchema
	
	/**
	 * Load JSON schema
	 */
	static def loadSchema(String jsonPath) {
		def jsonFile = new File(jsonPath)
		def jsonSlurper = new JsonSlurper()
		homepageSchema = jsonSlurper.parse(jsonFile)
		return homepageSchema
	}
	
	/**
	 * Generate dynamic Test Object based on pattern
	 * Pattern: cf__{keyName}_section__{fieldName}
	 */
	static TestObject createTestObject(String keyName, String fieldName) {
		TestObject obj = new TestObject()
		String locator = "cf__${keyName}_section__${fieldName}"
		obj.addProperty("name", ConditionType.EQUALS, locator)
		return obj
	}
	
	/**
	 * Handle TOGGLE field (Enable/Disable)
	 */
	static void setToggle(String keyName, String fieldName, boolean value) {
		TestObject toggleObj = createTestObject(keyName, fieldName)
		
		WebUI.waitForElementPresent(toggleObj, 10)
		
		// Check current state
		boolean isChecked = WebUI.verifyElementChecked(toggleObj, 0, com.kms.katalon.core.model.FailureHandling.OPTIONAL)
		
		// Click only if different from desired state
		if (isChecked != value) {
			WebUI.click(toggleObj)
			WebUI.comment("Toggle ${keyName}.${fieldName} set to: ${value}")
		}
	}
	
	/**
	 * Handle INPUT field (Text input)
	 */
	static void setInput(String keyName, String fieldName, String value) {
		TestObject inputObj = createTestObject(keyName, fieldName)
		
		WebUI.waitForElementPresent(inputObj, 10)
		WebUI.clearText(inputObj)
		WebUI.setText(inputObj, value)
		WebUI.comment("Input ${keyName}.${fieldName} set to: ${value}")
	}
	
	/**
	 * Handle TEXTAREA field
	 */
	static void setTextarea(String keyName, String fieldName, String value) {
		TestObject textareaObj = createTestObject(keyName, fieldName)
		
		WebUI.waitForElementPresent(textareaObj, 10)
		WebUI.clearText(textareaObj)
		WebUI.setText(textareaObj, value)
		WebUI.comment("Textarea ${keyName}.${fieldName} set to: ${value}")
	}
	
	/**
	 * Handle TEXT EDITOR field (Rich text with iframe)
	 */
	static void setTextEditor(String keyName, String fieldName, String value) {
		TestObject editorObj = createTestObject(keyName, fieldName)
		
		WebUI.waitForElementPresent(editorObj, 10)
		
		// Check if it's inside iframe
		if (WebUI.verifyElementPresent(
			new TestObject().addProperty("xpath", ConditionType.EQUALS, "//iframe[contains(@id, '${keyName}_${fieldName}')]"),
			5, com.kms.katalon.core.model.FailureHandling.OPTIONAL)) {
			
			// Switch to iframe
			WebUI.switchToFrame(
				new TestObject().addProperty("xpath", ConditionType.EQUALS, "//iframe[contains(@id, '${keyName}_${fieldName}')]"),
				10
			)
			
			// Find contenteditable element
			TestObject contentEditable = new TestObject()
			contentEditable.addProperty("xpath", ConditionType.EQUALS, "//body[@contenteditable='true']")
			
			WebUI.waitForElementPresent(contentEditable, 10)
			WebUI.clearText(contentEditable)
			WebUI.setText(contentEditable, value)
			
			// Switch back to main frame
			WebUI.switchToDefaultContent()
		} else {
			// Direct contenteditable
			WebUI.clearText(editorObj)
			WebUI.setText(editorObj, value)
		}
		
		WebUI.comment("Text Editor ${keyName}.${fieldName} set to: ${value}")
	}
	
	/**
	 * Handle MEDIA field (File upload with validation)
	 */
	static void setMedia(String keyName, String fieldName, String filePath, Map validation = [:]) {
		TestObject mediaObj = createTestObject(keyName, fieldName)
		
		WebUI.waitForElementPresent(mediaObj, 10)
		
		// Convert to absolute path if relative path provided
		String absolutePath = filePath
		if (!filePath.contains(':') && !filePath.startsWith('/')) {
			// Relative path - convert to absolute
			absolutePath = com.kms.katalon.core.configuration.RunConfiguration.getProjectDir() + '/' + filePath
		}
		
		// Validate file exists
		File file = new File(absolutePath)
		if (!file.exists()) {
			WebUI.comment("ERROR: File not found: ${absolutePath}")
			return
		}
		
		// Validate file type
		if (validation.containsKey('accept')) {
			def allowedTypes = validation.accept
			String fileExt = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase()
			
			boolean isValid = false
			allowedTypes.each { type ->
				if (type == 'image' && ['jpg', 'jpeg', 'png', 'gif', 'webp', 'svg'].contains(fileExt)) {
					isValid = true
				}
			}
			
			if (!isValid) {
				WebUI.comment("WARNING: File type ${fileExt} may not be accepted")
			}
		}
		
		// Validate file size (example: max 5MB)
		long fileSizeInMB = file.length() / (1024 * 1024)
		if (fileSizeInMB > 5) {
			WebUI.comment("WARNING: File size ${fileSizeInMB}MB exceeds recommended limit")
		}
		
		// Upload file
		WebUI.uploadFile(mediaObj, filePath)
		WebUI.comment("Media uploaded: ${keyName}.${fieldName} = ${filePath}")
	}
	
	/**
	 * Handle REPEATER field (Dynamic list items)
	 */
	static void addRepeaterItem(String keyName, String fieldName, int index = 0) {
		// Button to add new item: usually has pattern "add_{fieldName}_button"
		TestObject addButton = new TestObject()
		addButton.addProperty("xpath", ConditionType.EQUALS, "//button[contains(@data-field, '${fieldName}') and contains(., 'Add')]")
		
		WebUI.waitForElementPresent(addButton, 10)
		WebUI.click(addButton)
		WebUI.delay(1)
		WebUI.comment("Added repeater item: ${keyName}.${fieldName}[${index}]")
	}
	
	static void setRepeaterField(String keyName, String fieldName, int itemIndex, String subFieldName, String value, String fieldType = 'input') {
		// Pattern: cf__{keyName}_section__{fieldName}[{index}]__{subFieldName}
		TestObject repeaterFieldObj = new TestObject()
		String locator = "cf__${keyName}_section__${fieldName}[${itemIndex}]__${subFieldName}"
		repeaterFieldObj.addProperty("name", ConditionType.EQUALS, locator)
		
		WebUI.waitForElementPresent(repeaterFieldObj, 10)
		
		switch(fieldType.toLowerCase()) {
			case 'input':
			case 'text':
				WebUI.setText(repeaterFieldObj, value)
				break
			case 'media':
				// Convert to absolute path if relative path provided
				String absolutePath = value
				if (!value.contains(':') && !value.startsWith('/')) {
					absolutePath = com.kms.katalon.core.configuration.RunConfiguration.getProjectDir() + '/' + value
				}
				WebUI.uploadFile(repeaterFieldObj, absolutePath)
				break
			default:
				WebUI.setText(repeaterFieldObj, value)
		}
		
		WebUI.comment("Repeater field set: ${keyName}.${fieldName}[${itemIndex}].${subFieldName} = ${value}")
	}
	
	static void removeRepeaterItem(String keyName, String fieldName, int itemIndex) {
		// Button to remove item at specific index
		TestObject removeButton = new TestObject()
		removeButton.addProperty("xpath", ConditionType.EQUALS,
			"//div[contains(@data-field, '${fieldName}')][${itemIndex + 1}]//button[contains(., 'Remove') or contains(@class, 'delete')]")
		
		if (WebUI.verifyElementPresent(removeButton, 5, com.kms.katalon.core.model.FailureHandling.OPTIONAL)) {
			WebUI.click(removeButton)
			WebUI.delay(1)
			WebUI.comment("Removed repeater item: ${keyName}.${fieldName}[${itemIndex}]")
		}
	}
	
	/**
	 * Handle POST_RELATED field (Select related posts)
	 */
	static void setPostRelated(String keyName, String fieldName, List<String> postIds) {
		TestObject selectObj = createTestObject(keyName, fieldName)
		
		WebUI.waitForElementPresent(selectObj, 10)
		
		postIds.each { postId ->
			// This depends on your UI implementation (dropdown, multi-select, etc)
			WebUI.selectOptionByValue(selectObj, postId, false)
			WebUI.delay(0.5)
		}
		
		WebUI.comment("Post Related selected: ${keyName}.${fieldName} = ${postIds}")
	}
	
	/**
	 * Validate field by schema definition
	 */
	static boolean validateField(String keyName, String fieldName, def value) {
		def component = homepageSchema.components.find { it.keyName == keyName }
		if (!component) return false
		
		def field = component.fields.find { it.name == fieldName }
		if (!field) return false
		
		def attribute = field.attribute
		
		// Validate based on field type
		switch(field.field) {
			case 'input':
				if (attribute.maxLength && value.length() > attribute.maxLength) {
					WebUI.comment("ERROR: ${fieldName} exceeds maxLength (${attribute.maxLength})")
					return false
				}
				if (attribute.minLength && value.length() < attribute.minLength) {
					WebUI.comment("ERROR: ${fieldName} below minLength (${attribute.minLength})")
					return false
				}
				break
				
			case 'textarea':
				if (attribute.max && value.length() > attribute.max) {
					WebUI.comment("ERROR: ${fieldName} exceeds max characters (${attribute.max})")
					return false
				}
				break
				
			case 'repeater':
				if (attribute.min && value.size() < attribute.min) {
					WebUI.comment("ERROR: ${fieldName} below minimum items (${attribute.min})")
					return false
				}
				if (attribute.max && value.size() > attribute.max) {
					WebUI.comment("ERROR: ${fieldName} exceeds maximum items (${attribute.max})")
					return false
				}
				break
		}
		
		return true
	}
	
	/**
	 * Fill entire section dynamically from data
	 */
	static void fillSection(String keyName, Map<String, Object> data) {
		data.each { fieldName, value ->
			def component = homepageSchema.components.find { it.keyName == keyName }
			def field = component?.fields?.find { it.name == fieldName }
			
			if (!field) {
				WebUI.comment("WARNING: Field ${fieldName} not found in schema")
				return
			}
			
			// Route to appropriate handler based on field type
			switch(field.field) {
				case 'toggle':
					setToggle(keyName, fieldName, value as boolean)
					break
				case 'input':
					setInput(keyName, fieldName, value as String)
					break
				case 'textarea':
					setTextarea(keyName, fieldName, value as String)
					break
				case 'texteditor':
					setTextEditor(keyName, fieldName, value as String)
					break
				case 'media':
					setMedia(keyName, fieldName, value as String, field.attribute)
					break
				case 'post_related':
					setPostRelated(keyName, fieldName, value as List)
					break
				case 'repeater':
					// Handle repeater items
					(value as List).eachWithIndex { item, idx ->
						addRepeaterItem(keyName, fieldName, idx)
						item.each { subField, subValue ->
							def subFieldDef = field.attribute.fields.find { it.name == subField }
							setRepeaterField(keyName, fieldName, idx, subField, subValue as String, subFieldDef?.field)
						}
					}
					break
			}
		}
	}
}