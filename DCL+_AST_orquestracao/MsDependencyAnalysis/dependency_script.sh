#!/bin/bash


if [ -e $PWD/communicates.txt ]; then
	rm $PWD/communicates.txt
fi

if ! [[ -z "$1" ]]; then
	find $1 -path ./node_modules -prune -o -name '*.js' -exec node app.js {} \;
fi

