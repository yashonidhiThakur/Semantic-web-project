const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch();
  const page = await browser.newPage();
  await page.goto('http://localhost:8080/product-search.html', {waitUntil: 'networkidle0'});
  
  // Select Category
  await page.select('select.input', 'Electronic Devices');
  await page.waitForTimeout(500);
  
  // Click Laptop
  const buttons = await page.$$('button');
  for (const btn of buttons) {
      const text = await page.evaluate(el => el.textContent, btn);
      if (text.includes('Laptop')) {
          await btn.click();
          break;
      }
  }
  await page.waitForTimeout(500);
  
  // Select Brand
  await page.select('select.input[class*="h-10"]', 'Brand');
  
  // Click +
  const addBtn = await page.$('button[class*="bg-slate-900"]');
  if (addBtn) await addBtn.click();
  await page.waitForTimeout(500);
  
  // Inspect options in the new select
  const options = await page.evaluate(() => {
      const selects = document.querySelectorAll('select.input');
      const lastSelect = selects[selects.length - 1];
      if (!lastSelect) return null;
      return Array.from(lastSelect.options).map(o => o.value);
  });
  console.log("Options in Brand select:", options);
  
  await browser.close();
})();
