#!/bin/bash

export temp=$(cd "$(dirname "$0")" && pwd)
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
/usr/local/hadoop/bin/hdfs dfs -rmr input/*
/usr/local/hadoop/bin/hdfs dfs -put $temp/user_info.txt input
/usr/local/hadoop/bin/hadoop jar /usr/local/hadoop/bin/jars/updateuser.jar UpdateUser
/usr/local/hadoop/bin/hdfs dfs -rmr input/*
/usr/local/hadoop/bin/hdfs dfs -cp output/part-r-00000 input/user_info.txt
/usr/local/hadoop/bin/hdfs dfs -get input/user_info.txt $temp/tempp.txt
/usr/local/hadoop/bin/hadoop jar /usr/local/hadoop/bin/jars/r_one.jar RecommendationOne
/usr/local/hadoop/bin/hdfs dfs -rmr input/*
/usr/local/hadoop/bin/hdfs dfs -cp song.txt input
/usr/local/hadoop/bin/hdfs dfs -cp temp1/part-r-00000 temp.txt
/usr/local/hadoop/bin/hadoop jar /usr/local/hadoop/bin/jars/r_two.jar RecommendationTwo
/usr/local/hadoop/bin/hdfs dfs -get output/part-r-00000 $temp/temp.txt
/usr/local/hadoop/bin/hdfs dfs -rmr temp.txt
