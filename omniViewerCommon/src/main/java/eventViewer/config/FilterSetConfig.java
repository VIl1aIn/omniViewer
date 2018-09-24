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
@ConfigurationProperties(prefix = "presets")
public class FilterSetConfig {

    private final List<FilterSet> filterSets = new ArrayList<FilterSet>();

    public List<FilterSet> getFilterSets() {
        return this.filterSets;
    }
    public boolean isSet() {
        if (filterSets == null) {
            return false;
        }
        return !filterSets.isEmpty();
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

    public static class FilterSet {

        private String name;
        private String filter;

        public FilterSet(String name, String filter) {
            this.name = name;
            this.filter = filter;
        }

        public FilterSet() {
        }

        public String getName() {
            return name;
        }

        public String getFilter() {
            return filter;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        @Override
        public String toString() {
            return name + " -> " + filter;
        }
    }
}
