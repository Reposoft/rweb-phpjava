#!/bin/sh
cp -r target/classes/* target/repos-web/WEB-INF/classes/
cp -r src/main/webapp/WEB-INF/* target/repos-web/WEB-INF/
touch target/repos-web/WEB-INF/web.xml
