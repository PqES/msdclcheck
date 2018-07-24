const espree = require('espree');
const estraverse = require('estraverse');
const esquery = require('esquery');
const escodegen = require('escodegen');
const fs = require('fs');
const readline = require('readline-sync');


/* *
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
     })
}


const file = readline.question('File path :> ');


const code = fs.readFileSync(file, 'utf-8').replaceAll('let\\s', 'var ');
const ast = espree.parse(code);


var varRequireFromProject = {};
var httpRequestLibrarys = ['http', 'express', 'request'];
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

function checkRest(node){

     if(node.callee.property.name === 'get' || node.callee.property.name === 'delete' || node.callee.property.name === 'post'
     || node.callee.property.name === 'update'){

          return true;
     }

     return false;
}

function getNodeValue(ast, n_node, parent_node){

     if(n_node.type === 'Identifier'){
          if (identifierIsParam(n_node.name)) {
               return '';
          }
     }
     let startFunction = getStartFunction();
     // let nextNode

     let newAST = espree.parse(code);


     let removing = estraverse.replace(newAST, {
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
                         if (identifierIsParam(actualNode.name) ) {
                              retNode = {
                                   type: 'Literal',
                                   value: ''
                              };
                         }
                    }
               }
               if (node.start === parent_node.start) {
                    retNode = espree.parse('module.exports.SECRET_VAR_STRING_VALUE = ' + code.substring(n_node.start, n_node.end) );
                    console.log("-------------------------------Substring: " + code.substring(n_node.start, n_node.end));
               }

               return retNode;
          },
          leave: function(node, parent) {}
     });

     let codeResult = escodegen.generate(newAST);

     if(getFunctionExportName() !== undefined)
          codeResult += '\nmodule.exports.' + getFunctionExportName() + '();\n';
     /*else if(nodeFunction !=== undefined)
          codeResult += '\nmodule.exports.' +  + '();\n';*/



     let fileTemp = file.substring(0, file.length - 3) + '_temp.js';
     writeCode(codeResult, fileTemp);
     // console.log(codeResult);


     let rRequire = require(fileTemp).SECRET_VAR_STRING_VALUE;

     if(rRequire === undefined)
          return code.substring(n_node.start, n_node.end);

     return rRequire;

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


estraverse.traverse(ast, {
     enter: function(node, parent) {
          // Nó de importação de arquivo com a função require
          if ((node.type === 'CallExpression' &&
          node.callee.name === 'require')) {
               // rastreando importação de uma biblioteca para requisições
               if (node.arguments[0].type === 'Literal' &&
               node.arguments.length === 1 &&  httpRequestLibrarys.contains(node.arguments[0].value)) {
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

          if(node.type === 'CallExpression' && node.callee.name === 'express'){
               requireVarName.push(parent.id.name);
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
          if ((node.type === 'CallExpression' &&
          ((node.callee.type === 'MemberExpression' &&
          requireVarName.contains(node.callee.object.name) && checkRest(node)) ||
          (node.callee.type === 'Identifier' &&
          requireVarName.contains(node.callee.name))) && node.arguments.length > 0)) {

               let requestURL;


               var parent_aux = 0;

               if(parent.type === 'CallExpression')
                    parent_aux = parent;
               else
                    parent_aux = node;

               // console.log(parent);

               requestURL = getNodeValue(ast, node.arguments[0], parent_aux);

               console.log("REQUESTED URLD: " + requestURL);

               fs.unlinkSync(file.substring(0, file.length - 3) + '_temp.js', (err) => {if (err) throw err;});

               delete require.cache[require.resolve(file.substring(0, file.length - 3) + '_temp.js')];

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
               while (lastFunction !== undefined && lastFunction.end < node.end) {
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


let local_file = __filename;
local_file = local_file.substring(local_file.indexOf('api'), local_file.length - 1);

let ms_name = local_file.substring(local_file.indexOf('api/'), local_file.indexOf('/index.js'));

let output_file = 'communications_' + ms_name + '.txt';

console.log("LOCAL FILE: " + local_file);

let input_text;

requestMs.forEach(function(item, index, array){
     input_text += local_file + ' communicates ' + ms_name + ' using ' + item + '\n';
});

requestDependencyFromFunctions.forEach(function(item, index, array){
     input_text += local_file + ' communicates ' + item.file + ' using ' + item.member + '\n';
});

requestMsFromFunctions.forEach(function(item, index, array){
     input_text += local_file + ' communicates ' + ms_name + ' using ' + item + '\n';
});

writeCode(output_file, input_text);

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
