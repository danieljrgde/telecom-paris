'use strict';


function wordCount(str){
    return str.split(" ").reduce((wordCount, currentWord) => {
        wordCount[currentWord] = (wordCount[currentWord] || 0) + 1;
        return wordCount;
    }, {});
}


function WordL(str) {

    this.str = str

    this.maxCountWord = function(){
        var ocurrences = wordCount(this.str);
        var word = this.getWords().reduce((maxWord, currentWord) => {
            return ocurrences[currentWord] > ocurrences[maxWord] ? currentWord : maxWord;
        }, Object.keys(ocurrences)[0]);
        return word;
    }

    this.minCountWord = function(){
        var ocurrences = wordCount(this.str);
        var word = this.getWords().reduce((minWord, currentWord) => {
            return ocurrences[currentWord] < ocurrences[minWord] ? currentWord : minWord;
        }, Object.keys(ocurrences)[0]);
        return word;
    }

    this.getWords = function(){
        return this.str.split(" ").sort().filter((v, i, a) => a.indexOf(v) === i);
    }

    this.getCount = function(word){
        return wordCount(this.str)[word] || 0;
    }

    this.applyWordFunc = function(f){
        return this.getWords().map(word => f(word));
    }

}


exports.wordCount = wordCount;
exports.WordL = WordL;