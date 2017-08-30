-- Function: omop.omx_load_drug_cost(bigint)

-- DROP FUNCTION omop.omx_load_drug_cost(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_drug_cost(p_step_id bigint)
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
	v_function_name := 'omx_load_drug_cost';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;


	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;	

	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_drug_cost', v_row_count, v_step_ct, 'START');

		
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
			temp1.x_grid_node_id,
			temp1.x_data_source_id x_data_source_id,
			temp1.x_data_source_type x_data_source_type,
			temp1.payer_plan_period_source_identifier,
			temp1.drug_exposure_end_date,
			temp1.drug_exposure_start_date,
			temp1.mapped_id,
			c.id id
		FROM
			(Select DISTINCT idmap.mapped_id mapped_id,
				temp0.paid_copay paid_copay,
				temp0.paid_coinsurance paid_coinsurance,
				temp0.paid_toward_deductible paid_toward_deductible,
				temp0.paid_by_payer paid_by_payer,
				temp0.paid_by_coordination_benefits paid_by_coordination_benefits,
				temp0.total_out_of_pocket total_out_of_pocket,
				temp0.total_paid total_paid,
				temp0.ingredient_cost ingredient_cost,
				temp0.dispensing_fee dispensing_fee,
				temp0.average_wholesale_price average_wholesale_price,
				temp0.x_grid_node_id,
				temp0.x_data_source_id x_data_source_id,
				temp0.person_source_value person_source_value,
				temp0.x_data_source_type x_data_source_type,
				temp0.payer_plan_period_source_identifier payer_plan_period_source_identifier,
				temp0.drug_exposure_end_date drug_exposure_end_date,
				temp0.drug_exposure_start_date drug_exposure_start_date
				FROM
				(Select DISTINCT 
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
					a.drug_exposure_source_identifier
				FROM
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
					and a.x_data_source_id = b.x_data_source_id) temp0
				
				inner join cz.cz_wrk_id_map idmap
				on temp0.x_data_source_id = idmap.x_data_source_id
				and temp0.drug_exposure_source_identifier = idmap.source_value
			) temp1
			INNER JOIN cz.cz_id_map c on c.x_data_source_id = temp1.x_data_source_id 
							 and c.source_value = temp1.person_source_value
							 and c.id_type = 'person_id') temp2
			inner join omop.person op
							  on  temp2.id = op.person_id
			left outer Join omop.payer_plan_period d on temp2.payer_plan_period_source_identifier = d.payer_source_value and	 
							 temp2.id = d.person_id and
							 temp2.drug_exposure_start_date >= d.payer_plan_period_start_date and
							 temp2.drug_exposure_end_date <= d.payer_plan_period_end_date
			left join omop.drug_exposure x on x.drug_exposure_id = temp2.mapped_id where temp2.mapped_id is not null;
		
		
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_drug_cost', v_row_count, v_step_ct, 'COMPLETE');
		return 0;
	
	


	end;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_drug_cost(bigint)
  OWNER TO rosita;