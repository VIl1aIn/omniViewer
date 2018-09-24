package eventViewer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
//import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class OMNIbus implements SettingDAO {

    private String driverClass = "com.sybase.jdbc3.jdbc.SybDriver";
    private String username, host, url, ncoms;
    private HashMap<Integer, String> severityConv, classConv;

    private static final class MapValues implements ResultSetExtractor<HashMap<Integer, String>> {

        @Override
        public HashMap<Integer, String> extractData(ResultSet rs) throws SQLException {
            HashMap<Integer, String> sc = new HashMap<Integer, String>();
            while (rs.next()) {
                int key = rs.getInt(1);
                String value = rs.getString(2);
                sc.put(key, value);
            }
            return sc;
        }
    }

    /*
     * URL example for OMNI jdbc jconn3:
     * "jdbc:sybase:Tds:orw-tbsm02-test:4100/NCOMS?CHARSET=utf8"
     */
    private JdbcTemplate jdbcTemplate;

    public OMNIbus(String host, int port, String ncoms, String username, String password) {
        this.username = username;
        this.host = host;
        url = "jdbc:sybase:Tds:" + host + ":" + port + "/" + ncoms + "?CHARSET=utf8";
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        //SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        // "com.sybase.jdbc3.jdbc.SybDriver"
        dataSource.setDriverClassName(driverClass);
        dataSource.setUsername(username);
        dataSource.setUrl(url);
        dataSource.setPassword(password);
        //dataSource.setValidationQuery("select NumTables from catalog.databases where DatabaseName = 'alerts'");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcTemplate = jdbcTemplate;

        HashMap<Integer, String> severity = jdbcTemplate.query(
                "select Value, Conversion from alerts.conversions "
                + "where Colname='Severity' order by Value", new MapValues());
        severityConv = severity;
        HashMap<Integer, String> enumclass = jdbcTemplate.query(
                "select Value, Conversion from alerts.conversions "
                + "where Colname='Class' order by Value", new MapValues());
        classConv = enumclass;
    }

    public String getUsername() {
        return username;
    }

    public String getHost() {
        return host;
    }

    public String getUrl() {
        return url;
    }

    public String getNcoms() {
        return ncoms;
    }
    
    public JdbcTemplate getJdbc() {
        return jdbcTemplate;
    }

    public HashMap<Integer, String> getSeverityConv() {
        return severityConv;
    }

    public HashMap<Integer, String> getClassConv() {
        return classConv;
    }
}
