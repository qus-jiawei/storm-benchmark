#!/bin/bash
#需要添加~/storm/conf/supervisors 文件，里面描写了所有的supervisor的机器
SSH_PORT=22
supervisors=`cat ~/storm/conf/supervisors`
mkdir -p ~/storm_test/collect_data

for supervisor in $supervisors
do
    echo $supervisor
    scp -P ${SSH_PORT} $supervisor:~/storm_test/test_data/* ~/storm_test/collect_data/
    ssh -p ${SSH_PORT} $supervisor "rm -rf ~/storm_test/test_data/*"
done

