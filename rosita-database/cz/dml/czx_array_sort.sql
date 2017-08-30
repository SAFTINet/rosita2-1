-- Function: cz.czx_array_sort(anyarray)

-- DROP FUNCTION cz.czx_array_sort(anyarray);

CREATE OR REPLACE FUNCTION cz.czx_array_sort(anyarray)
  RETURNS anyarray AS
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
SELECT ARRAY(   
    SELECT $1[s.i] AS "foo"   
    FROM   
        generate_series(array_lower($1,1), array_upper($1,1)) AS s(i)   
    ORDER BY foo   
);   
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_array_sort(anyarray)
  OWNER TO rosita;
