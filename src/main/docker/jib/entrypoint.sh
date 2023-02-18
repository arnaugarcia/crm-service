#!/bin/sh

echo "Application is starting"
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.theagilemonkeys.crmservice.CrmServiceApplication"  "$@"
