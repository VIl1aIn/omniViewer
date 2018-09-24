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
@ConfigurationProperties(prefix = "colors")
public class ColorSetConfig {

    private final List<ColorSet> colorSets = new ArrayList<ColorSet>();

    public List<ColorSet> getColorSets() {
        return this.colorSets;
    }
    public boolean isSet() {
        if (colorSets == null) {
            return false;
        }
        return !colorSets.isEmpty();
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

    public static class ColorSet {

        private int code;
        private String color;

        public ColorSet(int code, String color) {
            this.code = code;
            this.color = color;
        }

        public ColorSet() {
        }

        public int getCode() {
            return code;
        }

        public String getColor() {
            return color;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return Integer.toString(code) + " -> " + color;
        }
    }
}
