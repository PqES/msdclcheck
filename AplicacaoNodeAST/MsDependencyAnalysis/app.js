const espree = require('espree');
const estraverse = require('estraverse');
const esquery = require('esquery');
const escodegen = require('escodegen');
const fs = require('fs');

/**
 * 
 * @param {*} element 
 */
Array.prototype.contains = function(element) {
    return this.indexOf(element) > -1;
};

/**
 * 
 * @param {*} find 
 * @param {*} replace 
 */
String.prototype.replaceAll = function(find, replace) {
    return this.replace(new RegExp(find, 'g'), replace);
}

/**
 * 
 * @param {*} code 
 * @param {*} filename 
 * 
 */
function writeCode(code, filename) {
    fs.writeFileSync(filename, '', err => {
        if (err) return console.log(err);
    });
    fs.writeFileSync(filename, code, err => {
        if (err) return console.log(err)
            // console.log('[INFO] \'' + filename + '\' foi refatorado e salvo no arquivo', newFilename);
    })
}

//var fileAnalysis = '/home/carlos/workspaces/microservices/toyExample/MsVenda/controllers/authenticationController.js';

//var a = require('/home/carlos/workspaces/microservices/toyExample/MsVenda/controllers/authenticationController.js');
// console.log("a");
//a.authenticate({ params });

//const file = '/home/carlos/workspaces/microservices/toyExample/MsDependencyAnalysis/requestUse_01.js';
const file = '/home/carlos/workspaces/microservices/toyExample/MsVenda/controllers/authenticationController.js';
//const file = '/home/carlos/workspaces/microservices/toyExample/MsVenda/controllers/productController.js';
//const file = '/home/carlos/workspaces/microservices/toyExample/MsVenda/controllers/customerController.js';
//const file = '/home/carlos/workspaces/microservices/toyExample/MsVenda/controllers/saleController.js';

const code = fs.readFileSync(file, 'utf-8').replaceAll('let\\s', 'var ');
console.log(code);
const ast = espree.parse(code);

//console.log(require('/home/carlos/workspaces/microservices/toyExample/MsVenda/config/microservices.json'));
// const astConfigJson = espree.parse('/home/carlos/workspaces/microservices/toyExample/MsVenda/config/microservices.json');
// console.log(astConfigJson);

var varRequireFromProject = {};
var httpRequestLibrarys = ['http', 'request'];
var requireVarName = [];
var requestMs = [];
var requireNodesParent = [];
var actualFunction = [];
var actualFunctionIdentifier;
var requestDependencyFromFunctions = {};
var requestMsFromFunctions = {};
requestMsFromFunctions['global'] = [];
requestDependencyFromFunctions['global'] = [];
var requestMsFromExports = {};
var requestMsFromExportsInv = {};
// console.log(esquery.query(ast, '[name="configFile"],[type="Identifier"]'));

/**
 * @returns índice inicial da função 
 */
function getStartFunction() {
    let functions = [];
    let lastFunction = actualFunction.pop();
    while (lastFunction !== undefined) {
        functions.push(lastFunction);
        lastFunction = actualFunction.pop();
    }
    lastFunction = functions.pop();
    let startFunction = -1;
    if (lastFunction !== undefined) {
        startFunction = lastFunction.start;
    }
    while (lastFunction !== undefined) {
        actualFunction.push(lastFunction);
        lastFunction = functions.pop();
    }
    return startFunction;
}

function getFunctionExportName() {
    return requestMsFromExportsInv['f' + getStartFunction()];
}

/**
 * 
 * @param {*} ast ast onde variável foi declarada
 * @param {*} identifierName identificador da variável
 * @param {*} start índice onde deseja analisar o valor da string
 * 
 * @returns valor de uma string contido em uma variável em determinado ponto do código
 */
