const resultTableBody = document.getElementById('resultTableBody');
const dataContainer = document.getElementById('dataContainer');
const burgerMenu = document.querySelector('.burger');
let currentUserId = null;
let isReverseTest = false;
const baseUrl = window.location.origin;

window.onload = getUserStatistic();

function setCurrentUserid() {
    currentUserId = document.querySelector(".grid__head").id;
}

burgerMenu.addEventListener('click', ({target}) => {
    if (target.closest('.burger')) {
        document.getElementById('navbarNavAltMarkup').classList.toggle('collapse');
        document.getElementById('navbarNavAltMarkup').classList.toggle('show');
    }
});

function updateResult(data) {
    if (!data) {
        return
    }
    console.log(isReverseTest)

    const reversedData = isReverseTest ? [...data].reverse() : [...data];
    dataContainer.classList.add('active');
    resultTableBody.innerHTML = `
    ${reversedData.map(({testName, count, avgProc}, index) => {
        return `
      <div class="container test" data-id="${index}">
        <div class="row result-row" data-bs-toggle="collapse" href='#test${index}'>
          <div class="col-1 text-start d-none d-md-block" id="index">${index + 1}</div>
          <div class="col-md-8 col-sm-7 text-start">${testName}</div>
          <div class="col-md-3 col-sm-5 text-center d-flex justify-content-between">
              <div style="width: 40px;">${count}</div>
              <div style="width: 100px;">${avgProc}%</div>
          </div> 
        </div>
      </div>
      `
    }).join('')}
  `
}

async function getUserStatistic() {
    dataContainer.classList.remove('active');
    try {
        const url = new URL(baseUrl + "/user/get-personal-statistics");
        setCurrentUserid();
        const params = {userId: currentUserId};
        url.search = new URLSearchParams(params).toString();
        const response = await fetch(url.toString());
        const result = await response.json();
        updateResult(result);
    } catch (err) {
        console.error(err)
    }
}