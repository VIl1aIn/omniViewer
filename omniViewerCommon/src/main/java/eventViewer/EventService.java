package eventViewer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import eventViewer.config.ColorSetConfig;
import eventViewer.config.ColumnSetConfig;
import eventViewer.config.FilterSetConfig;
import eventViewer.dao.SettingDAO;
import eventViewer.model.Event;

/**
 * Operations with alarms from OMNIbus
 */
public class EventService {

    private final JdbcTemplate jdbc;

    public HashMap<Integer, String> severityConv, severityColor, classConv;
    public HashMap<String, Integer> listRefresh;
    public HashMap<String, String> listFilters, listFields;
    public ArrayList<String> listDetails;

    public EventService(SettingDAO dao) {

        assert (dao != null);

        this.jdbc = dao.getJdbc();
        severityConv = dao.getSeverityConv();
        classConv = dao.getClassConv();

        severityColor = getDefaultColors();
        listFilters = getDefaultFilters();
        listFields = getDefaultListFields();
        listRefresh = getDefaultListRefresh();
        listDetails = getBaseFields();
    }
    /**
     * 
     * @param rs
     * @return event with fill fields from DB
     * @throws SQLException 
     */
    public Event newEventFromRS(ResultSet rs) throws SQLException {
    	Event ev = new Event();
    	// Set field for BaseEvent class
    	ev.setAck(rs.getInt("Acknowledged"));
    	ev.setSerial(rs.getInt("Serial"));
    	ev.setType(rs.getInt("Type"));
    	ev.setGrade(rs.getInt("Grade"));
    	ev.setFirstOccurrence(rs.getLong("FirstOccurrence"));
    	ev.setLastOccurrence(rs.getLong("LastOccurrence"));
    	ev.setStateChange(rs.getLong("StateChange"));
    	ev.setIdentifier(rs.getString("Identifier"));
    	ev.setAgent(rs.getString("Agent"));
    	ev.setManager(rs.getString("Manager"));
    	ev.setBsmIdentity(rs.getString("BSM_Identity"));
    	ev.setBsmSubIdentity(rs.getString("BSM_SubIdentity"));
        ev.setAlertKey(rs.getString("AlertKey"));
        ev.setAlertGroup(rs.getString("AlertGroup"));
        ev.setNode(rs.getString("Node"));
        ev.setNodeAlias(rs.getString("NodeAlias"));
        ev.setSummary(rs.getString("Summary"));
        ev.setEventId(rs.getString("EventId"));
        ev.setExtAttr(rs.getString("ExtendedAttr"));
        ev.setServerName(rs.getString("ServerName"));
        ev.setUrl(rs.getString("URL"));
        ev.setLocation(rs.getString("Location"));
        ev.setCustomer(rs.getString("Customer"));
        ev.setService(rs.getString("Service"));
        ev.setTecHostname(rs.getString("TECHostname"));
        ev.setLocalNodeAlias(rs.getString("LocalNodeAlias"));
        ev.setLocalPriObj(rs.getString("LocalPriObj"));
        ev.setLocalRootObj(rs.getString("LocalRootObj"));
        ev.setLocalSecObj(rs.getString("LocalSecObj"));
        ev.setEventClass(rs.getInt("Class"));
        ev.setSeverity(rs.getInt("Severity"));
        ev.setExpireTime(rs.getInt("ExpireTime"));
        ev.setTally(rs.getInt("Tally"));

        // Custom field add here, the next method
        //ev.setItmApplLabel(rs.getString("ITMApplLabel"));
        //ev.setRadServiceName(rs.getString("RAD_ServiceName"));
        
    	return ev;
    }
    
    
    /*
     * Section for generate list fields for SQL statement
     * 
     * addField for 'update alerts.status set ...'
     * insertField for 'insert into alerts.status (...) values (...)'
     */
    // Put string value
    private String addField(String field, String txt, String val) {
        if (val == null || val.isEmpty()) {
            return field;
        }
        return field + "," + txt + "='" + val + "'";
    }
    // Put int value
    private String addField(String field, String txt, int val) {
    	return field + "," + txt + "=" + val;
    }

    // Put String value
    private void insertField(HashMap<String, String> hm, String key, String value) {
        if (value != null && !value.isEmpty()) {
            hm.put(key, "'" + value + "'");
        }
    }
    // Put int value
    private void insertField(HashMap<String, String> hm, String key, int value) {
        hm.put(key, Integer.toString(value));
    }

