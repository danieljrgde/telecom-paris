'use strict';


const fs = require('fs');
const ex3 = require('./exercise3');
const Stud = ex3.Stud;
const ForeignStud = ex3.ForeignStud;


class Prom {

    students = []

    add(student){
        this.students.push(student);
    }

    size(){
        return this.students.length;
    }

    get(i){
        return this.students[i];
    }

    print(){
        var str = this.students.reduce((str, currentStudent) => {
            str += currentStudent.toString() + "\n";
            return str;
        }, "");
        console.log(str);
        return str;
    }

    write(){
        return JSON.stringify(this);
    }

    read(str){
        var prom = JSON.parse(str);
        prom.forEach(student => {
            student = student['nationality'] ? new ForeignStud(...Object.values(student)) : new Stud(...Object.values(student));
            this.add(student);
        });
    }

    readF(fileName){
        fs.writeFile(fileName, this.write(), (err) => { if (err) throw err });
    }

    saveF(fileName){
        fs.readFile(fileName, 'utf8', (err, data) => {
            if (err) throw err;
            this.read(data);
        });
    }

}


exports.Prom = Prom;