cd target

# fix grisu jar file
mkdir grix
cd grix

unzip -o ../grix-1.1-dist.jar
rm ../grix-1.1-dist.jar
rm -f META-INF/INDEX.LIST
cp ../../deployment-config/log4j.properties .
jar cmf ../../grix_impl/MANIFEST.MF ../grix.jar .
cd ..