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

package edu.ucdenver.rosita.utils;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 */
public class GridRowMapper implements RowMapper {

    private int valueCount = 0;

    public GridRowMapper(int valueCount) {

        super();
        this.valueCount = valueCount;

    }

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        ArrayList<Object> values = new ArrayList<Object>();

        for (int i = 0; i < valueCount; i++) {
            Object value = null;
//            int columnType = rsmd.getColumnType(i);
//
//            switch (columnType) {
//                case Types.VARCHAR:
//                    value = rs.getString(i);
//                    break;
//                case Types.INTEGER:
//                    value = rs.getInt(i);
//                    break;
//                case Types.DATE:
//                    value = rs.getDate(i);
//                    break;
//                case Types.TIME:
//                    value = rs.getTime(i);
//                    break;
//                default:
//                    value = rs.getObject(i);
//                    break;
//            }
            value = rs.getObject(i + 1);
            values.add(value);
        }

        return values;

    }

}
