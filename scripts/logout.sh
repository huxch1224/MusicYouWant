#!/bin/bash

export temp=$(cd "$(dirname "$0")" && pwd)
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
/usr/local/hadoop/bin/hdfs dfs -rmr input/*
/usr/local/hadoop/bin/hdfs dfs -put $temp/new_info.txt input
/usr/local/hadoop/bin/hadoop jar /usr/local/hadoop/bin/jars/updatesong.jar UpdateSong
/usr/local/hadoop/bin/hdfs dfs -rmr song.txt
/usr/local/hadoop/bin/hdfs dfs -cp output/part-r-00000 song.txt
rm $temp/new_info.txt
