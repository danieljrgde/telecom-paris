'use strict';


const fs = require('fs');
const http = require('http');
const mimeTypes = require('mime-types');
const url = require('url');


const port = process.argv[2] || 8000;


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

    else if (endpoint === "/stop"){
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

    else if (endpoint === "/data"){
        fs.readFile("./storage.json", (err, data) => {
            if (err){
                res.writeHead(500, {'Content-Type': "application/json"});
                res.write(JSON.stringify(`Error while reading the file: ${err}.`));
                res.end();
            }
            else {
                res.writeHeader(200, {'Content-Type': "application/json"});
                res.write(data);
                res.end();
            }
        });
    }

    else if (endpoint === "/add"){
        if (!query['title'] || !query['color'] || !query['value']){
            res.writeHead(400, {'Content-Type': "application/json"});
            res.write(JSON.stringify("One of the parameters is missing."));
            res.end();
        }
        else {
            fs.readFile("./storage.json", (err, data) => {
                if (err){
                    res.writeHead(500, {'Content-Type': "application/json"});
                    res.write(JSON.stringify(`Error while accessing the storage file: ${err}.`));
                    res.end();
                }
                else {
                    var storage = JSON.parse(data);
                    var wedge = { title: query['title'], color: query['color'], value: parseFloat(query['value']) };
                    storage.push(wedge);
                    fs.writeFile("./storage.json", JSON.stringify(storage), (err) => {
                        if (err){
                            res.writeHead(500, {'Content-Type': "application/json"});
                            res.write(JSON.stringify(`Error while writing the storage file: ${err}.`));
                            res.end();
                        }
                        else {
                            res.writeHeader(200, {'Content-Type': "application/json"});
                            res.write(JSON.stringify(wedge));
                            res.end();
                        }
                    });
                }
            });
        }
    }

    else if (endpoint === "/remove"){
        if (!query['index']){
            res.writeHead(400, {'Content-Type': "application/json"});
            res.write(JSON.stringify("the parameter index wasn't specified."));
            res.end();
        }
        else {
            fs.readFile("./storage.json", (err, data) => {
                if (err){
                    res.writeHead(500, {'Content-Type': "application/json"});
                    res.write(JSON.stringify(`Error while reading the file: ${err}.`));
                    res.end();
                }
                else {
                    data = JSON.parse(data);
                    if (query['index'] > data.length-1 || query['index'] < 0){
                        res.writeHead(400, {'Content-Type': "application/json"});
                        res.write(JSON.stringify("The specified index is out of range."));
                        res.end();
                    }
                    else {
                        data.splice(query['index'], 1);
                        fs.writeFile("./storage.json", JSON.stringify(data), (err) => {
                            if (err){
                                res.writeHead(500, {'Content-Type': "application/json"});
                                res.write(JSON.stringify(`Error while writing the storage file: ${err}.`));
                                res.end();
                            }
                            else {
                                res.writeHeader(200, {'Content-Type': "application/json"});
                                res.write(JSON.stringify("Wedge removed successfully."));
                                res.end();
                            }
                        });
                    }
                }
            });
        }
    }

    else if (endpoint === "/clear"){
        fs.writeFile("./storage.json", JSON.stringify([{title: "empty", color: "red", value: 1}]), (err) => {
            if (err){
                res.writeHead(500, {'Content-Type': "application/json"});
                res.write(JSON.stringify(`Error while writing the storage file: ${err}.`));
                res.end();
            }
            else {
                fs.readFile("./storage.json", (err, data) => {
                    if (err){
                        res.writeHead(500, {'Content-Type': "application/json"});
                        res.write(JSON.stringify(`Error while reading the file: ${err}.`));
                        res.end();
                    }
                    else {
                        res.writeHeader(200, {'Content-Type': "application/json"});
                        res.write(data);
                        res.end();
                    }
                });
            }
        });
    }

    else if (endpoint === "/restore"){
        const slices = [
            { title: "foo", color: "red", value: 20 }, 
            { title: "bar", color: "ivory", value: 100 },
            { title: "foo2", color: "blue", value: 50 },
        ];
        fs.writeFile("./storage.json", JSON.stringify(slices), (err) => {
            if (err){
                res.writeHead(500, {'Content-Type': "application/json"});
                res.write(JSON.stringify(`Error while writing the storage file: ${err}.`));
                res.end();
            }
            else {
                fs.readFile("./storage.json", (err, data) => {
                    if (err){
                        res.writeHead(500, {'Content-Type': "application/json"});
                        res.write(JSON.stringify(`Error while reading the file: ${err}.`));
                        res.end();
                    }
                    else {
                        res.writeHeader(200, {'Content-Type': "application/json"});
                        res.write(data);
                        res.end();
                    }
                });
            }
        });
    }

    else if (endpoint === "/piech"){
        fs.readFile("./storage.json", 'utf8', (err, data) => {
            if (err){
                res.writeHead(500, {'Content-Type': "application/json"});
                res.write(JSON.stringify(`Error while reading the file: ${err}.`));
                res.end();
            }
            else {
                data = JSON.parse(data);

                var svg = `<svg width="500" height="500">`;
                var sumValues = data.reduce((total, currentWedge) => total + currentWedge['value'], 0);
                var startAngle, endAngle = 0;
                var x1, y1, x2, y2 = 0;

                data.forEach(wedge => {

                    var wedgeAngle = 360 * wedge['value']/sumValues;
                    var largeAngleFlag = wedgeAngle > 180 ? 1 : 0;

                    startAngle = endAngle;
                    endAngle += wedgeAngle;

                    x1 = parseFloat(200 + 180*Math.cos(Math.PI*startAngle/180));
                    y1 = parseFloat(200 + 180*Math.sin(Math.PI*startAngle/180));

                    x2 = parseFloat(200 + 180*Math.cos(Math.PI*endAngle/180));
                    y2 = parseFloat(200 + 180*Math.sin(Math.PI*endAngle/180));

                    var path = `<path d="M 200 200 L ${x1} ${y1} A 180 180 0 ${largeAngleFlag} 1 ${x2} ${y2} Z" fill="${wedge['color']}"/>`;
                    svg += path;

                });

                svg += `</svg>`;

                res.writeHeader(200, {'Content-Type': "image/svg+xml"});
                res.write(svg);
                res.end();
            }
        });
    }

    else {
        res.writeHead(404, {'Content-Type': "text/html"});
        res.write(htmlBase("404 Not Found"));
        res.end();
    }
    
});

server.listen(port);