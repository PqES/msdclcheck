const espree = require('espree');
const estraverse = require('estraverse');
const esquery = require('esquery');
const escodegen = require('escodegen');
const fs = require('fs');
const readline = require('readline-sync');
const path = require('path');

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


if(process.argv[3] === undefined)
     var file = process.argv[2];
else {
     console.log(' [ERROR] File not existent!');
     process.exit(1);
}

console.log (' [INFO] ANALYZING ' + file);
const code = fs.readFileSync(file, 'utf-8').replaceAll('let\\s', 'var ');

console.log (' [INFO] AST INITIALIZING...');
const ast = espree.parse(code,  {
     loc: true,
     ecmaVersion: 6,
     sourceType: "module"
});
console.log (' [INFO] AST INITIALIZED');


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
var express_property =  {};
var const_mapping = [];
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
               return 'IdentifierIsParam';
          }
     }


     console.log(n_node);

     if (n_node.type === 'Literal'){
          let rAux = n_node.value;


          if (rAux === 'env')
               return 'IdentifierIsParam';

          if (rAux !== path.basename(rAux)){

               let ms_aux;

               rAux = n_node.value;

               let array_aux = rAux.split('/');

               if (array_aux[0] === '')
                    ms_aux = array_aux[1].toString();
               else
                    ms_aux = array_aux[0].toString();

               let url_aux = rAux.substring(rAux.indexOf(ms_aux) + ms_aux.length, rAux.length);

               rAux = {
                    microservice : ms_aux,
                    url : url_aux,
                    lineStart : n_node.loc.start.line,
                    lineEnd : n_node.loc.end.line
               };
          }

          return rAux;
     } else {

          console.log (' [INFO] USING EQUALS CONDITIONAL');
          let node_string = code.substring(n_node.start, n_node.end);

          let array_node_string = node_string.split('\.');

          // console.log(const_mapping[array_node_string[0]]);
          console.log (array_node_string);

          if (array_node_string[0].indexOf('\n') != -1)
               array_node_string[0] = array_node_string[0].slice(0, array_node_string[0].indexOf('\n'));

          if (const_mapping[array_node_string[0]] !== undefined){
               let rNodeValue = const_mapping[array_node_string[0]].toString().replaceAll('\:', '\$');

               let array_aux = rNodeValue.split('/');

               let ms_aux;

               if (array_aux[0] === '')
                    ms_aux = array_aux[1].toString();
               else
                    ms_aux = array_aux[0].toString();

               let url_aux = rNodeValue.substring(rNodeValue.indexOf(ms_aux) + ms_aux.length, rNodeValue.length);

               let requestObj = {
                    microservice : ms_aux,
                    url : url_aux,
                    lineStart : n_node.loc.start.line,
                    lineEnd : n_node.loc.end.line
               };

               return requestObj;
          }
     }

     console.log ('[INFO] USING GENERIC FUNCTION');
     let startFunction = getStartFunction();
     // let nextNode

     const newAST = espree.parse(code,  {
          loc: true,
          ecmaVersion: 6,
          sourceType: "module"
     });



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
                    console.log(' [INFO] Substring: ' + code.substring(n_node.start, n_node.end));
               }

               return retNode;
          },
          leave: function(node, parent) {}
     });

     let codeResult = escodegen.generate(newAST);

     if(getFunctionExportName() !== undefined)
          codeResult += '\nmodule.exports.' + getFunctionExportName() + '();\n';
     /*else if(express_property !== ''){
          console.log(express_property.argument);
          // codeResult += '\nvar app_temp = require(\'' + __dirname + '/' + express_property.argument + '\');\n \napp.use(app_temp);\n';7
          codeResult += '\nmodule.exports.function()\{app.' + express_property.argument + '()\};'
     }*/

     let fileTemp = file.substring(0, file.length - 3) + '_temp.js';

     writeCode(codeResult, fileTemp);
     // console.log(codeResult);


     let rRequire = require(fileTemp).SECRET_VAR_STRING_VALUE;

     fs.unlinkSync(fileTemp, (err) => {
          if (err) throw err;});

     delete require.cache[require.resolve(fileTemp)];

     console.log(rRequire);

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

          if (node.type === 'CallExpression' && node.callee.type === 'MemberExpression' && node.callee.object.name === 'app'
              && httpRequestLibrarys.contains('express') && node.arguments[0].type === 'Literal'){
              express_property = {
                  name: node.callee.property.name,
                  end: node.end,
                  argument: node.arguments[0].value.substring(1, node.arguments[0].value - 1)
              };
          } else if(express_property !== '' && node.start >= express_property.end){
              express_property = '';
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

               requestURL = getNodeValue(ast, node.arguments[0], parent_aux);

               if (requestURL !== 'IdentifierIsParam'){

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
          } else if(node.type === 'CallExpression' && node.callee.name === 'getRoute'){

               let requestURL = getNodeValue(ast, node.arguments[0], parent);

               if (requestURL !== 'IdentifierIsParam'){

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

          if (node.type === 'VariableDeclarator' && node.init !== null && node.init.type === 'Literal' && isNaN(node.init.value)){

               let init_aux = node.init.value;

               if (init_aux !== null && init_aux !== path.basename(init_aux)){
                    if (const_mapping[node.id.name] === undefined)
                         const_mapping[node.id.name] = [];

                    const_mapping[node.id.name].push(node.init.value);

                    // console.log ('CONST: ' + node.id.name + ' | Value: ' + const_mapping[node.id.name]);
               }
          }
     },
     leave: function(node, parent) {}
});



/* console.log('\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n');
console.log("###Relatório###");
console.log('requestMs');
console.log(requestMs);
console.log('requestDependencyFromFunctions');
console.log(requestDependencyFromFunctions);
console.log('requestMsFromFunctions');
console.log(requestMsFromFunctions);
console.log('requestMsFromExports');
console.log(requestMsFromExports);*/

let output_file = 'communicates.txt';

let output_content = '';

let substring_file = file.substring(0, file.length - 3);

for (let ms of requestMs){
     console.log (substring_file + ' communicates cba-' + ms.microservice + ' using ' + ms.url + ' starting in line ' + ms.lineStart + ' and ending in line ' + ms.lineEnd);
     output_content += substring_file + ' communicates ' + ms.microservice + ' using ' + ms.url + '\n';
}

fs.appendFile('communicates.txt', output_content , function (err) {
  if (err) throw err;
});

/* process.stdin.setRawMode(true);
process.stdin.resume();
process.stdin.on('data', process.exit.bind(process, 0)); */
