#!/bin/sh
HOME=/home/ca2ssim
CONFIG_FILE=$HOME/ca2ssim/config/acssim.config
PATH_TO_JAR=$HOME/ca2ssim/lib/acsrmqsim-jar-with-dependencies.jar
JAVA_OPT="-Dlogback.configurationFile=$HOME/ca2ssim/config/logback_acssim.xml"


#java -jar $PATH_TO_JAR $CONFIG_FILE
source /etc/profile
java $JAVA_OPT $DEBUG -classpath $PATH_TO_JAR media.platform.acsrmqsim.AcsRmqSimMain $CONFIG_FILE
exit