const express = require('express');
const bodyParser = require('body-parser');
const packageJson = require('../package.json');

class App {
    constructor(view) {
        const app = express();

        app.set("views","./src/views/templates");
        app.set("view engine","ejs");

        app.use(bodyParser.urlencoded({
            extended: true
        }));
        app.use(bodyParser.json());

        var middlewareHttp = function (request, response, next) {
            response.setHeader('Api-version', packageJson.version);
            // Accept
            response.setHeader('Accept', '*/*');
            /* Access */
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:8000")
            response.setHeader("Access-Control-Allow-Methods", "GET")
            response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Content-Type")


            console.log(`${request.method} ${request.originalUrl}`);
            if (request.body && Object.keys(request.body).length >0) {
                console.log(`request.body ${JSON.stringify(request.body)}`);
            }
            next();
        };
        app.use(middlewareHttp);

        view.configure(app);

        /* routes static */
        app.use('/static', express.static(__dirname + '/assets'));

        // TODO : Modifier le json
        app.get('/api/version', function (request, response) {
            response.json({
                version: packageJson.version
            });
        });

        var middlewareHttpError = function (request, response) {
            response.status(404).json({
                key: 'not.found'
            });
        };
        
        app.use(middlewareHttpError);

        // eslint-disable-next-line no-unused-vars
        app.use(function (error, request, response, next) {
            console.error(error.stack);
            response.status(500).json({
                key: 'server.error'
            });
        });
        this.app=app;
    }
}

module.exports = App;
