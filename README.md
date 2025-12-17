# 📋 Katalon CMS Homepage Automation

Automation testing untuk CMS Homepage dengan JSON-driven approach.

## 📂 Struktur Folder
```
KatalonProject/
├── Keywords/cms/HomepageKeywords.groovy    # Custom Keywords
├── Test Cases/Homepage/                    # Test Cases
├── Test Data/TestImages/                   # File upload (gambar, dll)
└── Data Files/homepage.json                # JSON Schema
```

## 🚀 Setup

1. Copy `HomepageKeywords.groovy` ke `Keywords/cms/`
2. Copy `homepage.json` ke `Data Files/`
3. Taruh file gambar di `Test Data/TestImages/`

## 📝 HTML Pattern

Semua field HTML harus pakai naming pattern ini:
```html
<input name="cf__hero_section__status">
<input name="cf__about_section__section_title">
```

Pattern: `cf__{keyName}_section__{fieldName}`

## 🎯 Cara Pakai

### Import
```groovy
import cms.HomepageKeywords as HK
```

### Basic
```groovy
// Toggle ON/OFF
HK.setToggle('hero', 'status', true)

// Input text
HK.setInput('about', 'section_title', 'About Us')

// Upload file (gunakan relative path)
HK.setMedia('hero', 'background_image', 'Test Data/TestImages/hero.jpg')
```

### Gallery / Repeater
```groovy
// Tambah item
HK.addRepeaterItem('gallery', 'gallery_images', 0)

// Isi field
HK.setRepeaterField('gallery', 'gallery_images', 0, 'image', 
                    'Test Data/TestImages/gallery1.jpg', 'media')
HK.setRepeaterField('gallery', 'gallery_images', 0, 'caption', 'Image 1', 'input')
```

### Data-Driven (Recommended)
```groovy
def heroData = [
    status: true,
    background_image: 'Test Data/TestImages/hero.jpg',
    scroll_indicator: true
]

HK.fillSection('hero', heroData)
```

## 🔧 Available Methods

| Method | Fungsi |
|--------|--------|
| `setToggle()` | Checkbox ON/OFF |
| `setInput()` | Input text |
| `setTextarea()` | Textarea |
| `setTextEditor()` | Rich text editor |
| `setMedia()` | Upload file |
| `addRepeaterItem()` | Tambah item repeater |
| `setRepeaterField()` | Isi field repeater |
| `fillSection()` | Isi 1 section sekaligus |

## ⚠️ Catatan Penting

✅ **File Path:** Gunakan relative path `Test Data/TestImages/file.jpg`  
✅ **HTML Pattern:** Pastikan HTML pakai `cf__{keyName}_section__{fieldName}`  
✅ **Validasi:** Otomatis cek file size, max length, dll

## 🐛 Troubleshooting

**Error: Element not found**
→ Check pattern HTML `name` attribute

**Error: File not found**
→ Pastikan file ada di `Test Data/TestImages/`
