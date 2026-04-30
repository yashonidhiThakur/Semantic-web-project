import re

with open("XCommerce/src/main/resources/static/product-search.html", "r") as f:
    content = f.read()

# The file has this exact string twice:
#        const Icon = ({ name, cls }) => {
#            const spanRef = useRef(null);
#            useEffect(() => {
#                if (spanRef.current) {
#                    spanRef.current.innerHTML = `<Icon name="${name}" cls="${cls || ''}" />`;
#                    lucide.createIcons({ root: spanRef.current });
#                }
#            }, [name, cls]);
#            return <span ref={spanRef} className="inline-flex items-center justify-center" />;
#        };

target = """        const Icon = ({ name, cls }) => {
            const spanRef = useRef(null);
            useEffect(() => {
                if (spanRef.current) {
                    spanRef.current.innerHTML = `<i data-lucide="${name}" class="${cls || ''}"></i>`;
                    lucide.createIcons({ root: spanRef.current });
                }
            }, [name, cls]);
            return <span ref={spanRef} className="inline-flex items-center justify-center" />;
        };"""

# Replace all occurrences of Icon definition with just ONE.
# Actually let's just find the first `const categories = useMemo(() => ["Electronic Devices", "Furniture"], []);`
# And remove anything between `// --- LOGIC PRESERVED FROM ORIGINAL ---` and that.
# Then insert exactly one Icon definition.

start_marker = "// --- LOGIC PRESERVED FROM ORIGINAL ---"
end_marker = 'const categories = useMemo(() => ["Electronic Devices", "Furniture"], []);'

idx1 = content.find(start_marker)
idx2 = content.find(end_marker)

if idx1 != -1 and idx2 != -1:
    new_block = start_marker + "\\n\\n" + target + "\\n\\n        " + end_marker
    content = content[:idx1] + new_block + content[idx2 + len(end_marker):]

with open("XCommerce/src/main/resources/static/product-search.html", "w") as f:
    f.write(content)

print("Done")
