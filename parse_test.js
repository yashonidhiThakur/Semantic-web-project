const fs = require('fs');
const { JSDOM } = require('jsdom');

const content = fs.readFileSync('/tmp/test.html', 'utf8');

const match = content.match(/const filterOptions = useMemo\(\(\) => \(\{([\s\S]*?)\}\), \[\]\);/);
if (match) {
    const objStr = "({" + match[1] + "})";
    try {
        const obj = eval(objStr);
        console.log("filterOptions.Laptop.Brand:", obj.Laptop.Brand);
    } catch(e) {
        console.error("Eval error", e);
    }
} else {
    console.log("No filterOptions found in HTML string!");
}
