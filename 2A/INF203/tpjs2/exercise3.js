'use strict';


function loadSlides(callback){
    var xhr = new XMLHttpRequest();
	xhr.open('GET', "slides.json");
	xhr.onload = function(){
        var slides = JSON.parse(xhr.responseText)['slides'];
        callback(slides);
    }
	xhr.send();
}


function displaySlide(url){
	var TOP = document.getElementById('TOP');
    var iframe = document.createElement('iframe');
    TOP.innerHTML = "";
    iframe.src = url;
    TOP.appendChild(iframe);
}


function playSlideshow(){
    loadSlides(slides => {
        slides.forEach(slide => {
            setTimeout(displaySlide, slide.time*1000, slide.url);
        });
    });
}


//------------------------------//
//--- ON WINDOW LOAD -----------//
//------------------------------//

window.onload = function(){
    var but_play = document.getElementById('but_play');
    but_play.addEventListener('click', playSlideshow);
}