package eventViewer.model;

import java.util.Date;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotBlank;

/*
 * Event from OMNIbus
 */

public class BaseEvent {
    //TODO add fields from OMNI
	ConvertStringField cf = new ConvertStringField();
    /*
	 * Identifier
	 * Acknowledged
	 * AlertKey 
	 * AlertGroup
	 * Agent
	 * Manager
	 * 
	 *
	 * 
	 * ...
	 * 
     */
    private String alertKey, alertGroup, node, nodeAlias,
            summary, bsm_Identity, bsm_SubIdentity,eventId,
            agent, manager, extAttr, serverName, url,
            location, service, customer, tecHostname,
            localNodeAlias, localPriObj, localRootObj, localSecObj;
    @NotBlank(message = "Not be empty")
    private String identifier;
    private Date firstOccurrence, lastOccurrence, stateChange;

    @Min(value = 0, message = "Not be null")
    private int eventClass, type, grade;
    private int expireTime, tally, severity, serial;
    private boolean ack;

    public BaseEvent() {}


    /*
    // Delete last symbol from String value (OMNI format: '\0' - end string)
    private String delLast(String omni_str) {
        if (omni_str != null && omni_str.length() > 0) {
            return omni_str.substring(0, omni_str.length() - 1);
        } else {
            return omni_str;
        }
    }
    */

    // Create test event with preset fill fields
    public void initCustomEvent(int eventClass, int type, int severity, String id, String summary) {
        this.eventClass = eventClass;
        this.type = type;
        this.severity = severity;
        this.summary = summary;
        this.identifier = id;
        //this.firstOccurrence = new Date();
    }

    // Getters
    public boolean getAck() {
        return ack;
    }
    public String getIdentifier() {
        return identifier;
    }
    public String getAgent() {
        return agent;
    }
    public String getManager() {
        return manager;
    }
    public String getBsm_Identity() {
        return bsm_Identity;
    }
    public String getBsm_SubIdentity() {
        return bsm_SubIdentity;
    }
    public String getAlertGroup() {
        return alertGroup;
    }
    public String getAlertKey() {
        return alertKey;
    }
    public String getNode() {
        return node;
    }
    public String getNodeAlias() {
        return nodeAlias;
    }
    public String getSummary() {
        return summary;
    }
    public String getEventId() {
    	return eventId;
    }
    public String getExtAttr() {
    	return extAttr;
    }
    public Date getFirstOccurrence() {
        return firstOccurrence;
    }
    public Date getLastOccurrence() {
        return lastOccurrence;
    }
    public Date getStateChange() {
        return stateChange;
    }
    public int getEventClass() {
        return eventClass;
    }
    public int getSeverity() {
        return severity;
    }
    public int getTally() {
        return tally;
    }
    public int getSerial() {
        return serial;
    }
    public int getType() {
        return type;
    }
    public int getGrade() {
    	return grade;
    }
    public int getExpireTime() {
    	return expireTime;
    }
    public String getServerName() {
    	return serverName;
    }
    public String getUrl() {
    	return url;
    }
    public String getLocation() {
    	return location;
    }
    public String getCustomer() {
    	return customer;
    }
    public String getService() {
    	return service;
    }
    public String getTecHostname() {
    	return tecHostname;
    }
    public String getLocalNodeAlias() {
    	return localNodeAlias;
    }
    public String getLocalPriObj() {
    	return localPriObj;
    }
    public String getLocalRootObj() {
    	return localRootObj;
    }
    public String getLocalSecObj() {
    	return localSecObj;
    }
    // Setters
    public void setAck(boolean ack) {
        this.ack = ack;
    }
    public void setAck(int ack) {
    	this.ack = (ack != 0);
    }
    public void setSerial(int serial) {
        this.serial = serial;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setGrade(int grade) {
    	this.grade = grade;
    }
    public void setFirstOccurrence(Date date) {
        this.firstOccurrence = date;
    }
    public void setFirstOccurrence(Long date) {
        this.firstOccurrence = new Date(date * 1000);
    }
    public void setLastOccurrence(Long date) {
        this.lastOccurrence = new Date(date * 1000);
    }
    public void setStateChange(Long date) {
        this.stateChange = new Date(date * 1000);
    }
    public void setIdentifier(String id) {
        identifier = cf.convertString(id);
    }
    public void setAgent(String agent) {
        this.agent = cf.convertString(agent);
    }
    public void setManager(String manager) {
        this.manager = cf.convertString(manager);
    }
    public void setBsm_Identity(String bsmid) {
        bsm_Identity = cf.convertString(bsmid);
    }
    public void setBsm_SubIdentity(String bsmsubid) {
        bsm_SubIdentity = cf.convertString(bsmsubid);
    }
    public void setAlertKey(String aKey) {
        alertKey = cf.convertString(aKey);
    }
    public void setAlertGroup(String aGroup) {
        alertGroup = cf.convertString(aGroup);
    }
    public void setNode(String node) {
        this.node = cf.convertString(node);
    }
    public void setNodeAlias(String alias) {
        this.nodeAlias = cf.convertString(alias);
    }
    public void setSummary(String summary) {
        this.summary = cf.convertString(summary);
    }
    public void setEventId(String evId) {
    	eventId = cf.convertString(evId);
    }
    public void setExtAttr(String extAttr) {
    	this.extAttr = cf.convertString(extAttr);
    }
    public void setServerName(String server) {
    	serverName = cf.convertString(server);
    }
    public void setUrl(String url) {
    	this.url = cf.convertString(url);
    }
    public void setLocation(String location) {
    	this.location = cf.convertString(location);
    }
    public void setCustomer(String customer) {
    	this.customer = cf.convertString(customer);
    }
    public void setService(String service) {
    	this.service = cf.convertString(service);
    }
    public void setTecHostname(String techost) {
    	tecHostname = cf.convertString(techost);
    }
    public void setLocalNodeAlias(String localNodeAlias) {
    	this.localNodeAlias = cf.convertString(localNodeAlias);
    }
    public void setLocalPriObj(String lpo) {
    	this.localPriObj = cf.convertString(lpo);
    }
    public void setLocalRootObj(String lro) {
    	this.localRootObj = cf.convertString(lro);
    }
    public void setLocalSecObj(String lso) {
    	this.localSecObj = cf.convertString(lso);
    }
    public void setEventClass(int eventClass) {
        this.eventClass = eventClass;
    }
    public void setSeverity(int sev) {
        severity = sev;
    }
    public void setExpireTime(int expTime) {
    	expireTime = expTime;
    }
    public void setTally(int tally) {
    	this.tally = tally;
    }

    @Override
    public String toString() {
        return String.format("Event: First=%tc, Severity=%d, Node=%s, Summary=%s", firstOccurrence, severity, node, summary);
    }
}
