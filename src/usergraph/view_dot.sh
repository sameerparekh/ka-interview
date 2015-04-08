#!/bin/bash
base=$1
dot -Tpng -o$1.png < $1.dot
open $1.png
