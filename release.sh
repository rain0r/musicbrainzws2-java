#!/bin/bash

# rm -rf ~/.m2/repository/
touch dependency-reduced-pom.xml pom.xml.releaseBackup release.properties
rm dependency-reduced-pom.xml pom.xml.releaseBackup release.properties

/home/rainer/scripts/lazy-git.sh

mvn clean -q
mvn release:prepare -X
mvn release:perform -X

# mvn javadoc:javadoc 
