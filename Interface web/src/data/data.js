// const dataFile = "./db/test/Solution.json"
const dataFileSolutions = "./db/solutions.json"
const dataFileInstances = "./db/instances.json"
const dataFileSolveurs = "./db/solveurs.json"

const _ = require("lodash");
const jsonDataSolutions = require(dataFileSolutions);
const jsonDataInstances = require(dataFileInstances);
const jsonDataSolveurs = require(dataFileSolveurs);

const cloneJsonDataSolutions = _.cloneDeep(jsonDataSolutions);
const cloneJsonDataInstances = _.cloneDeep(jsonDataInstances);
const cloneJsonDataSolveurs = _.cloneDeep(jsonDataSolveurs);

const waitAsync = (value) =>
    new Promise(resolve => setTimeout(resolve, Math.random() * 400 + 1, value));

function _loadAsync(_data) {
  return waitAsync(_.cloneDeep(_data));
}

function _saveAsync(data, _data) {
  Object.assign(_data, data);
  return waitAsync();
}

class Data {
    constructor() {
        this._dataSolutions = cloneJsonDataSolutions;
        this._dataInstances = cloneJsonDataInstances;
        this._dataSolveurs = cloneJsonDataSolveurs;
    }

    async getSolutionsAsync() {
        const data = await _loadAsync(this._dataSolutions);
        return _.cloneDeep(data.solutions);
    }

    async getSolutionAsync(index) {
        const data =  await _loadAsync(this._dataSolutions);
        const solutions = data.solutions;
        let solution = _.at(solutions, index);

        return _.cloneDeep(solution);
    }

    async getSolutionByNameAsync(name) {
        const data =  await _loadAsync(this._dataSolutions);
        const solutions = data.solutions;
        let solution = _.find(solutions, {
            nom: name
        });
        return _.cloneDeep(solution);
    }

    async getInstancesAsync() {
        const data =  await _loadAsync(this._dataInstances);

        return _.cloneDeep(data.instances);
    }

    async getInstanceByNameAsync(name) {
        const instances =  await this.getInstancesAsync();
        let instance = _.find(instances, {
            nom: name
        });
        return _.cloneDeep(instance);
    }

    async getSolveursAsync() {
        const data =  await _loadAsync(this._dataSolveurs);

        return _.cloneDeep(data.solveurs);
    }

    async getSolveurByNameAsync(name) {
        const solveurs =  await this.getSolveursAsync();
        let solveur = _.find(solveurs, {
            nom: name
        });
        return _.cloneDeep(solveur);
    }
}

module.exports = Data;