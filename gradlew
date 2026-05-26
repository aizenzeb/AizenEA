#!/usr/bin/env sh

DIR="$(cd "$(dirname "$0")"; pwd -P)"
APP_BASE_NAME=$(basename "$0")

CLASSPATH=$DIR/gradle/wrapper/gradle-wrapper.jar

DEFAULT_JVM_OPTS=""

exec java $DEFAULT_JVM_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
