#!/bin/bash
# Script to copy code from Jenkins build directories into ROSITA-2.0.2 directory
release='ROSITA-2.0.2'
mkdir -v $release
mkdir -v $release/database
mkdir -v $release/rosita
mkdir -v $release/rosita/schemas
mkdir -v $release/rosita/rules
mkdir -v $release/rosita/rules/etl
mkdir -v $release/rosita/rules/profile
mkdir -v $release/rosita/rules/publish
cp -v /var/lib/jenkins/jobs/rositaui-trunk-build/workspace/target/rosita.war $release
cp -v /var/lib/jenkins/jobs/rositalib-trunk-build/workspace/target/rosita-lib-2.0.*.jar $release/rosita/rosita-lib.jar
cp -v /var/lib/jenkins/jobs/rositascripts-trunk-build/workspace/*.template $release/rosita/
cp -v /var/lib/jenkins/jobs/rositascripts-trunk-build/workspace/rosita.sh $release/rosita/
cp -v /var/lib/jenkins/jobs/rositascripts-trunk-build/workspace/backupdata.sh $release/rosita/
cp -v /var/lib/jenkins/jobs/rositascripts-trunk-build/workspace/canceljava.sh $release/rosita/
cp -v /var/lib/jenkins/jobs/rositascripts-trunk-build/workspace/loadomopvocabulary.sh $release/rosita/
cp -rv /var/lib/jenkins/jobs/rositascripts-trunk-build/workspace/lib $release/rosita/
cp -v /var/lib/jenkins/jobs/rositascripts-trunk-build/workspace/*spec*.csv $release/rosita/schemas/
cp -v /var/lib/jenkins/jobs/rositascripts-trunk-build/workspace/*spec*.xsd $release/rosita/schemas/
cp -v /var/lib/jenkins/jobs/rositascripts-trunk-build/workspace/*etl-rules*.csv $release/rosita/rules/etl
cp -v /var/lib/jenkins/jobs/rositascripts-trunk-build/workspace/profile-rules*.csv $release/rosita/rules/profile
cp -v /var/lib/jenkins/jobs/rositascripts-trunk-build/workspace/publish-rules*.csv $release/rosita/rules/publish/
cp -vr /var/lib/jenkins/jobs/rositadb-trunk-build/workspace/* $release/database/
chmod -v u+x $release/database/setup.sh
chmod -v ug+r $release/rosita/rosita-lib.jar
chmod -v ug+r $release/rosita/*.template
chmod -v ug+x $release/rosita/*.sh
chmod -vR g+r $release/rosita/schemas
chmod -vR g+r $release/rosita/rules
