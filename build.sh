#!/bin/bash

mvn clean compile war:exploded
cp -r repos/repos-web target/repos-web
cp repos/build.xml target/
pushd target/
ant encode.templates
popd
cp -r quercus-webapp/* target/repos-web/
cp -r target/rweb-phpjava/WEB-INF/* target/repos-web/WEB-INF/
echo "<?php phpinfo(); ?>" > target/repos-web/phpinfo.php

