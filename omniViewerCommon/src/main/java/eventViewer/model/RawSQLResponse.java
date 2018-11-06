package eventViewer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RawSQLResponse {
	
	public String result;
	public String[] headers;
	public ArrayList<ArrayList<Object>> rawData;

	public RawSQLResponse() {}
	public void setProps(List<Map<String,Object>> rawOutput) {
		if (! rawOutput.isEmpty() && rawOutput.size() != 0) {
			headers = rawOutput.get(0).keySet().toArray(new String[0]);
			rawData = convertMap2Array(rawOutput);
			result = "Select "+ rawOutput.size() + " rows";
		} else {
			result = "No records";
		}
	}
	/**
     * table - ArrayList values
     * table[0] = columns from sql select
     * @return table
     */
    public ArrayList<ArrayList<Object>> convertMap2Array(List<Map<String,Object>> rawOutput) {
    	ArrayList<ArrayList<Object>> table = new ArrayList<ArrayList<Object>>();
    	for (Map<String,Object> el: rawOutput) {
    		ArrayList<Object> row = new ArrayList<Object>();
    		for (String s: headers) {
    			row.add(el.get(s));
    		}
    		table.add(row);
    	}
    	return table;
    }
}
