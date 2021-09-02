'use strict';


function show(){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "./../../data");
    xhr.onload = function(){
        document.getElementById('MAINSHOW').innerHTML = xhr.response;
    }
    xhr.send();
}


function showAdd(){
    var add = document.getElementById('add');
    var rem = document.getElementById('rem');
    add.style.visibility = 'visible';
    rem.style.visibility = 'hidden';
}


function add(){
    var title = encodeURIComponent(document.getElementById('titleTF').value) || null;
    var color = encodeURIComponent(document.getElementById('colorTF').value) || null;
    var value = encodeURIComponent(document.getElementById('valueTF').value) || null;

    if (!title || !color || !value)
        document.getElementById('MAINSHOW').innerHTML = "Please enter valid inputs.";
    else {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', `./../../add?title=${title}&color=${color}&value=${value}`);
        xhr.onload = function(){
            document.getElementById('MAINSHOW').innerHTML = xhr.response;
        }
        xhr.send();
    }
}


function showRemove(){
    var add = document.getElementById('add');
    var rem = document.getElementById('rem');
    add.style.visibility = 'hidden';
    rem.style.visibility = 'visible';
}


function remove(){
    var index = encodeURIComponent(document.getElementById('indexTF').value) || null;
    var xhr = new XMLHttpRequest();
    xhr.open('GET', `./../../remove?index=${index}`);
    xhr.onload = function(){
        document.getElementById('MAINSHOW').innerHTML = xhr.response;
    }
    xhr.send();
}


function restore(){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "./../../restore");
    xhr.onload = function(){
        document.getElementById('MAINSHOW').innerHTML = xhr.response;
    }
    xhr.send();
}


function clear(){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "./../../clear");
    xhr.onload = function(){
        document.getElementById('MAINSHOW').innerHTML = xhr.response;
    }
    xhr.send();
}


function pie(){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "./../../piech");
    xhr.onload = function(){
        document.getElementById('MAINSHOW').innerHTML = xhr.response;
    }
    xhr.send();
}


function localpie(){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "./../../data");
    xhr.onload = function(){
        var wedges = JSON.parse(xhr.response);

        var svg = document.createElementNS("http://www.w3.org/2000/svg", 'svg');
        svg.setAttribute('width', 500);
        svg.setAttribute('height', 500);

        var sumValues = wedges.reduce((total, currentWedge) => total + currentWedge['value'], 0);
        var startAngle, endAngle = 0;
        var x1, y1, x2, y2 = 0;

        wedges.forEach(wedge => {

            var wedgeAngle = 360 * wedge['value']/sumValues;
            var largeAngleFlag = wedgeAngle > 180 ? 1 : 0;

            startAngle = endAngle;
            endAngle += wedgeAngle;

            x1 = parseFloat(200 + 180*Math.cos(Math.PI*startAngle/180));
            y1 = parseFloat(200 + 180*Math.sin(Math.PI*startAngle/180));

            x2 = parseFloat(200 + 180*Math.cos(Math.PI*endAngle/180));
            y2 = parseFloat(200 + 180*Math.sin(Math.PI*endAngle/180));

            var path = document.createElementNS("http://www.w3.org/2000/svg", 'path');
            path.setAttribute('d', `M 200 200 L ${x1} ${y1} A 180 180 0 ${largeAngleFlag} 1 ${x2} ${y2} Z`);
            path.setAttribute('fill', wedge['color'])
            svg.appendChild(path);

        });

        document.getElementById('MAINSHOW').innerHTML = "";
        document.getElementById('MAINSHOW').appendChild(svg);
    }
    xhr.send();
}


//------------------------------//
//--- ON WINDOW LOAD -----------//
//------------------------------//

window.onload = function(){
    var BUTSHO = document.getElementById('BUTSHO');
    var BUTADD = document.getElementById('BUTADD');
    var DOADD = document.getElementById('DOADD');
    var REMOVE = document.getElementById('REMOVE');
    var RESTORE = document.getElementById('RESTORE');
    var DOREM = document.getElementById('DOREM');
    var CLEAR = document.getElementById('CLEAR');
    var BUTPIE = document.getElementById('BUTPIE');
    var BUTLPI = document.getElementById('BUTLPI');

    BUTSHO.addEventListener('click', show);
    BUTADD.addEventListener('click', showAdd);
    DOADD.addEventListener('click', add);
    REMOVE.addEventListener('click', showRemove);
    RESTORE.addEventListener('click', restore)
    DOREM.addEventListener('click', remove);
    CLEAR.addEventListener('click', clear);
    BUTPIE.addEventListener('click', pie);
    BUTLPI.addEventListener('click', localpie);
}