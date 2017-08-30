-- Function: cz.czx_linkage_preprocess(bigint, character varying)

DROP FUNCTION IF EXISTS cz.czx_linkage_preprocess(bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION cz.czx_linkage_preprocess(p_step_id bigint, p_claims_data_source_id bigint, p_linkage_cols character varying)
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
	
	v_linkage_cols		varchar(2000);
	v_linkage_cols_array varchar(100)[];
	v_linkage_cols_sel	varchar(2000);
	v_array_pos			int;
	v_sql				text;
	v_exists			int;
	v_rtn_cd			bigint;
	
begin
	v_database_name := 'cz';
	v_function_name := 'czx_linkage_preprocess';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;
	v_rtn_cd := 0;

	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;	
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start '||v_function_name || ' claim source: ' || p_claims_data_source_id::text, v_row_count, v_step_ct, 'START');
	
	--	validate p_claims_data_source_id, -1 = no claims data for this run, > 0 claims data
	
	if p_claims_data_source_id > -1 then
		
		select count(*) into v_exists
		from cz.cz_data_source
		where x_data_source_id = p_claims_data_source_id
		  and data_source_type = 'CLAIMS';
		if v_exists = 0 then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'p_claims_data_source_id parameter is not a claims source', v_row_count, v_step_ct, 'ERROR');
			return 16;
		end if;
		  
		select count(*) into v_exists
		from std.x_demographic
		where x_data_source_id = p_claims_data_source_id;
		if v_exists = 0 then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'No data in std.x_demographic for p_claims_data_source_id parameter', v_row_count, v_step_ct, 'ERROR');
			return 16;
		end if;		
	end if;
	
	--	insert missing clinical records into cz.cz_id_map
	
	begin
	insert into cz.cz_id_map
	(x_data_source_id
	,source_value
	,id_type
	)
	select distinct d.x_data_source_id
		  ,d.person_source_value
		  ,'person_id'
	from std.x_demographic d
		,cz.cz_data_source ds
	where d.x_data_source_id = ds.x_data_source_id
	  and ds.data_source_type = 'CLINICAL'
	  and not exists
		  (select 1 from cz.cz_id_map x
		   where d.person_source_value = x.source_value
			 and d.x_data_source_id = x.x_data_source_id
			 and x.id_type = 'person_id');
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Insert clinical records to cz.cz_id_map', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;
	
	--	end run if only clinical data to be processed
	
	if p_claims_data_source_id = -1 then
		insert into cz.linkage_log
		(person_source_value
		,x_data_source_id
		,log_type
		,log_message)
		values('-1',-1,'SUCCESS','No claims data source specified, only clinical data processed');
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end '|| v_function_name, 0, v_step_ct, 'COMPLETE');
		return 0;
	end if;
	
	--	check if linkage cols missing if claims are to be preprocessed
	
	if p_linkage_cols is null or p_linkage_cols = '' then
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'No linkage cols specified', v_row_count, v_step_ct, 'ERROR');
		return 16;
	end if;
	
	v_linkage_cols := trim(both ',' from p_linkage_cols);
	v_linkage_cols_array := string_to_array(replace(v_linkage_cols,' ',''),',');

	--	validate linkage cols against table columns
	
	if coalesce(array_length(v_linkage_cols_array,1),0) > 0 then
		v_array_pos := 1;
		while v_array_pos <= array_length(v_linkage_cols_array,1)
		loop
			select count(*) into v_exists
			from information_schema.columns
			where table_schema = 'std'
			  and table_name   = 'x_demographic'
			  and column_name = v_linkage_cols_array[v_array_pos];
			  
			if v_exists = 0 then
				perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Invalid linkage column: '||v_linkage_cols_array[v_array_pos] , v_row_count, v_step_ct, 'ERROR');	
				return 16;
			end if;
			
			v_array_pos := v_array_pos + 1;
		end loop;
	end if;

	--	truncate linkage tables

	begin
	truncate table cz.linkage_source_a;
	get diagnostics v_row_count = row_count;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate linkage_source_a', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;
	
