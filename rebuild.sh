#!/bin/sh
cp -r target/classes/* target/repos-web/WEB-INF/classes/
cp -r src/main/webapp/WEB-INF/* target/repos-web/WEB-INF/
# symlink?
cp -r ../resin/modules/quercus/bin/* target/repos-web/WEB-INF/classes/

touch target/repos-web/WEB-INF/web.xml
