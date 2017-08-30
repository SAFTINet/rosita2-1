-- Function: cz.czx_end_audit(bigint, character varying)

DROP FUNCTION IF EXISTS cz.czx_end_audit(bigint, character varying);

CREATE OR REPLACE FUNCTION cz.czx_end_audit(p_job_id bigint, p_job_status character varying)
  RETURNS void AS
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
begin

	raise info 'cz.czx_end_audit: % - %', p_job_id, p_job_status;

--	update cz.cz_job_master
--	set
--		active = 'N',
--		end_date = clock_timestamp(),
--		job_status = p_job_status
--	where
--		active = 'Y'
--		and job_id = p_job_id;
		
exception 

	when others then
		raise exception 'czx_end_audit: ERROR % - %', sqlstate, sqlerrm;	     


end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_end_audit(bigint, character varying)
  OWNER TO rosita;
