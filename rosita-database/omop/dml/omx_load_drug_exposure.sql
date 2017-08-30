-- Function: omop.omx_load_drug_exposure(bigint)

-- DROP FUNCTION omop.omx_load_drug_exposure(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_drug_exposure(p_step_id bigint)
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
	v_function_name := 'omx_load_drug_exposure';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;


	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;	

	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_drug_exposure', v_row_count, v_step_ct, 'START');

	begin
		truncate table omop.drug_exposure;
		--delete from omop.drug_exposure;
		--vacuum full omop.drug_exposure;
		
		get diagnostics v_row_count = row_count;
	end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate drug_exposure', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		perform setval('omop.omop_drug_exp_sq', 1, false);
		
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'reset omop_drug_exp_sq', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

	
	begin
		truncate cz.cz_wrk_id_map;
		get diagnostics v_row_count = row_count;
	end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate cz_wrk_id_map', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
	
	begin
		insert into cz.cz_wrk_id_map(source_value, x_data_source_id)
		select distinct drug_exposure_source_identifier,
		x_data_source_id
		from std.drug_exposure;

		update cz.cz_wrk_id_map
		set mapped_id = nextval('omop.omop_drug_exp_sq'::regclass);
		
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
		insert into omop.drug_exposure (
			drug_exposure_id,
			person_id,
			drug_concept_id,
			drug_exposure_start_date,
			drug_exposure_end_date,
			drug_type_concept_id,
			stop_reason,
			refills,
			quantity,
			days_supply,
			sig,
			prescribing_provider_id,
			visit_occurrence_id,
			relevant_condition_concept_id,
			drug_source_value,
			x_data_source_type,
			x_drug_name,
			x_drug_strength,
			x_grid_node_id,
			x_data_source_id
		)
		select distinct
			temp2.mapped_id,
			temp2.person_id,
			coalesce(m1x.target_concept_id, m1.target_concept_id) drug_concept_id,
			temp2.drug_exposure_start_date,
			temp2.drug_exposure_end_date,
			coalesce(m2x.target_concept_id, m2.target_concept_id) drug_type_concept_id,
			temp2.stop_reason stop_reason,  
			temp2.refills,
			temp2.quantity,
			temp2.days_supply,
			temp2.sig sig,
			temp2.prescribing_provider_id,
			temp2.visit_occurrence_id visit_occurrence_id,
			coalesce(m3x.target_concept_id, m3.target_concept_id) relevant_condition_concept_id,
			temp2.drug_source_value drug_source_value,
			temp2.x_data_source_type,
			temp2.x_drug_name,
			temp2.x_drug_strength,
			temp2.x_grid_node_id,
			temp2.x_data_source_id
			from
			
			(select distinct
			temp1.mapped_id,
			temp1.person_id,
			--coalesce(m1x.target_concept_id, m1.target_concept_id) drug_concept_id,
			temp1.drug_exposure_start_date,
			temp1.drug_exposure_end_date,
			--coalesce(m2x.target_concept_id, m2.target_concept_id) drug_type_concept_id,
			temp1.stop_reason stop_reason,  
			temp1.refills,
			temp1.quantity,
			temp1.days_supply,
			temp1.sig sig,
			pv.provider_id prescribing_provider_id,
			v.visit_occurrence_id visit_occurrence_id,
			--coalesce(m3x.target_concept_id, m3.target_concept_id) relevant_condition_concept_id,
			temp1.drug_source_value,
			temp1.x_data_source_type,
			temp1.x_drug_name x_drug_name,
			temp1.x_drug_strength,
			temp1.x_grid_node_id,
			temp1.relevant_condition_source_value,
			temp1.drug_type_source_value,
			temp1.drug_source_value_vocabulary,
			temp1.x_data_source_id
			from (select distinct
					temp0.mapped_id,
					cmap.id person_id,
					--coalesce(m1x.target_concept_id, m1.target_concept_id) drug_concept_id,
					temp0.drug_exposure_start_date drug_exposure_start_date,
					temp0.drug_exposure_end_date,
					--coalesce(m2x.target_concept_id, m2.target_concept_id) drug_type_concept_id,
					temp0.stop_reason stop_reason,  
					temp0.refills  refills,
					temp0.quantity quantity,
					temp0.days_supply days_supply,
					temp0.sig sig,
					--pv.provider_id prescribing_provider_id,
					--v.visit_occurrence_id visit_occurrence_id,
					--coalesce(m3x.target_concept_id, m3.target_concept_id) relevant_condition_concept_id,
					temp0.drug_source_value drug_source_value,
					temp0.x_data_source_type x_data_source_type,
					temp0.x_drug_name x_drug_name,
					temp0.x_drug_strength x_drug_strength,
					temp0.relevant_condition_source_value,
					temp0.x_grid_node_id,
					temp0.drug_type_source_value,
					temp0.provider_source_value,
					temp0.x_visit_occurrence_source_identifier,
					temp0.drug_source_value_vocabulary,
					temp0.x_data_source_id
			from
			(select distinct
					idmap.mapped_id,
					--cmap.id person_id,
					--coalesce(m1x.target_concept_id, m1.target_concept_id) drug_concept_id,
					d.drug_exposure_start_date drug_exposure_start_date,
					case
						when d.drug_exposure_end_date is null then null
						else d.drug_exposure_end_date 
					end drug_exposure_end_date,
					--coalesce(m2x.target_concept_id, m2.target_concept_id) drug_type_concept_id,
					d.stop_reason stop_reason,  
					d.refills  refills,
					d.quantity quantity,
					d.days_supply days_supply,
					d.sig sig,
					--pv.provider_id prescribing_provider_id,
					--v.visit_occurrence_id visit_occurrence_id,
					--coalesce(m3x.target_concept_id, m3.target_concept_id) relevant_condition_concept_id,
					d.drug_source_value drug_source_value,
					d.x_data_source_type x_data_source_type,
					d.x_drug_name x_drug_name,
					d.x_drug_strength x_drug_strength,
					0 x_grid_node_id,
					d.x_data_source_id,
					d.drug_type_source_value,
					d.relevant_condition_source_value,
					d.person_source_value,
					d.provider_source_value,
					d.x_visit_occurrence_source_identifier,
					d.drug_source_value_vocabulary

				from
					(SELECT DISTINCT de.drug_exposure_source_identifier, de.x_data_source_type, de.person_source_value, 
								de.drug_source_value, de.drug_source_value_vocabulary, de.drug_exposure_start_date, 
								de.drug_exposure_end_date, de.drug_type_source_value, de.stop_reason, 
								de.refills, de.quantity, de.days_supply, de.x_drug_name, de.x_drug_strength, 
								de.sig, de.provider_source_value, de.x_visit_occurrence_source_identifier, 
								de.relevant_condition_source_value, de.x_data_source_id, de.x_etl_date, de.x_record_num
								FROM std.drug_exposure de INNER JOIN (select
									drug_exposure_source_identifier, x_data_source_id, 
									max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
									from std.drug_exposure
									group by
									drug_exposure_source_identifier, x_data_source_id) de_maxrec ON de.drug_exposure_source_identifier = de_maxrec.drug_exposure_source_identifier
									and de.x_data_source_id = de_maxrec.x_data_source_id
									and to_char(de.x_etl_date, 'YYYYMMDDHHMISS') || to_char(de.x_record_num, '0000000000') = de_maxrec.last_rec ) d     
					join cz.cz_wrk_id_map idmap
						on d.x_data_source_id=idmap.x_data_source_id
						and d.drug_exposure_source_identifier = idmap.source_value) temp0
					join cz.cz_id_map cmap on temp0.person_source_value = cmap.source_value 
						 and temp0.x_data_source_id = cmap.x_data_source_id
						 and cmap.id_type = 'person_id'
					inner join omop.person op
						  on  cmap.id = op.person_id) temp1
					left outer join omop.provider pv on temp1.provider_source_value = pv.provider_source_value
						 and temp1.x_data_source_id = pv.x_data_source_id
					left outer join omop.visit_occurrence v on temp1.x_visit_occurrence_source_identifier = v.x_visit_occurrence_source_identifier 
						 and temp1.x_data_source_id = v.x_data_source_id) temp2
					left outer join cz.cz_concept_map m1 on temp2.drug_source_value = m1.source_value and temp2.drug_source_value_vocabulary = m1.source_vocabulary
						and m1.target_table = 'drug_exposure' and m1.target_column = 'drug_concept_id'
						and m1.x_data_source_id = -1
					left outer join cz.cz_concept_map m1x on temp2.drug_source_value = m1x.source_value and temp2.drug_source_value_vocabulary = m1x.source_vocabulary
						and m1x.target_table = 'drug_exposure' and m1x.target_column = 'drug_concept_id'
						and m1x.x_data_source_id = temp2.x_data_source_id 
					left outer join cz.cz_concept_map m2 on temp2.drug_type_source_value = m2.source_value
						and m2.target_table = 'drug_exposure' and m2.target_column = 'drug_type_concept_id'
						and m2.x_data_source_id = -1
					left outer join cz.cz_concept_map m2x on temp2.drug_type_source_value = m2x.source_value
						and m2x.target_table = 'drug_exposure' and m2x.target_column = 'drug_type_concept_id'
						and m2x.x_data_source_id = temp2.x_data_source_id
					left outer join cz.cz_concept_map m3 on temp2.relevant_condition_source_value = m3.source_value
						and m3.target_table = 'drug_exposure' and m3.target_column = 'relevant_condition_concept_id'
						and m3.x_data_source_id = -1
					left outer join cz.cz_concept_map m3x on temp2.relevant_condition_source_value = m3x.source_value
						and m3x.target_table = 'drug_exposure' and m3x.target_column = 'relevant_condition_concept_id'
						and m3x.x_data_source_id = temp2.x_data_source_id
					where coalesce(m1x.target_concept_id, m1.target_concept_id, -99) >= 0
					  and coalesce(m2x.target_concept_id, m2.target_concept_id, -99) >= 0
					  and coalesce(m3x.target_concept_id, m3.target_concept_id, -99) >= 0;

		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
	end;
		
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert drug_exposure', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		select count(*) into v_row_count
		from  std.drug_exposure c 	 
		inner join cz.cz_id_map p 
			  on  c.person_source_value = p.source_value 
			  and c.x_data_source_id = p.x_data_source_id
			  and p.id_type = 'person_id'
		where not exists
			  (select 1 from omop.person op
			   where p.id = op.person_id);
		if v_row_count > 0 then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'drug_exposure records not loaded, person linked in cz.cz_id_map but no omop.person record', v_row_count, v_step_ct, 'WARNING');
			v_step_ct := v_step_ct + 1;
		end if;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_drug_exposure', v_row_count, v_step_ct, 'COMPLETE');
	return 0;

		
	begin
		
		truncate table omop.drug_cost;
		get diagnostics v_row_count = row_count;
	end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate drug_cost', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		perform setval('omop.omop_drug_cost_sq', 1, false);

		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'reset omop_drug_cost_sq', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
	
	
	
	/*
	if p_step_id is null or p_step_id < 1 then
		perform cz.czx_end_audit(v_step_id, v_job_status);
	end if;
	*/

	
	
	begin
		insert into omop.drug_cost (
			drug_exposure_id,
			paid_copay,
			paid_coinsurance,
			paid_toward_deductible,
			paid_by_payer,
			paid_by_coordination_benefits,
			total_out_of_pocket,
			total_paid,
			ingredient_cost,
			dispensing_fee,
			average_wholesale_price,
			payer_plan_period_id,
			x_grid_node_id,
			x_data_source_id,
			x_data_source_type
		)
		SELECT DISTINCT
		temp2.mapped_id id,
		temp2.paid_copay paid_copay,
		temp2.paid_coinsurance paid_coinsurance,
		temp2.paid_toward_deductible paid_toward_deductible,
		temp2.paid_by_payer paid_by_payer,
		temp2.paid_by_coordination_benefits paid_by_coordination_benefits,
		temp2.total_out_of_pocket total_out_of_pocket,
		temp2.total_paid total_paid,
		temp2.ingredient_cost ingredient_cost,
		temp2.dispensing_fee dispensing_fee,
		temp2.average_wholesale_price average_wholesale_price,
		d.payer_plan_period_id payer_plan_period_id,
		temp2.x_grid_node_id,
		temp2.x_data_source_id x_data_source_id,
		temp2.x_data_source_type x_data_source_type
		FROM
		(SELECT DISTINCT temp1.paid_copay paid_copay,
			temp1.paid_coinsurance paid_coinsurance,
			temp1.paid_toward_deductible paid_toward_deductible,
			temp1.paid_by_payer paid_by_payer,
			temp1.paid_by_coordination_benefits paid_by_coordination_benefits,
			temp1.total_out_of_pocket total_out_of_pocket,
			temp1.total_paid total_paid,
			temp1.ingredient_cost ingredient_cost,
			temp1.dispensing_fee dispensing_fee,
			temp1.average_wholesale_price average_wholesale_price,
			0 x_grid_node_id,
			temp1.x_data_source_id x_data_source_id,
			temp1.x_data_source_type x_data_source_type,
			temp1.payer_plan_period_source_identifier,
			temp1.drug_exposure_end_date,
			temp1.drug_exposure_start_date,
			temp1.mapped_id,
			c.id id
		FROM
			(Select DISTINCT idmap.mapped_id drug_exposure_id,
				a.paid_copay paid_copay,
				a.paid_coinsurance paid_coinsurance,
				a.paid_toward_deductible paid_toward_deductible,
				a.paid_by_payer paid_by_payer,
				a.paid_by_coordination_benefits paid_by_coordination_benefits,
				a.total_out_of_pocket total_out_of_pocket,
				a.total_paid total_paid,
				a.ingredient_cost ingredient_cost,
				a.dispensing_fee dispensing_fee,
				a.average_wholesale_price average_wholesale_price,
				0 x_grid_node_id,
				a.x_data_source_id x_data_source_id,
				b.person_source_value person_source_value,
				a.x_data_source_type x_data_source_type,
				a.payer_plan_period_source_identifier payer_plan_period_source_identifier,
				b.drug_exposure_end_date drug_exposure_end_date,
				b.drug_exposure_start_date drug_exposure_start_date,
				idmap.mapped_id mapped_id
			FROM
			(
				(SELECT DISTINCT dc.drug_cost_source_identifier, dc.x_data_source_type, dc.drug_exposure_source_identifier, 
					dc.paid_copay, dc.paid_coinsurance, dc.paid_toward_deductible, dc.paid_by_payer, 
					dc.paid_by_coordination_benefits, dc.total_out_of_pocket, dc.total_paid, 
					dc.ingredient_cost, dc.dispensing_fee, dc.average_wholesale_price, dc.payer_plan_period_source_identifier, 
					dc.x_data_source_id, dc.x_etl_date,dc.x_record_num
					FROM std.drug_cost dc INNER JOIN (select
						drug_cost_source_identifier, x_data_source_id, 
						max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
						from std.drug_cost
						group by
						drug_cost_source_identifier, x_data_source_id) dc_maxrec ON dc.drug_cost_source_identifier = dc_maxrec.drug_cost_source_identifier
							and dc.x_data_source_id = dc_maxrec.x_data_source_id
							and to_char(dc.x_etl_date, 'YYYYMMDDHHMISS') || to_char(dc.x_record_num, '0000000000') = dc_maxrec.last_rec ) a 
			
				INNER JOIN 
					(SELECT DISTINCT de.drug_exposure_source_identifier, de.x_data_source_type, de.person_source_value, 
						de.drug_source_value, de.drug_source_value_vocabulary, de.drug_exposure_start_date, 
						de.drug_exposure_end_date, de.drug_type_source_value, de.stop_reason, 
						de.refills, de.quantity, de.days_supply, de.x_drug_name, de.x_drug_strength, 
						de.sig, de.provider_source_value, de.x_visit_occurrence_source_identifier, 
						de.relevant_condition_source_value, de.x_data_source_id, de.x_etl_date, de.x_record_num
						FROM std.drug_exposure de INNER JOIN (select
							drug_exposure_source_identifier, x_data_source_id, 
							max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
							from std.drug_exposure
							group by
							drug_exposure_source_identifier, x_data_source_id) de_maxrec ON de.drug_exposure_source_identifier = de_maxrec.drug_exposure_source_identifier
							and de.x_data_source_id = de_maxrec.x_data_source_id
							and to_char(de.x_etl_date, 'YYYYMMDDHHMISS') || to_char(de.x_record_num, '0000000000') = de_maxrec.last_rec ) b
				ON  b.drug_exposure_source_identifier = a.drug_exposure_source_identifier 
				and a.x_data_source_id = b.x_data_source_id
				
				inner join cz.cz_wrk_id_map idmap
				on b.x_data_source_id = idmap.x_data_source_id
				and b.drug_exposure_source_identifier = idmap.source_value
			)) temp1
			INNER JOIN cz.cz_id_map c on c.x_data_source_id = temp1.x_data_source_id 
							 and c.source_value = temp1.person_source_value
							 and c.id_type = 'person_id') temp2
			inner join omop.person op
							  on  temp2.id = op.person_id
			left outer Join omop.payer_plan_period d on temp2.payer_plan_period_source_identifier = d.payer_source_value and	 
							 temp2.id = d.person_id and
							 temp2.drug_exposure_start_date >= d.payer_plan_period_start_date and
							 temp2.drug_exposure_end_date <= d.payer_plan_period_end_date
						where exists (select 1 from omop.drug_exposure x where x.drug_exposure_id = temp2.mapped_id);
		
		
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_drug_exposure', v_row_count, v_step_ct, 'COMPLETE');
		return 0;
	
	


	end;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_drug_exposure(bigint)
  OWNER TO rosita;