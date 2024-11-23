window.onload = function () {
    // Hero Slider
    const heroContainer = document.querySelector('#hero-slider .slides-container');
    const heroSlides = document.querySelectorAll('#hero-slider .slide');
    const heroPrevBtn = document.querySelector('#hero-slider .prev');
    const heroNextBtn = document.querySelector('#hero-slider .next');

    let heroCurrentIndex = 0;

    function updateHeroSlidePosition() {
        const offset = -heroCurrentIndex * 100;
        heroContainer.style.transform = `translateX(${offset}%)`;
    }

    function nextHeroSlide() {
        if (heroSlides.length === 0) return;
        heroCurrentIndex = (heroCurrentIndex + 1) % heroSlides.length;
        updateHeroSlidePosition();
    }

    function prevHeroSlide() {
        if (heroSlides.length === 0) return;
        heroCurrentIndex = (heroCurrentIndex - 1 + heroSlides.length) % heroSlides.length;
        updateHeroSlidePosition();
    }

    heroNextBtn.addEventListener('click', nextHeroSlide);
    heroPrevBtn.addEventListener('click', prevHeroSlide);


    // Testimonial Slider
    const testimonialWrapper = document.querySelector('#testimonial-slider .slides-review');
    const testimonialSlides = document.querySelectorAll('#testimonial-slider .slide-review');
    const testimonialPrevBtn = document.querySelector('#testimonial-slider .prev-view');
    const testimonialNextBtn = document.querySelector('#testimonial-slider .next-view');

    const slidesToShow = 3; // Số lượng slide hiển thị
    const totalTestimonialSlides = testimonialSlides.length;
    let testimonialCurrentIndex = 0;

    function updateTestimonialPosition() {
        testimonialWrapper.style.transform = `translateX(-${(testimonialCurrentIndex * 100) / slidesToShow}%)`;
    }

    function nextTestimonialSlide() {
        if (totalTestimonialSlides <= slidesToShow) return;
        testimonialCurrentIndex = (testimonialCurrentIndex + 1) % Math.ceil(totalTestimonialSlides / slidesToShow);
        updateTestimonialPosition();
    }

    function prevTestimonialSlide() {
        if (totalTestimonialSlides <= slidesToShow) return;
        testimonialCurrentIndex = (testimonialCurrentIndex - 1 + Math.ceil(totalTestimonialSlides / slidesToShow)) %
            Math.ceil(totalTestimonialSlides / slidesToShow);
        updateTestimonialPosition();
    }

    testimonialNextBtn.addEventListener('click', nextTestimonialSlide);
    testimonialPrevBtn.addEventListener('click', prevTestimonialSlide);
};