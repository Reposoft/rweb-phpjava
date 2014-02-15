#!/bin/bash

mvn clean compile war:exploded
cp -r repos/repos-web target/repos-web
cp repos/build.xml target/
pushd target/
ant encode.templates
popd
cp -r quercus-war-4.0/* target/repos-web/
cp -r target/rweb-phpjava/WEB-INF/* target/repos-web/WEB-INF/
echo "<?php phpinfo(); ?>" > target/repos-web/phpinfo.php
# included in resin? getting class loader errors.
rm target/repos-web/WEB-INF/lib/slf4j-api-1.7.5.ja

