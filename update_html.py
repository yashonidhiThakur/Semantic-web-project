import re

with open("XCommerce/src/main/resources/static/product-search.html", "r") as f:
    content = f.read()

# 1. Add Icon component
icon_def = """
        const Icon = ({ name, cls }) => {
            const spanRef = useRef(null);
            useEffect(() => {
                if (spanRef.current) {
                    spanRef.current.innerHTML = `<i data-lucide="${name}" class="${cls || ''}"></i>`;
                    lucide.createIcons({ root: spanRef.current });
                }
            }, [name, cls]);
            return <span ref={spanRef} className="inline-flex items-center justify-center" />;
        };

        const categories = useMemo(() => ["Electronic Devices", "Furniture"], []);
"""
content = content.replace("const categories = useMemo(() => [\"Electronic Devices\", \"Furniture\"], []);", icon_def)

# 2. Add Furniture filter options and fix Storage for Laptop/Mobile
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
          TV: {
            Brand: ["Samsung", "Sony", "LG", "TCL", "Hisense", "OnePlus"],
            DisplayTechnology: ["OLED", "QLED", "Mini-LED", "LED", "Neo QLED"],
            DisplaySize: ["43", "50", "55", "65", "75", "85"],
            ScreenResolution: ["3840 x 2160 (4K)", "7680 x 4320 (8K)"],
            OS: ["Android TV", "Tizen", "webOS", "Google TV"],
            ManufacturingYear: ["2023", "2024", "2025", "2026"],
          },
          Chair: {
            Brand: ["IKEA", "Herman Miller", "Steelcase", "Ashley", "Durian"],
            Color: ["Black", "White", "Brown", "Grey", "Blue"],
            Material: ["Wood", "Plastic", "Metal", "Leather", "Fabric"],
            Seaters: ["1", "2", "3"],
            HasArmRest: ["Yes", "No"],
            Dimensions: ["Standard", "Compact", "Large"]
          },
          Sofa: {
            Brand: ["IKEA", "Ashley", "Godrej", "Durian"],
            Color: ["Black", "Grey", "Beige", "Brown", "Blue"],
            Material: ["Leather", "Fabric", "Velvet"],
            SeatingCapacity: ["1", "2", "3", "4", "5", "6"],
            Shape: ["L-Shape", "U-Shape", "Straight"],
            Dimensions: ["Standard", "Large"]
          },
          Dining: {
            Brand: ["IKEA", "Godrej", "Durian", "HomeTown"],
            Color: ["Brown", "Black", "White", "Glass"],
            Material: ["Wood", "Glass", "Metal", "Marble"],
            Dimensions: ["4-Seater", "6-Seater", "8-Seater"]
          },
          Study: {
            Brand: ["IKEA", "Godrej", "Wakefit", "HomeTown"],
            Color: ["Brown", "White", "Black"],
            Material: ["Wood", "Metal", "Engineered Wood"],
            Dimensions: ["Standard", "Compact"]
          },
          Shelf: {
            Brand: ["IKEA", "Godrej", "HomeTown"],
            Color: ["Brown", "White", "Black"],
            Material: ["Wood", "Metal", "Glass"],
            Dimensions: ["Small", "Medium", "Large"]
          },
          Wardrobe: {
            Brand: ["IKEA", "Godrej", "Spacewood", "Wakefit"],
            Color: ["Brown", "White", "Grey", "Black"],
            Material: ["Wood", "Engineered Wood", "Metal"],
            Dimensions: ["2-Door", "3-Door", "4-Door"]
          }"""
# Extract and replace
old_laptop_to_tv = re.search(r'Laptop: \{.*?TV: \{.*?\},', content, re.DOTALL).group(0)
content = content.replace(old_laptop_to_tv, fixed_options)

# 3. Add Storage to HDD payload mapping
payload_mapping_new = """          if (subCategory === "Laptop" && nextAttrs.RAMValue && nextAttrs.RAMUnit) {
            nextAttrs.RAM = nextAttrs.RAMValue + nextAttrs.RAMUnit;
          }
          if (subCategory === "Laptop" && nextAttrs.Storage) {
            nextAttrs.HDD = nextAttrs.Storage;
            delete nextAttrs.Storage;
          }"""
content = content.replace('if (subCategory === "Laptop" && nextAttrs.RAMValue && nextAttrs.RAMUnit) {\n            nextAttrs.RAM = nextAttrs.RAMValue + nextAttrs.RAMUnit;\n          }', payload_mapping_new)

# 4. Replace all `<i data-lucide="..."></i>` with `<Icon name="..." cls="..." />`
content = re.sub(r'<i data-lucide="([^"]+)" className="([^"]+)"></i>', r'<Icon name="\1" cls="\2" />', content)
content = re.sub(r'<i data-lucide="([^"]+)" class="([^"]+)"></i>', r'<Icon name="\1" cls="\2" />', content)
content = re.sub(r'<i data-lucide={([^}]+)} className="([^"]+)"></i>', r'<Icon name={\1} cls="\2" />', content)

with open("XCommerce/src/main/resources/static/product-search.html", "w") as f:
    f.write(content)

print("Done")
