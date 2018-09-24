package eventViewer.dao;

import java.util.HashMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class H2MemDB implements SettingDAO {
    private HashMap<Integer,String> severityConv, classConv;
    private JdbcTemplate jdbcTemplate;

    public H2MemDB() {
    	// TODO set severityConv, classConv, jdbcTemplate
    	EmbeddedDatabase emdb = new EmbeddedDatabaseBuilder()
    			.setType(EmbeddedDatabaseType.H2)
    			.setName("ncomsdb")
    			.addScript("classpath:/sql/schema.sql")
    			.addScript("classpath:/sql/test-data.sql")
    			.build();
    	jdbcTemplate = new JdbcTemplate(emdb);
    	// SeverityConv Init
    	severityConv = new HashMap<Integer,String>();
    	severityConv.put(0,"Clear");
    	severityConv.put(1,"Unknown");
    	severityConv.put(2,"Warning");
    	severityConv.put(3,"Minor");
    	severityConv.put(4,"Major");
    	severityConv.put(5,"Critical");
    	
    	//ClassConv init
    	classConv = new HashMap<Integer,String>();
    	classConv.put(0, "Default Class");
    	classConv.put(4400, "Generic probe");
    	classConv.put(6601, "TME10tecad");
    	classConv.put(1, "MyClass");
    	classConv.put(87723, "IBM Tivoli Monitoring Agent");
    	classConv.put(87722, "IBM Tivoli Monitoring");
    	classConv.put(100, "Ping Probe");
    	classConv.put(200, "Syslog Probe");
    	classConv.put(1150, "Socket probe");
    	classConv.put(10500, "Impact");
    }
    
    public JdbcTemplate getJdbc() {
    	return jdbcTemplate;
    }
    
    public HashMap<Integer,String> getSeverityConv() {
    	return severityConv;
    }
    
    public HashMap<Integer,String> getClassConv() {
    	return classConv;
    }
}
