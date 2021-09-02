'use strict';


class Stud {

    constructor(lastName, firstName, id){
        this.lastName = lastName;
        this.firstName = firstName;
        this.id = id;
    }

    toString(){
        return `student: ${this.lastName}, ${this.firstName}, ${this.id}`;
    }

}


class ForeignStud extends Stud {

    constructor(lastName, firstName, id, nationality){
        super(lastName, firstName, id);
        this.nationality = nationality;
    }

    toString(){
        return `student: ${this.lastName}, ${this.firstName}, ${this.id}, ${this.nationality}`;
    }

}


exports.Stud = Stud;
exports.ForeignStud = ForeignStud;