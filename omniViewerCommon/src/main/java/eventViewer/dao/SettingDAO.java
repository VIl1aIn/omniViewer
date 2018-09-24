package eventViewer.dao;

import java.util.HashMap;

import org.springframework.jdbc.core.JdbcTemplate;

public interface SettingDAO {
	JdbcTemplate getJdbc();
	HashMap<Integer,String> getSeverityConv();
	HashMap<Integer,String> getClassConv();
}
