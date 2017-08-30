-- Function: cz.czx_to_varchar(numeric, character varying)

DROP FUNCTION cz.czx_to_varchar(numeric, character varying);
 
CREATE OR REPLACE FUNCTION cz.czx_to_varchar(val numeric, cast_type character varying)
RETURNS character varying AS
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
  case upper(cast_type)
  when 'DATE' then
    return to_timestamp(val)::date::varchar;
  when 'TIME' then
    return to_timestamp(val)::time::varchar;
  else
    return val::varchar;
  end case;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_to_varchar(numeric, character varying)
  OWNER TO rosita;
