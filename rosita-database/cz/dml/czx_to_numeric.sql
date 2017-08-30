-- Function: cz.czx_to_numeric(character varying, character varying)
 
--DROP FUNCTION cz.czx_to_numeric(character varying, character varying);
 
CREATE OR REPLACE FUNCTION cz.czx_to_numeric(val character varying, cast_type character varying)
  RETURNS numeric AS
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
  if val is not null and char_length(val) > 0 then
    case lower(cast_type)
    when 'date' then
      return extract(epoch from val::date);
    when 'time' then
      return extract(epoch from val::time);
    else
      return val::numeric;
    end case;
  else
    return null;
  end if;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_to_numeric(character varying, character varying)
  OWNER TO rosita;

-- Function: cz.czx_to_numeric(numeric, character varying)

--DROP FUNCTION cz.czx_to_numeric(numeric, character varying);

CREATE OR REPLACE FUNCTION cz.czx_to_numeric(val numeric, cast_type character varying)
  RETURNS numeric AS
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
  return val;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_to_numeric(numeric, character varying)
  OWNER TO rosita;

-- Function: cz.czx_to_numeric(integer, character varying)

--DROP FUNCTION cz.czx_to_numeric(integer, character varying);

CREATE OR REPLACE FUNCTION cz.czx_to_numeric(val integer, cast_type character varying)
  RETURNS numeric AS
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
  return val::numeric;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_to_numeric(integer, character varying)
  OWNER TO rosita;

-- Function: cz.czx_to_numeric(time without time zone, character varying)

--DROP FUNCTION cz.czx_to_numeric(time without time zone, character varying);

CREATE OR REPLACE FUNCTION cz.czx_to_numeric(val time without time zone, cast_type character varying)
  RETURNS numeric AS
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
  return extract(epoch from val);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_to_numeric(time without time zone, character varying)
  OWNER TO rosita;

-- Function: cz.czx_to_numeric(date, character varying)

--DROP FUNCTION cz.czx_to_numeric(date, character varying);

CREATE OR REPLACE FUNCTION cz.czx_to_numeric(val date, cast_type character varying)
  RETURNS numeric AS
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
  if val is not null then 
	return extract(epoch from val);
  else
	return null;
  end if;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_to_numeric(date, character varying)
  OWNER TO rosita;
