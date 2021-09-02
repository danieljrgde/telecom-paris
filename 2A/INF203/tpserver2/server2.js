'use strict';


const bodyparser = require('body-parser');
const express = require('express');
const fs = require('fs');
const morgan = require('morgan');


const app = express();
app.use(morgan('tiny'));
app.use(bodyparser.json());


const PORT = process.argv[2] || 8000;
const DBPATH = "./db.json";


var db;
fs.readFile(DBPATH, (err, data) => db = err ? [] : JSON.parse(data));


app.get("/", (req, res) => {
    res.writeHead(200, {'Content-Type': "text/plain"});
    res.write("Hi");
    res.end();
});

app.get("/exit", (req, res) => {
    res.writeHead(200, {'Content-Type': "text/plain"});
    res.write("The server will stop now.");
    res.end();
    setTimeout(() => process.exit(0), 2000);
});

app.get("/reload", (req, res) => {
    fs.readFile(DBPATH, (err, data) => {
        if (err){
            res.writeHead(500, {'Content-Type': "text/plain"});
            res.write(`Error while reading the file: ${err}.`);
            res.end();
        }
        else {
            db = JSON.parse(data); 
            res.writeHeader(200, {'Content-type': "text/plain"});
            res.write("db.json reloaded");
            res.end();
        }
    });
});

app.get("/countpapers", (req, res) => {
    res.writeHeader(200, {'Content-type': "text/plain"});
    res.write(db.length.toString());
    res.end();
});

app.get("/byauthor/:author", (req, res) => {
    var matches = db.filter(paper => {
        var authors = paper['authors'].map(author => author.toLowerCase());
        var author = req.params['author'].toLowerCase();
        return authors.find(name => name.includes(author));
    });
    res.writeHeader(200, {'Content-type': "text/plain"});
    res.write(matches.length.toString());
    res.end();
});

app.get("/papersfrom/:author", (req, res) => {
    var matches = db.filter(paper => {
        var authors = paper['authors'].map(author => author.toLowerCase());
        var author = req.params['author'].toLowerCase();
        return authors.find(name => name.includes(author));
    });
    res.writeHeader(200, {'Content-type': "application/json"});
    res.write(JSON.stringify(matches));
    res.end();
});

app.get("/ttlist/:author", (req, res) => {
    var titles = db.reduce((titles, currentPaper) => {
        var authors = currentPaper['authors'].map(author => author.toLowerCase());
        var author = req.params['author'].toLowerCase();
        if (authors.find(name => name.includes(author)))
            titles.push(currentPaper['title']);
        return titles;      
    }, []);
    res.writeHeader(200, {'Content-type': "application/json"});
    res.write(JSON.stringify(titles));
    res.end();
});

app.get("/ref/:key", (req, res) => {
    var match = db.find(paper => paper['key'] === req.params['key']);
    if (match) {
        res.writeHeader(200, {'Content-type': "application/json"});
        res.write(JSON.stringify(match));
        res.end();
    }
    else {
        res.writeHeader(404, {'Content-type': "application/json"});
        res.write(JSON.stringify({err: "Paper not found."}));
        res.end();
    }
});

app.delete("/ref/:key", (req, res) => {
    var match = db.find(paper => paper['key'] === req.params['key']);
    if (match) {
        db = db.filter(paper => paper['key'] !== req.params['key']);
        res.writeHeader(200, {'Content-type': "text/plain"});
        res.write("Paper deleted.");
        res.end();
    }
    else {
        res.writeHeader(404, {'Content-type': "text/plain"});
        res.write("Paper not found.");
        res.end();
    }
});

app.post("/ref", (req, res) => {
    if (req.body){
        db.push(req.body);
        res.writeHeader(200, {'Content-type': "application/json"});
        res.write(JSON.stringify(req.body));
        res.end();
    }
    else {
        res.writeHeader(400, {'Content-type': "application/json"});
        res.write(JSON.stringify({err: "Bad request."}));
        res.end();
    }
});

app.put("/ref/:key", (req, res) => {
    var match = db.find(paper => paper['key'] === req.params['key']);
    if (match) {
        var newPaper;
        db = db.map(paper => {
            if (paper['key'] === req.params['key']){
                newPaper = {...paper, ...req.body};
                return newPaper;
            }
            else 
                return paper;
        });
        res.writeHeader(200, {'Content-type': "application/json"});
        res.write(JSON.stringify(newPaper));
        res.end();
    }
    else {
        res.writeHeader(404, {'Content-type': "application/json"});
        res.write(JSON.stringify({err: "Paper not found."}));
        res.end();
    }
});



app.listen(PORT);