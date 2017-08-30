-- Function: cz.czx_write_error(bigint, character varying, bigint, character varying, character varying, character varying)

DROP FUNCTION IF EXISTS cz.czx_write_error(bigint, character varying, bigint, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION cz.czx_write_error(
	p_step_id bigint,
	p_function_name character varying,
	p_step_number bigint,
	p_message_type character varying,
	p_error_state character varying,
	p_error_message character varying
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
 
BEGIN

	raise info 'cz.czx_write_error: ERROR occurred in % - %, %, %, %', p_step_id, p_function_name, p_step_number, p_error_state, p_error_message;
	
	select job_id into v_job_id from cz.step where step_id=p_step_id;
	
	insert into cz.log(
	    job_id,
		step_id,
 		function_name,
		subtask_num,
		message_type,
		error_code,
		message,
		log_date
	)
	values (  
 		v_job_id,
		p_step_id,
		p_function_name,
		p_step_number,
		p_message_type,
		p_error_state,
		p_error_message,
		clock_timestamp()
	);

        
exception 
	when others then
             raise exception 'cz.czx_write_error: ERROR % - %', sqlstate, sqlerrm;

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_write_error(bigint, character varying, bigint, character varying, character varying, character varying)
  OWNER TO rosita;
