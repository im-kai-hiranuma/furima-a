const countPrice = () => {
  const priceInput = document.getElementById('item-price');
  const addTaxPrice = document.getElementById('add-tax-price');
  const profit = document.getElementById('profit');

  if (!priceInput || !addTaxPrice || !profit) return;

  const priceValue = priceInput.value.trim();

  // 数値判定
  if (priceValue !== '' && !isNaN(priceValue)) {
    const price = parseInt(priceValue, 10);

    if (price >= 300 && price <= 9999999) {
      const tax = Math.floor(price * 0.1);
      const profitValue = price - tax;

      addTaxPrice.textContent = tax.toLocaleString();
      profit.textContent = profitValue.toLocaleString();
      return;
    }
  }

  // 条件外の場合は0を表示
  addTaxPrice.textContent = '0';
  profit.textContent = '0';
};

// ページ読み込み完了時と入力時の両方でイベントを設定
window.addEventListener('DOMContentLoaded', () => {
  const priceInput = document.getElementById('item-price');
  if (priceInput) {
    priceInput.addEventListener('input', countPrice);
    // 初期値が入っている場合のために初回呼び出し
    countPrice();
  }
});