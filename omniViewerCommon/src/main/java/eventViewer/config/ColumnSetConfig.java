/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventViewer.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author VasilyevIG
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "columns")
public class ColumnSetConfig {

    private final List<ColSet> colSets = new ArrayList<ColSet>();
    private final ArrayList<String> colDetails = new ArrayList<String>();

    public ArrayList<String> getColDetails() {
    	return this.colDetails;
    }
    public List<ColSet> getColSets() {
        return this.colSets;
    }
    public boolean isSet() {
        if (colSets == null && colDetails == null) {
            return false;
        }
        return !(colSets.isEmpty() && colDetails.isEmpty());
    }

    /*
    @PostConstruct
    public void init() {
        System.out.println("Init...");
        for(FilterSet f : getFilterSets()) {
            System.out.println("Filter: "+f);
        }
    }
     */

    public static class ColSet {

        private String col;
        private String header;

        public ColSet(String col, String header) {
            this.col = col;
            this.header = header;
        }

        public ColSet() {
        }

        public String getCol() {
            return col;
        }

        public String getHeader() {
            return header;
        }

        public void setCol(String col) {
            this.col = col;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        @Override
        public String toString() {
            return col + " -> " + header;
        }
    }
}
