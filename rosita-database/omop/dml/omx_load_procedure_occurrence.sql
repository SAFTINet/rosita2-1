-- Function: omop.omx_load_procedure_occurrence(bigint)

-- DROP FUNCTION omop.omx_load_procedure_occurrence(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_procedure_occurrence(p_step_id bigint)
  RETURNS bigint AS
$BODY$
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
declare
	v_database_name varchar(50);
	v_function_name varchar(50);
	v_step_id bigint;
	v_job_status varchar(50);
	v_step_ct int;
	v_row_count bigint;
begin
	v_database_name := 'omop';
	v_function_name := 'omx_load_procedure_occurrence';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		


	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;		

	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_procedure_occurrence', v_row_count, v_step_ct, 'START');

	begin
		truncate table omop.procedure_occurrence;
		get diagnostics v_row_count = row_count;
	end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate procedure_occurrence', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		perform setval('omop.omop_proc_occur_sq', 1, false);
		
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'reset omop_proc_occur_sq', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		
	begin
		truncate cz.cz_wrk_id_map;
		get diagnostics v_row_count = row_count;
	end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate cz_wrk_id_map', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
	begin

		insert into cz.cz_wrk_id_map(source_value, x_data_source_id)
		select distinct procedure_occurrence_source_identifier,
		x_data_source_id
		from std.procedure_occurrence;

		update cz.cz_wrk_id_map
		set mapped_id = nextval('omop.omop_proc_occur_sq'::regclass);

		get diagnostics v_row_count = row_count;
		
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
	end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_wrk_id_map', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		
	begin
		
		insert into omop.procedure_occurrence (
			procedure_occurrence_id,
			person_id,
			procedure_concept_id,
			procedure_date,
			procedure_type_concept_id,
			associated_provider_id,
			visit_occurrence_id,
			relevant_condition_concept_id,
			procedure_source_value,
			x_data_source_type,
			x_grid_node_id,
			x_data_source_id
		)
		select distinct
			temp2.mapped_id,
			temp2.person_id,
			coalesce(m1x.target_concept_id, m1.target_concept_id) procedure_concept_id, 
			temp2.procedure_date procedure_date,
			coalesce(m2x.target_concept_id, m2.target_concept_id) procedure_type_concept_id,
			temp2.associated_provider_id,
			temp2.visit_occurrence_id,
			coalesce(m3x.target_concept_id, m3.target_concept_id) relevant_condition_concept_id,  
			temp2.procedure_source_value,
			temp2.x_data_source_type,
			temp2.x_grid_node_id,
			temp2.x_data_source_id
		from
		(select distinct
			temp1.mapped_id,
			temp1.person_id,
			--coalesce(m1x.target_concept_id, m1.target_concept_id) procedure_concept_id, 
			temp1.procedure_date procedure_date,
			--coalesce(m2x.target_concept_id, m2.target_concept_id) procedure_type_concept_id,
			pv.provider_id associated_provider_id,
			v.visit_occurrence_id visit_occurrence_id,
			--coalesce(m3x.target_concept_id, m3.target_concept_id) relevant_condition_concept_id,  
			temp1.procedure_source_value,
			temp1.x_data_source_type,
			temp1.x_grid_node_id,
			temp1.procedure_type_source_value,
			temp1.relevant_condition_source_value,
			temp1.procedure_source_value_vocabulary,
			temp1.x_data_source_id
		from
		(select distinct
			temp0.mapped_id,
			p.id person_id,
			--coalesce(m1x.target_concept_id, m1.target_concept_id) procedure_concept_id, 
			temp0.procedure_date procedure_date,
			--coalesce(m2x.target_concept_id, m2.target_concept_id) procedure_type_concept_id,
			--pv.provider_id associated_provider_id,
			--v.visit_occurrence_id visit_occurrence_id,
			--coalesce(m3x.target_concept_id, m3.target_concept_id) relevant_condition_concept_id,  
			temp0.procedure_source_value,
			temp0.x_data_source_type,
			temp0.x_grid_node_id,
			temp0.x_visit_occurrence_source_identifier,
			temp0.provider_record_source_value,
			temp0.relevant_condition_source_value,
			temp0.procedure_type_source_value,
			temp0.procedure_source_value_vocabulary,
			temp0.x_data_source_id
		from
		(select distinct
			idmap.mapped_id,
			--p.id person_id,
			--coalesce(m1x.target_concept_id, m1.target_concept_id) procedure_concept_id, 
			po.procedure_date procedure_date,
			--coalesce(m2x.target_concept_id, m2.target_concept_id) procedure_type_concept_id,
			--pv.provider_id associated_provider_id,
			--v.visit_occurrence_id visit_occurrence_id,
			--coalesce(m3x.target_concept_id, m3.target_concept_id) relevant_condition_concept_id,  
			po.procedure_source_value procedure_source_value,
			po.x_data_source_type x_data_source_type,
			0 x_grid_node_id,
			po.person_source_value,
			po.provider_record_source_value,
			po.x_visit_occurrence_source_identifier,
			po.procedure_source_value_vocabulary,
			po.procedure_type_source_value,
			po.relevant_condition_source_value,
			po.x_data_source_id

		from
			(SELECT DISTINCT po1.procedure_occurrence_source_identifier, po1.x_data_source_type, po1.person_source_value, 
				po1.procedure_source_value, po1.procedure_source_value_vocabulary, po1.procedure_date, 
				po1.procedure_type_source_value, po1.provider_record_source_value, po1.x_visit_occurrence_source_identifier, 
				po1.relevant_condition_source_value, po1.x_data_source_id, po1.x_etl_date
				FROM std.procedure_occurrence po1 INNER JOIN 
						(select procedure_occurrence_source_identifier, x_data_source_id, 
						max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
						from std.procedure_occurrence
						group by procedure_occurrence_source_identifier, x_data_source_id) po_maxrec ON po1.procedure_occurrence_source_identifier = po_maxrec.procedure_occurrence_source_identifier
						and po1.x_data_source_id = po_maxrec.x_data_source_id
						and to_char(po1.x_etl_date, 'YYYYMMDDHHMISS') || to_char(po1.x_record_num, '0000000000') = po_maxrec.last_rec) po
			join cz.cz_wrk_id_map idmap
				on po.x_data_source_id=idmap.x_data_source_id
				and po.procedure_occurrence_source_identifier = idmap.source_value) temp0
			inner join cz.cz_id_map p on temp0.person_source_value = p.source_value
				  and temp0.x_data_source_id = p.x_data_source_id
				  and p.id_type = 'person_id' 
			inner join omop.person op
				  on  p.id = op.person_id) temp1
			left outer join omop.provider pv on temp1.provider_record_source_value = pv.provider_source_value
				  and temp1.x_data_source_id = pv.x_data_source_id
			left outer join omop.visit_occurrence v on temp1.x_visit_occurrence_source_identifier = v.x_visit_occurrence_source_identifier
				  and temp1.x_data_source_id = v.x_data_source_id) temp2
			left outer join cz.cz_concept_map m1 on temp2.procedure_source_value = m1.source_value and temp2.procedure_source_value_vocabulary = m1.source_vocabulary
				and m1.target_table = 'procedure_occurrence' and m1.target_column = 'procedure_concept_id'
				and m1.x_data_source_id = -1
			left outer join cz.cz_concept_map m1x on temp2.procedure_source_value = m1x.source_value and temp2.procedure_source_value_vocabulary = m1x.source_vocabulary
				and m1x.target_table = 'procedure_occurrence' and m1x.target_column = 'procedure_concept_id'
				and m1x.x_data_source_id = temp2.x_data_source_id
			left outer join cz.cz_concept_map m2 on temp2.procedure_type_source_value = m2.source_value
				and m2.target_table = 'procedure_occurrence' and m2.target_column = 'procedure_type_concept_id'
				and m2.x_data_source_id = -1
			left outer join cz.cz_concept_map m2x on temp2.procedure_type_source_value = m2x.source_value
				and m2x.target_table = 'procedure_occurrence' and m2x.target_column = 'procedure_type_concept_id'
				and m2x.x_data_source_id = temp2.x_data_source_id
			left outer join cz.cz_concept_map m3 on temp2.relevant_condition_source_value = m3.source_value
				and m3.target_table = 'procedure_occurrence' and m3.target_column = 'relevant_condition_concept_id'
				and m3.x_data_source_id = -1
			left outer join cz.cz_concept_map m3x on temp2.relevant_condition_source_value = m3x.source_value
				and m3x.target_table = 'procedure_occurrence' and m3x.target_column = 'relevant_condition_concept_id'
				and m3x.x_data_source_id = temp2.x_data_source_id;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
	end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert procedure_occurrence', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;	
	
	select count(*) into v_row_count
	from  std.procedure_occurrence c 	 
	inner join cz.cz_id_map p 
		  on  c.person_source_value = p.source_value 
		  and c.x_data_source_id = p.x_data_source_id
		  and p.id_type = 'person_id'
	where not exists
		  (select 1 from omop.person op
		   where p.id = op.person_id);
	if v_row_count > 0 then
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'procedure_occurrence records not loaded, person linked in cz.cz_id_map but no omop.person record', v_row_count, v_step_ct, 'WARNING');
		v_step_ct := v_step_ct + 1;
	end if;

	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_procedure_occurrence', v_row_count, v_step_ct, 'COMPLETE');
	return 0;

		
	begin
		truncate table omop.procedure_cost;
		get diagnostics v_row_count = row_count;
	end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate procedure_cost', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		perform setval('omop.omop_proc_cost_sq', 1, false);

		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'reset omop_procedure_cost_sq', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

	begin
		insert into omop.procedure_cost (
			procedure_occurrence_id,
			paid_copay,
			paid_coinsurance,
			paid_toward_deductible,
			paid_by_payer,
			paid_by_coordination_benefits,
			total_out_of_pocket,
			total_paid,
			disease_class_concept_id,
			revenue_code_concept_id,
			payer_plan_period_id,
			disease_class_source_value,
			revenue_code_source_value,
			x_grid_node_id,
			x_data_source_id,
			x_data_source_type
		)
		Select distinct 
		temp3.mapped_id procedure_occurrence_id,
		temp3.paid_copay paid_copay,
		temp3.paid_coinsurance paid_coinsurance,
		temp3.paid_toward_deductible paid_toward_deductible,
		temp3.paid_by_payer paid_by_payer,
		temp3.paid_by_coordination_benefits paid_by_coordination_benefits,
		temp3.total_out_of_pocket total_out_of_pocket,
		temp3.total_paid total_paid,
		coalesce(dcx.target_concept_id,dc.target_concept_id) disease_class_concept_id,
		coalesce(rcx.target_concept_id,rc.target_concept_id) revenue_code_concept_id,
		d.payer_plan_period_id payer_plan_period_id,
		temp3.disease_class_source_value disease_class_source_value,
		temp3.revenue_code_source_value revenue_code_source_value,
		temp3.x_grid_node_id x_grid_node_id,
		temp3.x_data_source_id x_data_source_id,
		temp3.x_data_source_type,
		temp3.person_source_value
			from
			(select DISTINCT
			temp2.paid_copay paid_copay,
			temp2.paid_coinsurance paid_coinsurance,
			temp2.paid_toward_deductible paid_toward_deductible,
			temp2.paid_by_payer paid_by_payer,
			temp2.paid_by_coordination_benefits paid_by_coordination_benefits,
			temp2.total_out_of_pocket total_out_of_pocket,
			temp2.total_paid total_paid,
			temp2.disease_class_source_value disease_class_source_value,
			temp2.revenue_code_source_value revenue_code_source_value,
			temp2.x_grid_node_id x_grid_node_id,
			temp2.x_data_source_id x_data_source_id,
			temp2.x_data_source_type,
			temp2.person_source_value,
			temp2.payer_plan_period_source_identifier,
			temp2.procedure_date,
			temp2.mapped_id,
			c.id id
				FROM
				(select DISTINCT
				temp1.paid_copay paid_copay,
				temp1.paid_coinsurance paid_coinsurance,
				temp1.paid_toward_deductible paid_toward_deductible,
				temp1.paid_by_payer paid_by_payer,
				temp1.paid_by_coordination_benefits paid_by_coordination_benefits,
				temp1.total_out_of_pocket total_out_of_pocket,
				temp1.total_paid total_paid,
				temp1.disease_class_source_value disease_class_source_value,
				temp1.revenue_code_source_value revenue_code_source_value,
				temp1.x_grid_node_id x_grid_node_id,
				temp1.x_data_source_id x_data_source_id,
				temp1.x_data_source_type,
				temp1.person_source_value,
				temp1.procedure_date,
				temp1.payer_plan_period_source_identifier,
				idmap.mapped_id
				FROM
					(select distinct
						a.procedure_occurrence_source_identifier,
						a.paid_copay paid_copay,
						a.paid_coinsurance paid_coinsurance,
						a.paid_toward_deductible paid_toward_deductible,
						a.paid_by_payer paid_by_payer,
						a.paid_by_coordination_benefits paid_by_coordination_benefits,
						a.total_out_of_pocket total_out_of_pocket,
						a.total_paid total_paid,
						a.disease_class_source_value disease_class_source_value,
						a.revenue_code_source_value revenue_code_source_value,
						0 x_grid_node_id,
						a.x_data_source_id x_data_source_id,
						a.x_data_source_type,
						a.payer_plan_period_source_identifier,
						b.person_source_value,
						b.procedure_date
						from 
						(SELECT DISTINCT pc.procedure_cost_source_identifier, pc.x_data_source_type, pc.procedure_occurrence_source_identifier, 
						pc.paid_copay, pc.paid_coinsurance, pc.paid_toward_deductible, pc.paid_by_payer, 
						pc.paid_by_coordination_benefits, pc.total_out_of_pocket, pc.total_paid, 
						pc.disease_class_source_value, pc.disease_class_concept_id, pc.revenue_code_source_value, 
						pc.revenue_code_concept_id, pc.payer_plan_period_source_identifier, 
						pc.x_data_source_id, pc.x_etl_date
						FROM std.procedure_cost pc INNER JOIN 
									(select procedure_cost_source_identifier, x_data_source_id, 
									max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
									from std.procedure_cost
									group by procedure_cost_source_identifier, x_data_source_id) pc_maxrec ON pc.procedure_cost_source_identifier = pc_maxrec.procedure_cost_source_identifier
									and pc.x_data_source_id = pc_maxrec.x_data_source_id
									and to_char(pc.x_etl_date, 'YYYYMMDDHHMISS') || to_char(pc.x_record_num, '0000000000') = pc_maxrec.last_rec) a
						inner join 

						(SELECT DISTINCT po1.procedure_occurrence_source_identifier, po1.x_data_source_type, po1.person_source_value, 
							po1.procedure_source_value, po1.procedure_source_value_vocabulary, po1.procedure_date, 
							po1.procedure_type_source_value, po1.provider_record_source_value, po1.x_visit_occurrence_source_identifier, 
							po1.relevant_condition_source_value, po1.x_data_source_id, po1.x_etl_date
							FROM std.procedure_occurrence po1 INNER JOIN 
									(select procedure_occurrence_source_identifier, x_data_source_id, 
									max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
									from std.procedure_occurrence
									group by procedure_occurrence_source_identifier, x_data_source_id) po_maxrec ON po1.procedure_occurrence_source_identifier = po_maxrec.procedure_occurrence_source_identifier
									and po1.x_data_source_id = po_maxrec.x_data_source_id
									and to_char(po1.x_etl_date, 'YYYYMMDDHHMISS') || to_char(po1.x_record_num, '0000000000') = po_maxrec.last_rec) b
						
							  on  b.procedure_occurrence_source_identifier = a.procedure_occurrence_source_identifier 
							  and a.x_data_source_id = b.x_data_source_id) temp1
			INNER JOIN cz.cz_wrk_id_map idmap
				on temp1.x_data_source_id = idmap.x_data_source_id
				and temp1.procedure_occurrence_source_identifier = idmap.source_value) temp2
			INNER JOIN cz.cz_id_map c	
				on  temp2.person_source_value = c.source_value
				and c.x_data_source_id = temp2.x_data_source_id 
				and c.id_type = 'person_id') temp3
			INNER JOIN omop.person op
				  on  temp3.id = op.person_id
			left outer Join omop.payer_plan_period d on temp3.payer_plan_period_source_identifier = d.payer_source_value and	 
				 temp3.id = d.person_id and
				 temp3.procedure_date >= d.payer_plan_period_start_date and
				 temp3.procedure_date <= d.payer_plan_period_end_date
			left outer join cz.cz_concept_map dc on temp3.disease_class_source_value = dc.source_value 
				--and po.procedure_source_value_vocabulary = m1.source_vocabulary
				and dc.target_table = 'procedure_cost' and dc.target_column = 'disease_class_concept_id'
				and dc.x_data_source_id = -1
			left outer join cz.cz_concept_map dcx on temp3.disease_class_source_value = dcx.source_value 
				--and po.procedure_source_value_vocabulary = m1.source_vocabulary
				and dcx.target_table = 'procedure_cost' and dcx.target_column = 'disease_class_concept_id'
				and dcx.x_data_source_id = temp3.x_data_source_id
			left outer join cz.cz_concept_map rc on temp3.revenue_code_source_value = rc.source_value 
				--and po.procedure_source_value_vocabulary = m1.source_vocabulary
				and rc.target_table = 'procedure_cost' and rc.target_column = 'revenue_code_concept_id'
				and rc.x_data_source_id = -1
			left outer join cz.cz_concept_map rcx on temp3.revenue_code_source_value = rcx.source_value 
				--and po.procedure_source_value_vocabulary = m1.source_vocabulary
				and rcx.target_table = 'procedure_cost' and rcx.target_column = 'revenue_code_concept_id'
				and rcx.x_data_source_id = temp3.x_data_source_id
			where exists (select 1 from omop.procedure_occurrence x where x.procedure_occurrence_id = temp3.mapped_id);
		
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert procedure_cost', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
	select count(*) into v_row_count
	from  std.procedure_cost a 	 
	inner join std.procedure_occurrence b
		  on  b.procedure_occurrence_source_identifier = a.procedure_occurrence_source_identifier 
		  and a.x_data_source_id = b.x_data_source_id
	inner join cz.cz_id_map c	
		  on  b.person_source_value = c.source_value
		  and c.x_data_source_id = b.x_data_source_id 
		  and c.id_type = 'person_id'
	where not exists
		  (select 1 from omop.person op
		   where c.id = op.person_id);
	if v_row_count > 0 then
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'procedure_cost records not loaded, person linked in cz.cz_id_map but no omop.person record', v_row_count, v_step_ct, 'WARNING');
		v_step_ct := v_step_ct + 1;
	end if;

	/*
	if p_step_id is null or p_step_id < 1 then
		perform cz.czx_end_audit(v_step_id, v_job_status);
	end if;
	*/


end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_procedure_occurrence(bigint)
  OWNER TO rosita;