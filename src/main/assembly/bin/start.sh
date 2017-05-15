#!/bin/sh
JAVA_OPT="-Xmx128m -Xms64m -server"
JAR_NAME=${project.build.finalName}.jar

if [ -z "$JAVA_HOME" ];then
    export JAVA=`which java`
else
    export JAVA="$JAVA_HOME/bin/java"
fi

BIN_DIR=$(cd `dirname $0`; pwd)
BASE_DIR=$(cd `dirname $BIN_DIR`;pwd)
cd ${BASE_DIR}
CONF_DIR=$BASE_DIR/conf
LOG_DIR=$BASE_DIR/log
LIB_DIR=$BASE_DIR/lib
NO_HUP_FILE=$LOG_DIR/nohup
PID_FILE=$BIN_DIR/run.pid

if [ -f "$PID_FILE" ]; then
    pid=$(cat "$PID_FILE")
    process=`ps aux | grep "$pid" | grep -v grep`;
    if [ "$process" != "" ];then
        echo "the timer-consumer is running!";
        exit 1;
    fi
fi

sleep 1

touch $NO_HUP_FILE
nohup $JAVA $JAVA_OPT  -jar $LIB_DIR/$JAR_NAME 2>&1 >$NO_HUP_FILE &

touch $PID_FILE
echo $! > $PID_FILE
chmod 755 $PID_FILE



