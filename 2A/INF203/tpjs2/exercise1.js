'use strict';


function loadDoc(){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "./text.txt");
    xhr.onload = function(){
        var ta = document.getElementById('ta');
        ta.innerText = xhr.responseText;
    }
    xhr.send();
}


function loadDoc2(){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "./text.txt");
    xhr.onload = function(){
        xhr.responseText.split('<br/>').forEach(line => { 
            var ta2 = document.getElementById('ta2');
            var p = document.createElement('p');
            p.style.color = '#' + ("000000" + Math.random().toString(16).slice(2, 8).toUpperCase()).slice(-6);
            p.innerText = line;
            ta2.appendChild(p);
        });
    }
    xhr.send();
}


//------------------------------//
//--- ON WINDOW LOAD -----------//
//------------------------------//

window.onload = function(){
    var button1 = document.getElementById('button1');
    button1.addEventListener('click', loadDoc);
    var button2 = document.getElementById('button2');
    button2.addEventListener('click', loadDoc2);
}
