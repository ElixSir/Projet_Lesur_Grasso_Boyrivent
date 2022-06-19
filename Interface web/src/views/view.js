/*
const fs = require('fs')
const readFileSync = require('fs').readFileSync;
const readdirSync = require('fs').readdirSync;
const { applyPatch } = require('fast-json-patch');      
*/
class View {
    
    constructor(data) {
        this.data = data;
    }

    configure(app) {
        const data = this.data;

        /* routes */
        app.get('/', (req, res) => {
            res.redirect('/home');
        });

        this.configureRoutes(app);
        
    }

    configureRoutes(app) {

        // home
        app.get('/home', (req, res) => this.home(res) );

        // solutions
        app.get('/solutions', (req, res) => this.solution(res) );

        // solution :name
        app.get('/solution/:name', (req, res) => this.solution(res, req.params.name) );

        // instances 
        app.get('/instances', (req, res) => this.instance(res) );

        // instance :name
        app.get('/instance/:name', (req, res) => this.instance(res, req.params.name) );
    }

    async home(res) {
        let solveurs = await this.data.getSolveursAsync();

        this.render(res,"home", {solveurs: solveurs});
    }

    async solution(res, name = null) {
        if(null == name) {
            let solutions = await this.data.getSolutionsAsync();

            this.render(res, "solutions", {solutions: solutions});
            return;
        }

        let solution = await this.data.getSolutionByNameAsync(name);

        this.render(res, "solution", {solution: solution});
    }

    async instance(res, name = null) {
        if(null == name) {
            let instances = await this.data.getInstancesAsync();

            this.render(res, "instances", {instances: instances});
            return;
        }

        let instance = await this.data.getInstanceByNameAsync(name);

        this.render(res, "instance", {instance: instance});
    }

    render(res, template, param) {
        /*
        if(! fs.existsSync( __dirname + "/templates/" + template + ".ejs")) {
            template = ""
        }
        */
        
        let obj = { 
            template: '../' + template,
            param: param
        }
        res.render('main/index', obj);
    }
}

module.exports = View;
