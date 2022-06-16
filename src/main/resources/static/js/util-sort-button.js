function reverseTests() {
    document.querySelector('#sortTestButton').classList.toggle('reverse')
    const reversedTests = Array.from(resultTableBody.querySelectorAll('.test')).reverse();
    let index = 0;
    resultTableBody.innerHTML = '';
    reversedTests.forEach(test => {
        index = ++index;
        test.childNodes.item(1).childNodes.item(1).textContent = index;
        resultTableBody.append(test);
    });
    index = 0;
}

dataContainer.addEventListener('click', (event) => {
    const {target} = event;
    if (target.closest('#sortTestButton')) {
        isReverseTest = !isReverseTest;
        reverseTests();
    }
})