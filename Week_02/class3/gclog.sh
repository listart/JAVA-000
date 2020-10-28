#!/bin/bash

for use in "SerialGC" "ParallelGC" "ConcMarkSweepGC" "G1GC"
do
    for c in 128 512 1024 2048 4096
    do
        echo "${use}-${c}m"
        java -XX:+Use${use} -Xms${c}m -Xmx${c}m -Xloggc:log/${use}${c}M.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis > log/${use}${c}M.out 2>&1
    done 
done
