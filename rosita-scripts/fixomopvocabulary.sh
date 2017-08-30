#!/bin/bash
#
# FIX OMOP vocabulary files
#

# Remove trailing | characters from csv files
sed -E -e "s/\|$//" -i $VOCABPATH/VOCABULARY.csv
sed -E -e "s/\|$//" -i $VOCABPATH/CONCEPT.csv
sed -E -e "s/\|$//" -i $VOCABPATH/RELATIONSHIP.csv
sed -E -e "s/\|$//" -i $VOCABPATH/CONCEPT_SYNONYM.csv
sed -E -e "s/\|$//" -i $VOCABPATH/SOURCE_TO_CONCEPT_MAP.csv
sed -E -e "s/\|$//" -i $VOCABPATH/CONCEPT_RELATIONSHIP.csv
sed -E -e "s/\|$//" -i $VOCABPATH/CONCEPT_ANCESTOR.csv