    /*
     * get event list from OMNI
     */
    public List<Event> list(String filter, String group, String order) {

        assert (filter != null && !filter.isEmpty());

        String query = "select * from alerts.status where "
                + filter;
        if (!group.isEmpty()) {
            query += " group by " + group;
        }
        if (!order.isEmpty()) {
            query += " order by " + order;
        }

        List<Event> events = jdbc.query(query,
            new RowMapper<Event>() {
            @Override
            public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            	return newEventFromRS(rs);
            }
        }
        		);
        return events;
    }

    public Event get(int eventSerial) {
        return list("Serial = " + eventSerial, "", "").get(0);
    }

    public void update(Event ev, boolean updateAll) {
        int ack = ev.getAck() ? 1 : 0;
        String restField = "";
        assert (ev.getSerial() > 0);
        if (updateAll) {
            restField = addField(restField, "Summary", ev.getSummary());
            restField = addField(restField, "ExtendedAttr", ev.getExtAttr());
            restField = addField(restField, "URL", ev.getUrl());
            restField = addField(restField, "EventId", ev.getEventId());
            restField = addField(restField, "ExpireTime", ev.getExpireTime());
            restField = addField(restField, "Location", ev.getLocation());
            restField = addField(restField, "Customer", ev.getCustomer());
        }

        String update = "update alerts.status set "
                + "Severity=" + ev.getSeverity() + ","
                + "Acknowledged=" + ack
                + restField
                + " where Serial=" + ev.getSerial();
        //System.out.println(update);
        jdbc.update(update);
    }

    public void massupdate(String[] serial, int ack, int severity) {
    	// Check do need change
        if (severity + ack == -2) return;
        
    	String upd = "update alerts.status set ";
    	if (severity != -1)
    		upd += "Severity="+severity;
    	if (ack != -1) {
    		if (severity != -1) upd += ",";
    		upd += "Acknowledged="+ack;
    	}
    	upd += " where Serial in (";
        for (String s : serial) {
            upd += s + ",";
        }
        String update = upd.substring(0, upd.length() - 1);
        update += ")";
        jdbc.update(update);
    }

    public void delete(int serial) {

        assert (serial > 0);
        jdbc.update("delete from alerts.status where Serial=" + serial);
    }

    public void massdelete(String[] serial) {
        for (String s : serial) {
            delete(Integer.parseInt(s));
        }
    }

    public void create(Event ev) {
        HashMap<String, String> insertMap = new HashMap<String, String>();
        // Add fields for insert to alerts
        insertField(insertMap, "Severity", ev.getSeverity());
        insertField(insertMap, "Class", ev.getEventClass());
        insertField(insertMap, "Agent", ev.getAgent());
        insertField(insertMap, "Manager", ev.getManager());

        insertField(insertMap, "Identifier", ev.getIdentifier());

        insertField(insertMap, "Type", ev.getType());
        insertField(insertMap, "AlertKey", ev.getAlertKey());
        insertField(insertMap, "AlertGroup", ev.getAlertGroup());
        insertField(insertMap, "Node", ev.getNode());
        insertField(insertMap, "NodeAlias", ev.getNodeAlias());
        insertField(insertMap, "BSM_Identity", ev.getBsmIdentity());
        insertField(insertMap, "Summary", ev.getSummary());
        insertField(insertMap, "ExtendedAttr", ev.getExtAttr());
        insertField(insertMap, "URL", ev.getUrl());
        insertField(insertMap, "Location", ev.getLocation());
        insertField(insertMap, "Customer", ev.getCustomer());
        insertField(insertMap, "EventId", ev.getEventId());
        insertField(insertMap, "ExpireTime", ev.getExpireTime());

        insertField(insertMap, "FirstOccurrence", (int) (new Date().getTime() / 1000));
        insertField(insertMap, "Tally", 1);

        Set<String> keys = insertMap.keySet();
        String lF = "";
        String lV = "";

        for (Object o : keys) {
            lF += (String) o + ",";
            lV += insertMap.get(o) + ",";
        }
        // Delete last ","
        String fields = lF.substring(0, lF.length() - 1);
        String values = lV.substring(0, lV.length() - 1);

        String create = "insert into alerts.status ("
                + fields + ") values ("
                + values + ")";
        //System.out.println(create);
        jdbc.update(create);
    }

    /**
     * Count number events by filter
     *
     * @param filter
     * @return count(Severity) where filter
     */
    public int count(String filter) {
        String query = "select count(Severity) from alerts.status where ";
        query += filter;
        return this.jdbc.queryForObject(query, Integer.class);
    }

    /**
     * Set list filters from external settings
     *
     * @param fsConf
     */
    public void setFilters(FilterSetConfig fsConf) {
        listFilters.clear();
        for (FilterSetConfig.FilterSet fs : fsConf.getFilterSets()) {
            listFilters.put(fs.getName(), fs.getFilter());
        }
    }

    /**
     * Set list columns from external settings
     *
     * @param colConf
     */
    public void setColumns(ColumnSetConfig colConf) {
    	if (!(colConf.getColSets().isEmpty() || colConf.getColSets() == null)) {
    		listFields.clear();
    		for (ColumnSetConfig.ColSet cs : colConf.getColSets()) {
    			listFields.put(cs.getCol(), cs.getHeader());
    		}
    	}
    	if (!(colConf.getColDetails().isEmpty() || colConf.getColDetails() == null)) {
    		listDetails.addAll(colConf.getColDetails());
    	}
    }

    /**
     * Set colors severity from external settings
     *
     * @param colorConf
     */
    public void setColor(ColorSetConfig colorConf) {
        severityColor.clear();
        for (ColorSetConfig.ColorSet cs : colorConf.getColorSets()) {
            severityColor.put(cs.getCode(), cs.getColor());
        }
    }

    /**
     * Get base list fields by event
     * @return ArrayList<String> with list events
     */
    public ArrayList<String> getBaseFields() {
    	ArrayList<String> bD = new ArrayList<String>();
    	bD.add("ack");
    	bD.add("identifier");
    	bD.add("serial");
    	bD.add("grade");
    	bD.add("severity");
    	bD.add("firstOccurrence");
    	bD.add("lastOccurrence");
    	bD.add("stateChange");
    	bD.add("agent");
    	bD.add("manager");
    	bD.add("alertKey");
    	bD.add("alertGroup");
    	bD.add("node");
    	bD.add("nodeAlias");
		bD.add("localNodeAlias");
    	bD.add("localPriObj");
    	bD.add("localRootObj");
    	bD.add("localSecObj");
    	bD.add("bsmIdentity");
    	bD.add("bsmSubIdentity");
    	bD.add("location");
    	bD.add("customer");
    	bD.add("eventId");
    	bD.add("eventClass");
    	bD.add("summary");
    	bD.add("expireTime");
    	bD.add("tally");
    	bD.add("extAttr");
    	bD.add("serverName");
    	bD.add("tecHostname");
    	bD.add("service");
    	bD.add("url");
    	bD.add("type");
    	
    	return bD;
    }
    
    /**
     * Get default lists of filters w/o file
     *
     * @return HashMap<String, String>:
     *
     * "Display Name", "where clause"
     */
    private HashMap<String, String> getDefaultFilters() {

        HashMap<String, String> lf = new HashMap<String, String>();

        lf.put("All", "Class != 12000");
        lf.put("All Service", "Class = 12000");
        lf.put("Last 15 min", "Class != 12000 and Severity != 0 and LastOccurrence > getdate() - 900");
        lf.put("Last hour", "Class != 12000 and Severity != 0 and LastOccurrence > getdate() - 3600");
        lf.put("Critical", "Class != 12000 and Severity = 5");
        lf.put("Major", "Class != 12000 and Severity = 4");
        lf.put("Minor", "Class != 12000 and Severity = 3");
        lf.put("Warning", "Class != 12000 and Severity = 2");
        lf.put("Unknown", "Class != 12000 and Severity = 1");
        lf.put("Clear", "Class != 12000 and Severity = 0");

        return lf;
    }

    /**
     * Get default list fields and Header w/o external
     *
     * @return HashMap<String, String> fields
     */
    private HashMap<String, String> getDefaultListFields() {

        HashMap<String, String> lf = new HashMap<String, String>();

        lf.put("summary", "Summary");
        lf.put("tally", "Count");

        return lf;
    }

    /**
     * Get default colors w/o external
     *
     * @return HashMap<Integer, String> fields
     */
    private HashMap<Integer, String> getDefaultColors() {

        HashMap<Integer, String> sc = new HashMap<Integer, String>();
        // Init colors for output
        sc.put(0, "LimeGreen");
        sc.put(1, "MediumOrchid");
        sc.put(2, "SkyBlue");
        sc.put(3, "yellow");
        sc.put(4, "orange");
        sc.put(5, "Red");

        return sc;
    }
    private HashMap<String, Integer> getDefaultListRefresh() {
        HashMap<String, Integer> lr = new HashMap<String, Integer>();
        lr.put("No refresh", 0);
        lr.put("1 min", 60);
        lr.put("3 min", 180);
        lr.put("5 min", 300);
        lr.put("10", 600);
        
        return lr;
    }
    /*
    * Format query:
    * select [top <num line>] * from db.table where <conditions>
    * group by <col>, <col> ...
    * order by <col>,<col> ... [ASC | DESC]
     */
}