function getValueStringIdentifier(ast, identifierName, start, parentStart) {
    if (identifierIsParam(identifierName)) {
        return '';
    }
    let startFunction = getStartFunction();
    let nextNode
    const newAST = estraverse.replace(ast, {
        enter: function(node, parent) {
            let retNode = node;
            if (node.start >= startFunction) {
                let actualNode = node;
                while (actualNode.type === 'CallExpression' &&
                    actualNode.callee.type === 'MemberExpression') {
                    actualNode = actualNode.callee;
                }
                if (actualNode.type === 'MemberExpression') {
                    while (actualNode.type === 'MemberExpression') {
                        actualNode = actualNode.object;
                    }
                    if (identifierIsParam(actualNode.name)) {
                        retNode = espree.parse("''");
                    }
                }
            }
            if (node.start === parentStart) {
                retNode = espree.parse('module.exports.SECRET_VAR_STRING_VALUE = ' + identifierName + ';');
            }
            return retNode;
        },
        leave: function(node, parent) {}
    });
    let codeResult = escodegen.generate(newAST);
    codeResult += '\nmodule.exports.' + getFunctionExportName() + '();\n';
    let fileTemp = file.substring(0, file.length - 3) + '_temp.js';
    writeCode(codeResult, fileTemp);
    // console.log('AGORA');
    // console.log(require(fileTemp).SECRET_VAR_STRING_VALUE);
    return require(fileTemp).SECRET_VAR_STRING_VALUE;
    // value = '';
    // const astString = estraverse.traverse(ast, {
    //     enter: function(node, parent) {
    //         if (node.type === 'Identifier' &&
    //             node.name === identifierName) {
    //             if (node.start >= start) {
    //                 // nothing to do
    //             } else if (parent.type === 'VariableDeclarator') {
    //                 if (parent.init.type === 'Literal') {
    //                     value = parent.init.value;
    //                 } else if (parent.init.type === 'BinaryExpression') {
    //                     value = getValueStringBinaryExpression(ast, parent);
    //                 }
    //             }
    //         }
    //     },
    //     leave: function(node, parent) {}
    // });
    // return value;
}

/**
 * 
 * @param {*} identifierName identificador da variável
 * 
 * @returns true, se a variável é um parâmetro da função, caso contrário, false
 */
function identifierIsParam(identifierName) {
    let functions = [];
    let lastFunction = actualFunction.pop();
    let isParam = false;
    while (lastFunction !== undefined && !isParam) {
        functions.push(lastFunction);
        lastFunction.params.forEach(param => {
            if (param.name === identifierName) {
                isParam = true;
            }
        });
        lastFunction = actualFunction.pop();
    }
    lastFunction = functions.pop();
    while (lastFunction !== undefined) {
        actualFunction.push(lastFunction);
        lastFunction = functions.pop();
    }
    return isParam;
}

/**
 * 
 * @param {*} ast ast do código
 * @param {*} node nó com expressão binária
 * 
 * @returns valor de uma string resultante de uma expressão binária
 */
function getValueStringBinaryExpression(ast, node) {
    value = '';
    nodes = [node.left, node.right];
    if (node.operator === '+') {
        for (_node in nodes) {
            if (_node.type === 'Literal') {
                value += _node.value;
            } else if (_node.type === 'BinaryExpression') {
                value += getValueStringBinaryExpression(ast, _node);
            } else if (_node.type === 'CallExpression') {
                if (_node.calle.type === 'MemberExpression') {

                }
            } else if (_node.type === 'MemberExpression') {
                value += '';
            }
        }
    }
    return value;
}

function getVar() {

}

/**
 * 
 * @param {*} ast ast do código
 * @param {*} node nó com chamada de função
 * 
 * @returns valor de uma string resultante de uma chamada de função
 */
function getValueStringCallExpression(ast, node) {
    if (node.calle.type === 'MemberExpression') {

    }
    value = '';

    return value;
}




