#!/bin/bash

# rm -rf ~/.m2/repository/
touch dependency-reduced-pom.xml pom.xml.releaseBackup release.properties
rm dependency-reduced-pom.xml pom.xml.releaseBackup release.properties

tmp='.tmp.commit'

git pull github
git status --porcelain -uno > $tmp
git commit -a -F $tmp
git push github
rm $tmp



mvn clean -q
# mvn deploy -X
mvn release:prepare -Possrh
mvn release:perform -Possrh
