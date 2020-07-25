# -*- coding: utf-8 -*-
#!/usr/bin/python3

from __future__ import unicode_literals
from os.path import isfile, join
import sys, getopt
import os
import random
import collections
import time
import re

FOLDER_AST_VISITOR = 'src/org/ufla/dcc/jsdepextractor'
PACKAGE_AST_VISITOR = 'org.ufla.dcc.jsdepextractor'
PACKAGE_NODE_TREES_CLOSURE_COMPILER = 'com.google.javascript.jscomp.parsing.parser.trees'
NODE_TYPES_FILE = 'node-tree-types.txt'
AST_VISITOR = 'ASTVisitor'
AST_VISITOR_TRAVERSAL = 'ASTVisitorTraversal'
TAB = '  '


with open(NODE_TYPES_FILE, 'r') as nodeTypesFile:
    types2 = nodeTypesFile.readlines()
    types = []
    for type in types2:
        types.append(type[:-1])
    


def writePackageAndImports(file, types):
    file.write('package ' + PACKAGE_AST_VISITOR + ';\n\n')
    for type in types:
        file.write('import ' + PACKAGE_NODE_TREES_CLOSURE_COMPILER + '.' + type + ';\n')

with open(FOLDER_AST_VISITOR + '/' + AST_VISITOR + '.java', 'w') as astVisitor:
    writePackageAndImports(astVisitor, types)

    astVisitor.write('\npublic abstract class ASTVisitor {\n\n')
    astVisitor.write('  public ASTVisitor(ParseTree node) {\n')
    astVisitor.write('    ASTVisitorTraversal.traversalVisitorFromNode(this, node);\n')
    astVisitor.write('  }\n\n\n')

    for type in types:
        astVisitor.write('  public boolean visit(' + type + ' node) {\n')
        astVisitor.write('    return true;\n')
        astVisitor.write('  }\n\n')
    astVisitor.write('}\n')

def getType2FromType(type):
    type2 = type[0]
    for c in type[1:-4]:
        if c.islower():
            type2 += c.upper()
        else:
            type2 += '_' + c
    return type2

def getMethodTypeFromType(type):
    return 'as' + type[:-4];

with open(FOLDER_AST_VISITOR + '/' + AST_VISITOR_TRAVERSAL + '.java', 'w') as astVisitorTraversal:
    writePackageAndImports(astVisitorTraversal, types)

    astVisitorTraversal.write('\nclass ASTVisitorTraversal {\n\n')

    astVisitorTraversal.write('  public static void traversalVisitorFromNode(ASTVisitor astVisitor, ParseTree node) {\n')
    astVisitorTraversal.write('    ASTVisitorTraversal astVisitorTraversal = new ASTVisitorTraversal(astVisitor);\n')
    astVisitorTraversal.write('    astVisitorTraversal.traversal(node);\n')
    astVisitorTraversal.write('  }\n\n\n')

    astVisitorTraversal.write('  private ASTVisitor astVisitor;\n\n\n')

    astVisitorTraversal.write('  private ASTVisitorTraversal(ASTVisitor astVisitor) {\n')
    astVisitorTraversal.write('    this.astVisitor = astVisitor;\n')
    astVisitorTraversal.write('  }\n\n\n')

    for type in types:
        astVisitorTraversal.write('  public void traversal(' + type + ' node) {\n')
        astVisitorTraversal.write('    if (!astVisitor.visit(node)) {\n')
        astVisitorTraversal.write('      return;\n')
        astVisitorTraversal.write('    }\n')
        if type == 'ParseTree':
            astVisitorTraversal.write('    switch (node.type) {\n')
            for type2 in types:
                astVisitorTraversal.write('      case ' + getType2FromType(type2) + ':\n')
                astVisitorTraversal.write('        traversal(node.' + getMethodTypeFromType(type2) + '());\n')
                astVisitorTraversal.write('        break;\n')
            astVisitorTraversal.write('      default:\n')
            astVisitorTraversal.write('        break;\n')
            astVisitorTraversal.write('    }\n\n')
        astVisitorTraversal.write('  }\n\n')
    astVisitorTraversal.write('}\n')