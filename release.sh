#!/bin/bash

# rm -rf ~/.m2/repository/

touch dependency-reduced-pom.xml pom.xml.releaseBackup release.properties
rm dependency-reduced-pom.xml pom.xml.releaseBackup release.properties

/home/rainer/scripts/lazy-git.sh

mvn clean -q
# mvn deploy -X
mvn release:prepare -Phihn-releases
mvn release:perform -Phihn-releases