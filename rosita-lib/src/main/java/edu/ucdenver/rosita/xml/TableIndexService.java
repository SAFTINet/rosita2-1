package edu.ucdenver.rosita.xml;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class TableIndexService {

    private DataSource ds = null;
    private JdbcTemplate jdbc = null;
    private Long stepId = 0L;

    public TableIndexService(DataSource ds, Long stepId) {

        this.ds = ds;
        this.jdbc = new JdbcTemplate(ds);
        this.stepId = stepId;

    }

    public int saveIndexes(String schemaName) {

        String sql = "SELECT cz.czx_table_index_maint(?, ?, ?)";
        Object[] params = { stepId, "SAVE", schemaName };
        int result = jdbc.queryForInt(sql, params);

        return result;

    }


    public int dropIndexes(String schemaName) {

        String sql = "SELECT cz.czx_table_index_maint(?, ?, ?)";
        Object[] params = { stepId, "DROP", schemaName };
        int result = jdbc.queryForInt(sql, params);

        return result;

    }

    public int addIndexes(String schemaName) {

        String sql = "SELECT cz.czx_table_index_maint(?, ?, ?)";
        Object[] params = { stepId, "ADD", schemaName };
        int result = jdbc.queryForInt(sql, params);

        return result;

    }

}
