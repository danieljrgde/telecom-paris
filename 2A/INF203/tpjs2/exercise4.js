'use strict';


//------------------------------//
//--- CLASSES ------------------//
//------------------------------//

class Timer {
    
    constructor(){
        this.paused = false;
        this.pauses = [];
        this.releases = [];
    }

    start(){
        this.paused = false;
        this.reset();
        this.release();
    }

    reset(){
        this.pauses = [];
        this.releases = [];
    }

    pause(){
        this.paused = true;
        this.pauses.push(new Date());
    }

    release(){
        this.paused = false;
        this.releases.push(new Date());
    }

    setTimePassed(timePassed){
        var now = new Date();
        this.releases = [new Date(now.getTime() - timePassed)];
        this.pauses = [now];
        this.paused = true;
    }

    timePassed(){
        return this.releases.reduce( (timePassed, currentRelease, idx) => {
            var release = currentRelease.getTime();
            var pause = (this.pauses[idx] || new Date()).getTime();
            timePassed += (pause - release)/1000;
            return timePassed;
        }, 0);
    }

}


//------------------------------//
//--- GLOBAL VARIABLES ---------//
//------------------------------//

var currentSlide = null;
var timer = new Timer();


//------------------------------//
//--- FUNCTIONS ----------------//
//------------------------------//

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
        timer.start();
        currentSlide = 0;
        displaySlide(slides[currentSlide].url);
        var intervalId = setInterval(() => {
            if (currentSlide+1 < slides.length){
                if (timer.timePassed() >= slides[currentSlide+1].time){
                    nextSlide(false);
                }
            }
            else {
                timer.reset();
                clearInterval(intervalId);
            }
        }, 100);
    });
}


function pauseRelease(){
    timer.paused ? timer.release() : timer.pause();
    var PAUSE = document.getElementById('PAUSE');
    PAUSE.innerText = timer.paused ? "Continue" : "Pause";
}


function nextSlide(forced){
    loadSlides(slides => {
        currentSlide = currentSlide === null ? 0 : currentSlide === slides.length-1 ? currentSlide : currentSlide+1;
        if (forced)
            timer.setTimePassed(slides[currentSlide].time*1000);
        displaySlide(slides[currentSlide].url);
    });
}


function previousSlide(){
    loadSlides(slides => {
        currentSlide = currentSlide === null ? 0 : currentSlide === 0 ? currentSlide : currentSlide-1;
        timer.setTimePassed(slides[currentSlide].time*1000);
        displaySlide(slides[currentSlide].url);
    });
}


//------------------------------//
//--- ON WINDOW LOAD -----------//
//------------------------------//

window.onload = function(){
    var but_play = document.getElementById('but_play');
    var PAUSE = document.getElementById('PAUSE');
    var suivant = document.getElementById('suivant');
    var but_prev = document.getElementById('but_prev');

    but_play.addEventListener('click', playSlideshow);
    PAUSE.addEventListener('click', pauseRelease);
    suivant.addEventListener('click', () => nextSlide(true));
    but_prev.addEventListener('click', previousSlide);
}