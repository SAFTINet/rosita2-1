
ALTER TABLE rz.vocabulary
  ADD CONSTRAINT vocabulary_vocabulary_id_pk PRIMARY KEY (vocabulary_id ),
  ADD CONSTRAINT vocabulary_vocabulary_name_uk UNIQUE (vocabulary_name );

ALTER TABLE rz.concept
  ADD CONSTRAINT concept_pk PRIMARY KEY (concept_id ),
  ADD CONSTRAINT concept_vocabulary_ref_fk FOREIGN KEY (vocabulary_id)
      REFERENCES rz.vocabulary (vocabulary_id) MATCH FULL
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  ADD CONSTRAINT concept_invalid_reason_check CHECK (invalid_reason = 'D'::bpchar OR invalid_reason = 'U'::bpchar);

ALTER TABLE rz.concept_ancestor
  ADD CONSTRAINT concept_ancestor_pk PRIMARY KEY (ancestor_concept_id , descendant_concept_id ),
  ADD CONSTRAINT concept_ancestor_ancestor_concept_fk FOREIGN KEY (ancestor_concept_id)
      REFERENCES rz.concept (concept_id) MATCH FULL
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  ADD CONSTRAINT concept_ancestor_descendant_concept_fk FOREIGN KEY (descendant_concept_id)
      REFERENCES rz.concept (concept_id) MATCH FULL
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE rz.relationship
  ADD CONSTRAINT relationship_pk PRIMARY KEY (relationship_id );

ALTER TABLE rz.concept_relationship
  ADD CONSTRAINT concept_relationship_pk PRIMARY KEY (concept_id_1, concept_id_2, relationship_id),
  ADD CONSTRAINT concept_relationship_concept1_fk FOREIGN KEY (concept_id_1)
      REFERENCES rz.concept (concept_id) MATCH FULL
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  ADD CONSTRAINT concept_relationship_concept2_fk FOREIGN KEY (concept_id_2)
      REFERENCES rz.concept (concept_id) MATCH FULL
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  ADD CONSTRAINT concept_relationship_relationship_fk FOREIGN KEY (relationship_id)
      REFERENCES rz.relationship (relationship_id) MATCH FULL
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  ADD CONSTRAINT concept_relationship_invalid_reason_check CHECK (invalid_reason = 'D'::bpchar OR invalid_reason = 'U'::bpchar);

ALTER TABLE rz.concept_synonym
  ADD CONSTRAINT concept_synonym_pk PRIMARY KEY (concept_synonym_id ),
  ADD CONSTRAINT concept_synonym_concept_fk FOREIGN KEY (concept_id)
    REFERENCES rz.concept (concept_id) MATCH FULL
    ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE rz.source_to_concept_map
  ADD CONSTRAINT source_to_concept_map_pk PRIMARY KEY (x_data_source_id, source_vocabulary_id , target_concept_id , source_code , valid_end_date),
  ADD CONSTRAINT src_to_concept_map_target_concept_fk FOREIGN KEY (target_concept_id)
    REFERENCES rz.concept (concept_id) MATCH FULL
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  ADD CONSTRAINT src_to_concept_map_src_vocab_fk FOREIGN KEY (source_vocabulary_id)
    REFERENCES rz.vocabulary (vocabulary_id) MATCH FULL
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  ADD CONSTRAINT src_to_concept_map_target_vocab_fk FOREIGN KEY (target_vocabulary_id)
    REFERENCES rz.vocabulary (vocabulary_id) MATCH FULL
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  ADD CONSTRAINT src_to_concept_map_primary_map CHECK (invalid_reason = 'Y'::bpchar OR NULL),
  ADD CONSTRAINT src_to_concept_map_invalid_reason_check CHECK (invalid_reason = 'D'::bpchar OR invalid_reason = 'U'::bpchar);

CREATE INDEX src_to_con_map_src_code_idx
  ON rz.source_to_concept_map
  USING btree
  (x_data_source_id, source_code COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

CREATE INDEX src_to_con_map_src_code2_idx
  ON rz.source_to_concept_map
  USING btree
  (source_code COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

CREATE INDEX src_to_con_map_src_vocab_idx
  ON rz.source_to_concept_map
  USING btree
  (source_vocabulary_id )
TABLESPACE rosita_indx;

CREATE INDEX src_to_con_map_map_type_idx
  ON rz.source_to_concept_map
  USING btree
  (mapping_type COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

