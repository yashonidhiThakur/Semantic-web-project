import re

with open("XCommerce/src/main/resources/static/product-search.html", "r") as f:
    content = f.read()

# Fix the Icon innerHTML bug
content = content.replace('spanRef.current.innerHTML = `<Icon name="${name}" cls="${cls || \'\'}" />`;', 'spanRef.current.innerHTML = `<i data-lucide="${name}" class="${cls || \'\'}"></i>`;')

# Fix filterOptions mapping
fixed_options = """          Laptop: {
            Brand: ["Dell", "HP", "Lenovo", "Apple", "Asus", "Acer", "MSI"],
            Color: ["Black", "Silver", "Gray", "White", "Blue"],
            Storage: ["256GB", "512GB", "1TB", "2TB"],
            RAM: ["8", "16", "32", "64"],
            OS: ["Windows", "macOS", "Linux"],
            DisplayTechnology: ["OLED", "IPS", "Mini-LED", "Retina"],
            DisplaySize: ["13.3", "13.4", "14.0", "15.6", "16.0"],
            ProcessorSpeed: ["2", "3", "4", "5"],
            ManufacturingYear: ["2023", "2024", "2025", "2026"],
          },
          Mobile: {
            Brand: ["Apple", "Samsung", "OnePlus", "Google", "Xiaomi", "Vivo"],
            Color: ["Black", "White", "Blue", "Green", "Purple"],
            Storage: ["128GB", "256GB", "512GB", "1TB"],
            OS: ["Android", "iOS"],
            DisplayTechnology: ["LTPO AMOLED", "Super Retina XDR", "OLED"],
            DisplaySize: ["6.1", "6.3", "6.7", "6.8"],
            BatteryLife: ["24", "36", "48", "52"],
            Pixels: ["48MP", "50MP", "108MP", "200MP"],
            SIMSlots: ["1", "2"],
            ManufacturingYear: ["2023", "2024", "2025", "2026"],
          },
"""
# Replace Laptop and Mobile blocks
old_laptop_mobile = """          Laptop: {
            Brand: ["Dell", "HP", "Lenovo", "Apple", "Asus", "Acer", "MSI"],
            Color: ["Black", "Silver", "Gray", "White", "Blue"],
            HDDValue: ["256", "512", "1024", "2048"],
            HDDUnit: ["GB", "TB"],
            RAM: ["8", "16", "32", "64"],
            OS: ["Windows", "macOS", "Linux"],
            DisplayTechnology: ["OLED", "IPS", "Mini-LED", "Retina"],
            DisplaySize: ["13.3", "13.4", "14.0", "15.6", "16.0"],
            ProcessorSpeed: ["2", "3", "4", "5"],
            ManufacturingYear: ["2023", "2024", "2025", "2026"],
          },
          Mobile: {
            Brand: ["Apple", "Samsung", "OnePlus", "Google", "Xiaomi", "Vivo"],
            Color: ["Black", "White", "Blue", "Green", "Purple"],
            StorageValue: ["128", "256", "512", "1024"],
            StorageUnit: ["GB"],
            OS: ["Android", "iOS"],
            DisplayTechnology: ["LTPO AMOLED", "Super Retina XDR", "OLED"],
            DisplaySize: ["6.1", "6.3", "6.7", "6.8"],
            BatteryLife: ["24", "36", "48", "52"],
            Pixels: ["48MP", "50MP", "108MP", "200MP"],
            SIMSlots: ["1", "2"],
            ManufacturingYear: ["2023", "2024", "2025", "2026"],
          },
"""
content = content.replace(old_laptop_mobile, fixed_options)

with open("XCommerce/src/main/resources/static/product-search.html", "w") as f:
    f.write(content)

print("Done")
