-- Function: cz.czx_generate_random_ids(character varying, integer)

DROP FUNCTION cz.czx_generate_random_ids(character varying, integer);
 
CREATE OR REPLACE FUNCTION cz.czx_generate_random_ids(p_id_type character varying, p_max_id integer)
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
declare
	v_max_random_id double precision;
	v_i integer;
	v_j integer;
	v_ids integer array[2147483647];
begin
	delete from cz.cz_random_id where id_type = p_id_type;

	v_ids[0] := 0;

	for v_i in 1..(p_max_id - 1) loop
		v_j := round(random() * v_i::double precision);
		v_ids[v_i] := v_ids[v_j];
		v_ids[v_j] := v_i;
		if v_i % 10000 = 0 then
			raise info 'Generated % ids', v_i;
		end if;
	end loop;
		

	for v_i in 0..(p_max_id - 1) loop
		insert into cz.cz_random_id (id, id_type, random_id)
		values (v_i, p_id_type, v_ids[v_i] + 1);
		if v_i % 10000 = 0 then
			raise info 'Inserted % ids', v_i;
		end if;
	end loop;
	
/*
	v_id := 1;
	v_collisions := 0;
	while v_id < p_max_id loop
		begin
			insert into cz.cz_random_id (id, id_type, random_id)
			values (v_id, p_id_type, round((random() * v_max_random_id) + 1::double precision));
			v_id := v_id + 1;
		exception
			when unique_violation then
				v_collisions := v_collisions + 1;
				if v_id % 10000 = 0 then
					raise info 'ERROR id %, collisions % , error % - %', v_id, v_collisions, sqlstate, sqlerrm;
				end if;
		end;
	end loop;
*/

	
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_generate_random_ids(character varying, integer)
  OWNER TO rosita;
