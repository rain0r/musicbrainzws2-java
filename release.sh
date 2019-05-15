#!/bin/bash

# rm -rf ~/.m2/repository/

clear

# touch dependency-reduced-pom.xml pom.xml.releaseBackup release.properties
# rm dependency-reduced-pom.xml pom.xml.releaseBackup release.properties
/home/rainer/scripts/lazy-git.sh

# mvn clean -q
# mvn deploy -X
mvn release:prepare && mvn release:perform
