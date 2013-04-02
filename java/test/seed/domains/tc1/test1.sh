#!/bin/bash -xe
echo $*
sleep 2
`dirname $0`/dep.sh 1 $* &
ls
sleep 2
`dirname $0`/dep.sh 2 $* &
echo OK
