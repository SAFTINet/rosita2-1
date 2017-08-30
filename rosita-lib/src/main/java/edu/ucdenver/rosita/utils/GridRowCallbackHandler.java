package edu.ucdenver.rosita.utils;

import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
  */
public class GridRowCallbackHandler implements RowCallbackHandler {

    GridCache cache = null;
    int valueCount = 0;
    int rowNum = 0;

    public GridRowCallbackHandler(GridCache cache, int valueCount) {

        this.cache = cache;
        this.valueCount = valueCount;

    }

    @Override
    public void processRow(ResultSet rs) throws SQLException {

        ArrayList<Object> record = mapRow(rs);
        rowNum++;
        cache.add(record);

    }

    public ArrayList<Object> mapRow(ResultSet rs) throws SQLException {

        ArrayList<Object> values = new ArrayList<Object>();

        for (int i = 0; i < valueCount; i++) {
            Object value = null;
            value = rs.getObject(i + 1);
            values.add(value);
        }

        return values;

    }


}
