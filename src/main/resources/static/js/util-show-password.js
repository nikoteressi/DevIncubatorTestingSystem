const showPasswordButton = document.querySelector('.password-control');

showPasswordButton.addEventListener('click', (event) => {
    const passwordInput = document.getElementsByName('password').item(0);
    const target = event.target;
    const isContainOpenClass = target.classList.contains('open');
    target.classList.toggle('open');
    if (isContainOpenClass) {
        passwordInput.setAttribute('type', 'password');
    } else {
        passwordInput.setAttribute('type', 'text');
    }
});


