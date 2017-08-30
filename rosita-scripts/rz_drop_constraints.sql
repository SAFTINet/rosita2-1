/*
 *   Copyright 2012-2013 The Regents of the University of Colorado
 *
 *   Licensed under the Apache License, Version 2.0 (the "License")
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

ALTER TABLE rz.concept_ancestor
  DROP CONSTRAINT IF EXISTS concept_ancestor_pk,
  DROP CONSTRAINT IF EXISTS concept_ancestor_ancestor_concept_fk,
  DROP CONSTRAINT IF EXISTS concept_ancestor_descendant_concept_fk;

ALTER TABLE rz.concept_relationship
  DROP CONSTRAINT IF EXISTS concept_relationship_pk,
  DROP CONSTRAINT IF EXISTS concept_relationship_concept1_fk,
  DROP CONSTRAINT IF EXISTS concept_relationship_concept2_fk,
  DROP CONSTRAINT IF EXISTS concept_relationship_relationship_fk,
  DROP CONSTRAINT IF EXISTS concept_relationship_invalid_reason_check;
  
ALTER TABLE rz.concept_synonym
  DROP CONSTRAINT IF EXISTS concept_synonym_pk,
  DROP CONSTRAINT IF EXISTS concept_synonym_concept_fk;

ALTER TABLE rz.source_to_concept_map
  DROP CONSTRAINT IF EXISTS source_to_concept_map_pk,
  DROP CONSTRAINT IF EXISTS src_to_concept_map_target_concept_fk,
  DROP CONSTRAINT IF EXISTS src_to_concept_map_src_vocab_fk,
  DROP CONSTRAINT IF EXISTS src_to_concept_map_target_vocab_fk,
  DROP CONSTRAINT IF EXISTS src_to_concept_map_primary_map,
  DROP CONSTRAINT IF EXISTS src_to_concept_map_invalid_reason_check;
  
DROP INDEX IF EXISTS rz.src_to_con_map_src_code_idx;

DROP INDEX IF EXISTS rz.src_to_con_map_src_code2_idx;

DROP INDEX IF EXISTS rz.src_to_con_map_src_vocab_idx;

DROP INDEX IF EXISTS rz.src_to_con_map_map_type_idx;

ALTER TABLE rz.relationship
  DROP CONSTRAINT IF EXISTS relationship_pk;
  
ALTER TABLE rz.concept
  DROP CONSTRAINT IF EXISTS concept_pk,
  DROP CONSTRAINT IF EXISTS concept_vocabulary_ref_fk,
  DROP CONSTRAINT IF EXISTS concept_invalid_reason_check;

ALTER TABLE rz.vocabulary
  DROP CONSTRAINT IF EXISTS vocabulary_vocabulary_id_pk,
  DROP CONSTRAINT IF EXISTS vocabulary_vocabulary_name_uk;
