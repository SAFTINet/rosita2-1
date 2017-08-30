#!/bin/bash
#
# Load OMOP vocabulary file in this order so as not to
#  not violate foreign key constraints
#
export PGDATABASE=rosita
export PGUSER=rosita
export PGPASSWORD=`grep "^password=" /usr/share/rosita/rosita.properties | cut -d "=" -f 2`
export VOCABPATH=`grep "^folder\.vocabulary=" /usr/share/rosita/rosita.properties | cut -d "=" -f 2`

if [[ -a /usr/bin/psql ]]
then
  psql_bin='/usr/bin/psql'
elif [[ -a /usr/local/bin/psql ]]
then
  psql_bin='/usr/local/bin/psql'
else
  echo "Unable to locate psql executable"
  exit 1
fi

$psql_bin --file=$VOCABPATH/rz_drop_constraints.sql
echo "STATUS|||1|||12"

$psql_bin -c "TRUNCATE TABLE rz.source_to_concept_map;"
$psql_bin -c "TRUNCATE TABLE rz.concept_ancestor;"
$psql_bin -c "TRUNCATE TABLE rz.concept_relationship;"
$psql_bin -c "TRUNCATE TABLE rz.concept_synonym;"
$psql_bin -c "TRUNCATE TABLE rz.relationship;"
$psql_bin -c "TRUNCATE TABLE rz.concept CASCADE;"
$psql_bin -c "TRUNCATE TABLE rz.vocabulary CASCADE;"
echo "STATUS|||2|||12"

$psql_bin -c "COPY rz.vocabulary(VOCABULARY_ID,VOCABULARY_NAME) FROM '$VOCABPATH/VOCABULARY.csv' WITH (FORMAT CSV, HEADER, ENCODING 'LATIN1');"
echo "STATUS|||3|||12"

$psql_bin -c "COPY rz.concept(CONCEPT_ID,CONCEPT_NAME,CONCEPT_LEVEL,CONCEPT_CLASS,VOCABULARY_ID,CONCEPT_CODE,VALID_START_DATE,VALID_END_DATE,INVALID_REASON) FROM '$VOCABPATH/CONCEPT.csv' WITH (FORMAT CSV, HEADER, ENCODING 'LATIN1');"
echo "STATUS|||4|||12"

$psql_bin -c "COPY rz.relationship(RELATIONSHIP_ID,RELATIONSHIP_NAME,IS_HIERARCHICAL,DEFINES_ANCESTRY,REVERSE_RELATIONSHIP) FROM '$VOCABPATH/RELATIONSHIP.csv' WITH (FORMAT CSV, HEADER, ENCODING 'LATIN1');"
echo "STATUS|||5|||12"

$psql_bin -c "COPY rz.concept_synonym(CONCEPT_SYNONYM_ID,CONCEPT_ID,CONCEPT_SYNONYM_NAME) FROM '$VOCABPATH/CONCEPT_SYNONYM.csv' WITH (FORMAT CSV, HEADER, ENCODING 'LATIN1');"
echo "STATUS|||6|||12"

$psql_bin -c "COPY rz.source_to_concept_map(SOURCE_CODE,SOURCE_VOCABULARY_ID,SOURCE_CODE_DESCRIPTION,TARGET_CONCEPT_ID,TARGET_VOCABULARY_ID,MAPPING_TYPE,PRIMARY_MAP,VALID_START_DATE,VALID_END_DATE,INVALID_REASON) FROM '$VOCABPATH/SOURCE_TO_CONCEPT_MAP.csv' WITH (FORMAT CSV, HEADER, ENCODING 'LATIN1');"
echo "STATUS|||7|||12"

$psql_bin -c "COPY rz.concept_relationship(CONCEPT_ID_1,CONCEPT_ID_2,RELATIONSHIP_ID,VALID_START_DATE,VALID_END_DATE,INVALID_REASON) FROM '$VOCABPATH/CONCEPT_RELATIONSHIP.csv' WITH (FORMAT CSV, HEADER, ENCODING 'LATIN1');"
echo "STATUS|||8|||12"

$psql_bin -c "COPY rz.concept_ancestor(ANCESTOR_CONCEPT_ID,DESCENDANT_CONCEPT_ID,MIN_LEVELS_OF_SEPARATION,MAX_LEVELS_OF_SEPARATION) FROM '$VOCABPATH/CONCEPT_ANCESTOR.csv' WITH (FORMAT CSV, HEADER, ENCODING 'LATIN1');"
echo "STATUS|||9|||12"

$psql_bin --file=$VOCABPATH/rz_add_constraints.sql
echo "STATUS|||10|||12"

# Add record to vocabulary table to define custom vocabularies
$psql_bin -c "INSERT INTO rz.vocabulary(vocabulary_id, vocabulary_name) VALUES (99, 'Custom');"
echo "SUCCESS|||11|||12"

echo load vocabulary complete
