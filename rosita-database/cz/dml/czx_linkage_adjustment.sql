-- Function: cz.czx_linkage_adjustment(bigint)

DROP FUNCTION IF EXISTS cz.czx_linkage_adjustment(bigint);

CREATE OR REPLACE FUNCTION cz.czx_linkage_adjustment(p_step_id bigint)
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
	
	v_sql 	varchar(2000);
	v_msg	varchar(2000);
	log	record;
	
begin
	v_database_name := 'cz';
	v_function_name := 'czx_linkage_adjustment';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;	

	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;	
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start '||v_function_name, v_row_count, v_step_ct, 'START');	
	
	drop table if exists cz.linkage_adj1;
	
	v_sql := 'create table cz.linkage_adj1 as select distinct la.person_source_value, la.x_data_source_id, cmap.id as claims_id,';
	v_sql := v_sql || ' case when la.link_source_value = ' || '''' || '''' || ' then null else la.link_source_value end as link_source_value,';
	v_sql := v_sql || ' case when la.link_x_data_source_id::text = ' || '''' || '''' || ' then null else la.link_x_data_source_id end as link_x_data_source_id,';
	v_sql := v_sql || ' lmap.id as clinical_id from cz.linkage_adjustment la';
	v_sql := v_sql || ' left outer join cz.cz_id_map cmap on la.person_source_value = cmap.source_value and la.x_data_source_id = cmap.x_data_source_id and cmap.id_type = ' || '''' || 'person_id' || '''';
	v_sql := v_sql || ' left outer join cz.cz_id_map lmap on la.link_source_value = lmap.source_value and la.link_x_data_source_id = lmap.x_data_source_id and lmap.id_type = ' || '''' || 'person_id' || '''';
	raise notice 'v_sql: %', v_sql;
	begin
	execute v_sql;
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Create cz.linkage_adj1 work table', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;
		 
	--	add valid_person, valid_link
	alter table cz.linkage_adj1 add column valid_person char(1);
	alter table cz.linkage_adj1 add column valid_link char(1);
	
	--	check that person_source_value/x_data_source_id is valid
	
	begin
	with upd as (select distinct person_source_value, x_data_source_id from std.x_demographic)
	update cz.linkage_adj1 a
	set valid_person='Y'
	from upd
	where a.person_source_value = upd.person_source_value
	  and a.x_data_source_id = upd.x_data_source_id;
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Set valid_person ind', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;
	
	--	check that link_source_value/x_data_source_id is valid
	
	begin
	with upd as (select distinct person_source_value, x_data_source_id from std.x_demographic)
	update cz.linkage_adj1 a
	set valid_link='Y'
	from upd
	where a.link_source_value = upd.person_source_value
	  and a.link_x_data_source_id = upd.x_data_source_id;
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Set valid_person ind', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;	
	--	process linkage_adjustment deletes
	
	begin
	delete from cz.cz_id_map cim
	where (id, source_value, x_data_source_id) in
		  (select la.claims_id as claim_id
				 ,la.person_source_value
				 ,la.x_data_source_id
		   from cz.linkage_adj1 la
		   where la.valid_person = 'Y'
		     and la.claims_id is not null
		     and ((la.link_source_value is null and la.link_x_data_source_id is null) or 
			      (la.clinical_id is not null and la.claims_id != la.clinical_id)));
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Unlink values in linkage_adjustment from cz.cz_id_map', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;
	
	--	process link linkage_adjustment records
	
	begin
	insert into cz.cz_id_map
	(id
	,source_value
	,x_data_source_id
	,id_type
	)
	select la.clinical_id
		  ,la.person_source_value
		  ,la.x_data_source_id
		  ,'person_id'
	from cz.linkage_adj1 la
	where la.clinical_id is not null
	  and la.claims_id != la.clinical_id
	  and la.valid_person = 'Y'
	  and not exists
		 (select 1 from cz.cz_id_map x
		  where la.person_source_value = x.source_value
		    and la.x_data_source_id = x.x_data_source_id
			and x.id_type = 'person_id');
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Link values in linkage_adjustment from cz.cz_id_map', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;
	
	--	log any linkage adjustment errors
	
	for log in select * from cz.linkage_adj1
	loop
		--	not valid person
		if log.valid_person is null then
			v_msg := 'person_source_value: ' || log.person_source_value || ' x_data_source_id: ' || log.x_data_source_id::text;
			v_msg := v_msg || ' not found in std.x_demographic table';
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, v_msg, 1, v_step_ct, 'ERROR');
			v_step_ct := v_step_ct + 1;
		else
			--	not valid link person
			if log.valid_link is null and (log.link_source_value is not null or log.link_x_data_source_id is not null) then
				v_msg := 'link_source_value: ' || log.link_source_value || 'link_x_data_source_id: ' || log.link_x_data_source_id::text;
				v_msg := v_msg || ' not found in std.x_demographic table';
				perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, v_msg, 1, v_step_ct, 'ERROR');
				v_step_ct := v_step_ct + 1;
			else	
				--	no id to unlink
				if log.claims_id is null and log.link_person_source_value is null and log.x_data_source_id is null then
					v_msg := 'person_source_value: ' || log.person_source_value || ' x_data_source_id: ' || log.x_data_source_id::text;
					v_msg := v_msg || ' not found in cz.cz_id_map, no unlink performed';
					perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, v_msg, 1, v_step_ct, 'WARNING');
					v_step_ct := v_step_ct + 1;
				else
					--	no id to link
					if log.valid_link = 'Y' and log.clinical_id is null then
						v_msg := 'link_source_value: ' || log.link_source_value || 'link_x_data_source_id: ' || log.link_x_data_source_id::text;
						v_msg := v_msg || ' not found in cz.cz_id_map, no link performed';
						perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, v_msg, 1, v_step_ct, 'ERROR');
						v_step_ct := v_step_ct + 1;
					end if;
				end if;
			end if;	
		end if;		
		
	end loop;
	
	--drop table if exists cz.linkage_adj1;
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end '|| v_function_name, 0, v_step_ct, 'COMPLETE');
	
	return 0;
	
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_linkage_adjustment(bigint)
  OWNER TO rosita;
	