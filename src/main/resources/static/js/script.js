// lazy-load

document.addEventListener('DOMContentLoaded', function() {
    function isInViewport(element) {
        const rect = element.getBoundingClientRect();
        return (
            rect.top >= 0 &&
            rect.left >= 0 &&
            rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
            rect.right <= (window.innerWidth || document.documentElement.clientWidth)
        );
    }

    function checkElements() {
        const elements = document.querySelectorAll('.content-row');
        
        elements.forEach((element) => {
            if (isInViewport(element)) {
                element.classList.add('visible');
            }
        });
    }

    checkElements();

    window.addEventListener('scroll', function() {
        checkElements();
    });
});