estraverse.traverse(ast, {
    enter: function(node, parent) {
        // Nó de importação de arquivo com a função require
        if (node.type === 'CallExpression' &&
            node.callee.name === 'require') {
            // rastreando importação de uma biblioteca para requisições
            if (node.arguments[0].type === 'Literal' &&
                node.arguments.length === 1 &&
                httpRequestLibrarys.contains(node.arguments[0].value)) {
                if (parent.type === 'VariableDeclarator') {
                    requireVarName.push(parent.id.name);
                }
            }
            // rastreando importação de arquivo do próprio projeto
            if (node.arguments[0].type === 'Literal' &&
                node.arguments.length === 1 &&
                node.arguments[0].value.startsWith('.')) {
                if (parent.type === 'VariableDeclarator') {
                    varRequireFromProject[parent.id.name] = node.arguments[0].value;
                }
            }
        }
        // nó de chamada de função pelo membro de um objeto, onde este objeto foi importado
        // de um arquivo do próprio projeto (depêndencia dentro do projeto)
        if (node.type === 'CallExpression' &&
            node.callee.type === 'MemberExpression' &&
            varRequireFromProject[node.callee.object.name] !== undefined) {
            let requestDependency = {
                file: varRequireFromProject[node.callee.object.name],
                member: node.callee.property.name
            };
            let lastFunction = actualFunction.pop();
            while (lastFunction !== undefined &&
                lastFunction.end < node.end) {
                lastFunction = actualFunction.pop();
            }
            if (lastFunction !== undefined) {
                let funcionId = 'f' + lastFunction.start;
                if (requestDependencyFromFunctions[funcionId] === undefined) {
                    requestDependencyFromFunctions[funcionId] = [];
                }
                requestDependencyFromFunctions[funcionId].push(requestDependency);
                actualFunction.push(lastFunction);
            } else {
                requestDependencyFromFunctions['global'].push(requestDependency);
            }
        }
        // nó de chamada de função pelo objeto de uma biblioteca de requisições
        if (node.type === 'CallExpression' &&
            ((node.callee.type === 'MemberExpression' &&
                    requireVarName.contains(node.callee.object.name)) ||
                (node.callee.type === 'Identifier' &&
                    requireVarName.contains(node.callee.name)))) {
            //console.log(node);

            let requestURL;
            if (node.arguments[0].type === 'Literal') {
                requestURL = node.arguments[0].value;
            } else if (node.arguments[0].type === 'Identifier') {
                requestURL = getValueStringIdentifier(ast, node.arguments[0].name, node.arguments[0].start, node.start);
            } else if (node.arguments[0].type === 'ObjectExpression') {

            } else if (node.arguments[0].type === 'BinaryExpression') {
                requestURL = getValueStringBinaryExpression(ast, node.arguments[0]);
            }
            requestMs.push(requestURL);
            let lastFunction = actualFunction.pop();
            while (lastFunction !== undefined &&
                lastFunction.end < node.end) {
                lastFunction = actualFunction.pop();
            }
            if (lastFunction !== undefined) {
                let funcionId = 'f' + lastFunction.start;
                if (requestMsFromFunctions[funcionId] === undefined) {
                    requestMsFromFunctions[funcionId] = [];
                }
                requestMsFromFunctions[funcionId].push(requestURL);
                actualFunction.push(lastFunction);
            } else {
                requestMsFromFunctions['global'].push(requestURL);
            }
        }
        // nó de definição de uma função
        if (node.type === 'FunctionExpression' ||
            node.type === 'ArrowFunctionExpression') {
            let lastFunction = actualFunction.pop();
            while (lastFunction !== undefined &&
                lastFunction.end < node.end) {
                lastFunction = actualFunction.pop();
            }
            if (lastFunction !== undefined) {
                actualFunction.push(lastFunction);
            }
            actualFunction.push(node);
        }
        // nó de definição de membro de exportação do módulo do arquivo
        if (node.type === 'AssignmentExpression' &&
            node.operator === '=' &&
            node.left.type === 'MemberExpression' &&
            node.left.object.type === 'MemberExpression' &&
            node.left.object.object.name === 'module' &&
            node.left.object.property.name === 'exports') {
            if (node.right.type === 'FunctionExpression' ||
                node.right.type === 'ArrowFunctionExpression') {
                requestMsFromExportsInv['f' + node.right.start] = node.left.property.name;
                requestMsFromExports[node.left.property.name] = 'f' + node.right.start;
            } else if (node.right.type === 'Identifier') {
                requestMsFromExportsInv[node.right.name] = node.left.property.name;
                requestMsFromExports[node.left.property.name] = node.right.name;
            }
        }
    },
    leave: function(node, parent) {}
});

console.log('\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n');
console.log("###Relatório###");
console.log('requestMs');
console.log(requestMs);
console.log('requestDependencyFromFunctions');
console.log(requestDependencyFromFunctions);
console.log('requestMsFromFunctions');
console.log(requestMsFromFunctions);
console.log('requestMsFromExports');
console.log(requestMsFromExports);