/*		this will be done by the application because czx_linkage_preprocess will be called one time for each claims source
	
	begin
	truncate table cz.linkage_log;
	get diagnostics v_row_count = row_count;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate cz_linkage_log', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;
*/

	--	truncate linkage_source_b
	begin
	truncate table cz.linkage_source_b;
	get diagnostics v_row_count = row_count;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate linkage_source_b', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;
		
	drop table if exists cz.linkage_wrk1;
	
	v_sql := 'create table cz.linkage_wrk1 as select d.person_source_value, d.x_data_source_id, d.x_record_num' ;
	v_sql := v_sql || ',cz_id_map.id, d.x_etl_date, 0 as x_log_type, upper(ds.data_source_type) as data_source_type'; 
	if coalesce(array_length(v_linkage_cols_array,1),0) > 0 then
		v_array_pos := 1;
		while v_array_pos <= array_length(v_linkage_cols_array,1)
		loop
			v_sql := v_sql || ', d.' || v_linkage_cols_array[v_array_pos];
			v_array_pos := v_array_pos + 1;
		end loop;
	end if;
	v_sql := v_sql || ' from std.x_demographic d left outer join cz.cz_id_map on d.person_source_value = cz_id_map.source_value';
	v_sql := v_sql || ' and d.x_data_source_id = cz_id_map.x_data_source_id and cz_id_map.id_type = ' || '''' || 'person_id' || '''';
	v_sql := v_sql || ' inner join cz.cz_data_source ds on d.x_data_source_id = ds.x_data_source_id';
	v_sql := v_sql || ' where d.x_data_source_id = ' || p_claims_data_source_id;
	v_sql := v_sql || ' or upper(ds.data_source_type) = ' || '''' || 'CLINICAL' || '''';
	-- raise notice 'v_sql = %', v_sql;
	begin
	execute v_sql;
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert clinical records into work table', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;	
	
	create index linkage_wrk1_idx1 on cz.linkage_wrk1 (person_source_value, x_data_source_id);			

	--	set x_log_type to 1 for not latest record

	begin
	with upd as (select t.person_source_value
					   ,t.x_data_source_id
					   ,t.x_record_num
					   ,t.x_etl_date
				 from cz.linkage_wrk1 t
				 where to_char(t.x_etl_date,'YYYYMMDDHHMISS') || to_char(t.x_record_num,'0000000') <
					  (select max(to_char(x.x_etl_date,'YYYYMMDDHHMISS') || to_char(x.x_record_num,'0000000')) 
					   from cz.linkage_wrk1 x
					   where t.person_source_value = x.person_source_value
					     and t.x_data_source_id = x.x_data_source_id)
				)
	update cz.linkage_wrk1 w
	set x_log_type=1
	from upd
	where w.person_source_value = upd.person_source_value 
	  and w.x_data_source_id = upd.x_data_source_id 
	  and w.x_record_num = upd.x_record_num
	  and w.x_etl_date = upd.x_etl_date;
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Set indicator for not latest record', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;		

	if p_claims_data_source_id > -1 then 
		--	set x_log_type to 10 for claims already mapped 
		begin
		update cz.linkage_wrk1 w
		set x_log_type = 10 
		where w.id is not null
		  and w.data_source_type = 'CLAIMS';
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Set indicator for already mapped data', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;	
	
		--	set x_log_type to 15 for claims record in linkage_adjustment 
		begin
		with upd as (select distinct person_source_value, x_data_source_id from cz.linkage_adjustment)
		update cz.linkage_wrk1 w
		set x_log_type = 15
		from upd
		where w.person_source_value = upd.person_source_value
		  and w.x_data_source_id = upd.x_data_source_id
		  and w.x_log_type = 0
		  and w.data_source_type = 'CLAIMS';
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Set indicator for claims record in linkage_adjustment', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;	
	end if;
	
	--	if linkage cols are specified...

	if coalesce(array_length(v_linkage_cols_array,1),0) > 0 then
	
		--	set x_log_type to 20 for all linkage cols null
	
		v_sql := 'update cz.linkage_wrk1 set x_log_type=20 where 1=1';
		v_array_pos := 1;
		while v_array_pos <= array_length(v_linkage_cols_array,1)
		loop
			v_sql := v_sql || ' and (' || v_linkage_cols_array[v_array_pos] || ' is null or ' || v_linkage_cols_array[v_array_pos] || '::text=' || '''' || '''' || ')';
			v_array_pos := v_array_pos + 1;
		end loop;
		-- raise notice 'v_sql: %', v_sql;
		
		begin
		execute v_sql;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Set indicator for null linkage cols', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;	

		--	set x_log_type to 30 for multiple records with same linkage col values within data source
		
		v_sql := 'with upd as (select person_source_value, x_data_source_id, x_record_num from cz.linkage_wrk1';
		v_sql := v_sql || ' where (' || v_linkage_cols || ', x_data_source_id) in (select ' || v_linkage_cols || ', x_data_source_id';
		v_sql := v_sql || ' from cz.linkage_wrk1 where x_log_type = 0';	
		v_sql := v_sql || ' group by ' || v_linkage_cols || ', x_data_source_id having count(distinct person_source_value) > 1))';
		v_sql := v_sql || ' update cz.linkage_wrk1 w set x_log_type=30';
		v_sql := v_sql || ' from upd where w.person_source_value = upd.person_source_value and w.x_data_source_id = upd.x_data_source_id' ;
		v_sql := v_sql || ' and w.x_record_num = upd.x_record_num';
		-- raise notice 'v_sql = %', v_sql;
		begin
		execute v_sql;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Set indicator for multiple records with same linkage values within data source id', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;		
				
		--	set x_log_type to 40 for multiple records with same linkage col values within same data_source_type
		
		v_sql := 'with upd as (select person_source_value, x_data_source_id, x_record_num from cz.linkage_wrk1';
		v_sql := v_sql || ' where (' || v_linkage_cols || ', data_source_type) in (select ' || v_linkage_cols || ', data_source_type';
		v_sql := v_sql || ' from cz.linkage_wrk1 where x_log_type = 0';
		v_sql := v_sql || ' group by ' || v_linkage_cols || ', data_source_type having count(distinct x_data_source_id) > 1))';
		v_sql := v_sql || ' update cz.linkage_wrk1 w set x_log_type=40';
		v_sql := v_sql || ' from upd where w.person_source_value = upd.person_source_value and w.x_data_source_id = upd.x_data_source_id' ;
		v_sql := v_sql || ' and w.x_record_num = upd.x_record_num';
		-- raise notice 'v_sql = %', v_sql;
		begin
		execute v_sql;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Set indicator for multiple records with same linkage values across data source id', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
	end if;
	
	--	log records to linkage_log
	
	begin
	insert into cz.linkage_log
	(person_source_value
	,x_data_source_id
	,x_etl_date
	,x_record_num
	,log_type
	,log_message
	)
	select distinct w.person_source_value
		  ,w.x_data_source_id
		  ,w.x_etl_date
		  ,w.x_record_num
		  ,case x_log_type 
		   when 0 then 'SUCCESS'
		   when 1 then 'SKIPPED'
		   when 10 then 'SKIPPED'
		   when 15 then 'SKIPPED'
		   when 20 then 'WARNING'
		   when 30 then 'ERROR'
		   when 40 then 'ERROR'
		   else 'Unknown' end
		  ,case x_log_type 
		   when 0 then 'Clinical record selected'
		   when 1 then initcap(w.data_source_type) || ' record skipped, not latest record'
		   when 10 then 'Claims record skipped, already mapped'
		   when 15 then 'Claims record skipped, person_source_value/x_data_source_id in cz.linkage_adjustment'
		   when 20 then initcap(w.data_source_type) || ' record skipped, all ' || v_linkage_cols || ' column values are null' 
		   when 30 then initcap(w.data_source_type) || ' multiple records with same ' || v_linkage_cols || ' values within a data source'
		   when 40 then initcap(w.data_source_type) || ' multiple records with same ' || v_linkage_cols || ' values and same data source type and different data sources'
		   else 'Unknown' end
	from cz.linkage_wrk1 w
	where (w.data_source_type = 'CLAIMS' and w.x_log_type > 0)		-- skip success for claims because the record may not be mapped in link step
	   or (w.data_source_type = 'CLINICAL')
	order by w.x_data_source_id
			,w.person_source_value
			,w.x_etl_date
			,w.x_record_num;	 
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert records to linkage_log', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;	
		
	--	if linkage cols specified ....
	
	if coalesce(array_length(v_linkage_cols_array,1),0) > 0 then
		--	insert surviving records into clinical (a) table
		begin
		insert into cz.linkage_source_a
		(person_source_value
		,x_data_source_type
		,medicaid_id_number
		,ssn
		,last
		,middle
		,first
		,address_1
		,address_2
		,city
		,state
		,zip
		,county
		,year_of_birth
		,month_of_birth
		,day_of_birth
		,x_data_source_id
		,x_etl_date
		,x_record_num
		)
		select d.person_source_value
			  ,d.x_data_source_type
			  ,d.medicaid_id_number
			  ,d.ssn
			  ,d.last
			  ,d.middle
			  ,d.first
			  ,d.address_1
			  ,d.address_2
			  ,d.city
			  ,d.state
			  ,d.zip
			  ,d.county
			  ,d.year_of_birth
			  ,d.month_of_birth
			  ,d.day_of_birth
			  ,d.x_data_source_id
			  ,d.x_etl_date
			  ,d.x_record_num
		from std.x_demographic d
		inner join cz.linkage_wrk1 w
			  on  d.person_source_value = w.person_source_value
			  and d.x_data_source_id = w.x_data_source_id
			  and d.x_record_num = w.x_record_num
			  and d.x_etl_date = w.x_etl_date
		where w.data_source_type = 'CLINICAL'
		  and w.x_log_type = 0;			 
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert clinical records to linkage_source_a', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		--	insert surviving records into medicaid (b) table
		begin
		insert into cz.linkage_source_b
		(person_source_value
		,x_data_source_type
		,medicaid_id_number
		,ssn
		,last
		,middle
		,first
		,address_1
		,address_2
		,city
		,state
		,zip
		,county
		,year_of_birth
		,month_of_birth
		,day_of_birth
		,x_data_source_id
		,x_etl_date
		,x_record_num
		)
		select d.person_source_value
			  ,d.x_data_source_type
			  ,d.medicaid_id_number
			  ,d.ssn
			  ,d.last
			  ,d.middle
			  ,d.first
			  ,d.address_1
			  ,d.address_2
			  ,d.city
			  ,d.state
			  ,d.zip
			  ,d.county
			  ,d.year_of_birth
			  ,d.month_of_birth
			  ,d.day_of_birth
			  ,d.x_data_source_id
			  ,d.x_etl_date
			  ,d.x_record_num
		from std.x_demographic d
		inner join cz.linkage_wrk1 w
			  on  d.person_source_value = w.person_source_value
			  and d.x_data_source_id = w.x_data_source_id
			  and d.x_record_num = w.x_record_num
			  and d.x_etl_date = w.x_etl_date
		where w.x_log_type = 0			-- only unmapped records
		  and w.data_source_type = 'CLAIMS';			 
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert claims records to linkage_source_b', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;	
			
		if v_row_count = 0 then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'No records inserted to linkage_source_b', v_row_count, v_step_ct, 'WARNING');
			v_rtn_cd := 8;
		end if;

	end if;
	
	--drop table if exists cz.linkage_wrk1;
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end '|| v_function_name, 0, v_step_ct, 'COMPLETE');
	
	return v_rtn_cd;
	
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_linkage_preprocess(bigint, bigint, character varying)
  OWNER TO rosita;
