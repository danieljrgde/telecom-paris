'use strict';


function fibonaIt(n){
    var current = 0;
    var next = 1
    for (var i=0; i < n; i++){
        var tmp = current;
        current = next;
        next += tmp;
    }
    return current;
}


function fibRec(n){
    return [0, 1].includes(n) ? n : fibRec(n-1) + fibRec(n-2);
}


function fibArr(arr){
    var seq = [];
    for (var i=0; i < arr.length; i++)
        seq.push(fibRec(arr[i]));
    return seq;
}


function fibMap(arr){
    return arr.map(value => fibRec(value));
}


exports.fibonaIt = fibonaIt;
exports.fibRec = fibRec;
exports.fibArr = fibArr;
exports.fibMap = fibMap;