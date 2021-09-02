'use strict';


const fs = require('fs');
const http = require('http');
const mimeTypes = require('mime-types');
const url = require('url');


const port = process.argv[2] || 8000;
var visitors = []; 


function htmlBase(str){
    return `
        <!DOCTYPE html>
        <html>
            <head>
                <meta charset='utf-8'>
            </head>
            <body>
                ${str}
            </body>
        </html>
    `
}


const server = http.createServer((req, res) => {

    var endpoint = url.parse(req.url, true).pathname;
    var query = url.parse(req.url, true).query

    if (endpoint === "/"){
        res.writeHead(200, {'Content-Type': "text/html"});
        res.write(htmlBase("The server is working!"));
        res.end();
    }

    else if (endpoint === "/kill"){
        res.writeHead(200, {'Content-Type': "text/html"});
        res.write(htmlBase("The server will stop now."));
        res.end();
        setTimeout(() => process.exit(0), 2000);
    }

    else if (endpoint.startsWith("/files")){        
        var filePath = endpoint.substr(7);
        if (!fs.existsSync(filePath)){
            res.writeHead(404, {'Content-Type': "text/html"});
            res.write(htmlBase("404 Not Found"));
            res.end();
        }
        else if (filePath.includes("..")){
            res.writeHead(403, {'Content-Type': "text/html"});
            res.write(htmlBase("403 Forbidden"));
            res.end();
        }
        else {
            filePath = fs.statSync(filePath).isDirectory() ? filePath + "/index.html" : filePath;
            fs.readFile(filePath, (err, data) => {
                if (err){
                    res.writeHead(500, {'Content-Type': "text/html"});
                    res.write(htmlBase(`Error while reading the file: ${err}.`));
                    res.end();
                }
                else {
                    res.writeHeader(200, {'Content-type': mimeTypes.lookup(filePath) || "text/plain"});
                    res.write(data)
                    res.end();
                }
            });
        }
    }

    else if (endpoint === "/bonjour"){
        if (!query['visiteur']){
            res.writeHead(400, {'Content-Type': "text/html"});
            res.write(htmlBase("The request is missing the parameter 'visiteur'."));
            res.end();
        }
        else {
            res.writeHeader(200, {'Content-type': "text/html"});
            res.write(htmlBase(`bonjour ${query['visiteur']}`));
            res.end();
        }
    }

    else if (endpoint === "/coucou"){
        if (!query['name']){
            res.writeHead(400, {'Content-Type': "text/html"});
            res.write(htmlBase("The request is missing the parameter 'name'."));
            res.end();
        }
        else {
            var name = query['name'].replace(/</g, "&lt;").replace(/>/g, "&gt;");
            res.writeHead(200, {'Content-Type': "text/html"});
            res.write(htmlBase(`coucou ${name}, the following users have already visited this page: ${visitors.join(", ")}`));
            res.end();
            visitors.push(name);
        }        
    }

    else if (endpoint === "/clear"){
        visitors = [];
        res.writeHead(200, {'Content-Type': "text/html"});
        res.write(htmlBase("The memory was cleared."));
        res.end();
    }

    else {
        res.writeHead(404, {'Content-Type': "text/html"});
        res.write(htmlBase("404 Not Found"));
        res.end();
    }
    
});

server.listen(port);