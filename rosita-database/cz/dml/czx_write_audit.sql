-- Function: cz.czx_write_audit(bigint, character varying, character varying, character varying, bigint, bigint, character varying)

DROP FUNCTION IF EXISTS cz.czx_write_audit(bigint, character varying, character varying, character varying, bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION cz.czx_write_audit(
	p_step_id bigint,
	p_database_name character varying,
	p_procedure_name character varying,
	p_step_desc character varying,
	p_records_manipulated bigint,
	p_step_number bigint,
	p_step_status character varying
) RETURNS void AS
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
 v_job_id bigint;
 lastTime timestamp;
 currTime timestamp;
 elapsedSecs numeric;

begin
	-- raise info 'cz.czx_write_audit: %.% - %, %, %, %', p_database_name, p_procedure_name, p_step_number, p_step_desc, p_records_manipulated, p_step_status;
	
	select job_id into v_job_id from cz.step where step_id=p_step_id;
	
	select max(log_date) into lastTime from cz.log where step_id = p_step_id;
	
 -- clock_timestamp() is the current system time
    select clock_timestamp() into currTime;
    elapsedSecs :=  coalesce(((DATE_PART('day', currTime - lastTime) * 24 + 
					DATE_PART('hour', currTime - lastTime)) * 60 +
                    DATE_PART('minute', currTime - lastTime)) * 60 +
                    DATE_PART('second', currTime - lastTime),0);

	
	insert 	into cz.log(
	    job_id,
		step_id,
		schema_name,
 		function_name, 
 		message, 
		records_manipulated,
		subtask_num,
		message_type,
		time_elapsed_secs,
		log_date
	)
	values (  
 		v_job_id,
		p_step_id,
		p_database_name,
		p_procedure_name,
		p_step_desc,
		p_records_manipulated,
		p_step_number,
		p_step_status,
		elapsedSecs,
		currTime
	);
   
exception 

		when others then
	             raise exception 'czx_write_audit: ERROR % - %', sqlstate, sqlerrm;  	

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_write_audit(bigint, character varying, character varying, character varying, bigint, bigint, character varying)
  OWNER TO rosita;
