window.onload = function() {
    const slidesContainer = document.querySelector('.slides-container');
    const slides = document.querySelectorAll('.slide');
    const prevButton = document.querySelector('.prev');
    const nextButton = document.querySelector('.next');

    let currentIndex = 0;

    function updateSlidePosition() {
        const offset = -currentIndex * 100;
        slidesContainer.style.transform = `translateX(${offset}%)`;
    }

    function nextSlide() {
        currentIndex = (currentIndex + 1) % slides.length;
        updateSlidePosition();
    }

    function prevSlide() {
        currentIndex = (currentIndex - 1 + slides.length) % slides.length;
        updateSlidePosition();
    }

    nextButton.addEventListener('click', nextSlide);
    prevButton.addEventListener('click', prevSlide);

    setInterval(nextSlide, 5000);
};