const fs = require('fs');
const content = fs.readFileSync('XCommerce/src/main/resources/static/product-search.html', 'utf8');
console.log("Checking filterOptions...");
const match = content.match(/const filterOptions = useMemo\(\(\) => \(\{([\s\S]*?)\}\), \[\]\);/);
if (match) {
    console.log("Found filterOptions!");
    // Evaluate the object
    const objStr = "({" + match[1] + "})";
    try {
        const obj = eval(objStr);
        console.log("Laptop Brand length:", obj.Laptop.Brand.length);
        console.log("Laptop Storage length:", obj.Laptop.Storage.length);
    } catch(e) {
        console.error("Error evaluating filterOptions:", e);
    }
} else {
    console.log("filterOptions not found.");
}
