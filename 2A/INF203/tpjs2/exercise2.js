'use strict';


function send(){
    var textedit = document.getElementById('textedit');
    if (textedit.value){
        var xhr = new XMLHttpRequest();
        xhr.open('GET', `./chat.php?phrase=${textedit.value}`);
        xhr.send();
        textedit.value = "";
    }
}


function updateChat(){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "./chatlog.txt");
    xhr.onload = function(){
        var ta = document.getElementById('ta');
        ta.innerHTML = "";
        xhr.responseText.split("\n").reverse().filter(v => v).slice(0, 10).forEach(line => {
            var p = document.createElement('p');
            p.textContent = line;
            ta.appendChild(p);           
        });
    }
    xhr.send();
}


//------------------------------//
//--- ON WINDOW LOAD -----------//
//------------------------------//

window.onload = function(){
    var sendbut = document.getElementById('sendbut');
    sendbut.addEventListener('click', send);
    setInterval(updateChat, 1000);
}