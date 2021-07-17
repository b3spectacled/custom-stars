package com.bespectacled.customstars.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.logging.log4j.Level;

import com.bespectacled.customstars.CustomStars;

/*
 * Some weird, naive observer thing
 */
public class CustomStarsConfigObserver {
    private static final Map<String, Object> OPTIONS = new LinkedHashMap<>();
    private static final Map<String, Supplier<Object>> DEFAULT_OPTIONS = new LinkedHashMap<>();
    
    public void registerObservable(String option, Supplier<Object> supplier) {
        OPTIONS.put(option, supplier.get());
        DEFAULT_OPTIONS.put(option, supplier);
    }
    
    public boolean update() {
        boolean updated = false;
        
        for (String option : OPTIONS.keySet()) {
            Object newValue = DEFAULT_OPTIONS.get(option).get();
            
            if (!OPTIONS.get(option).equals(newValue)) {
                CustomStars.log(Level.INFO, "Setting '" + option + "' was updated.");
                
                OPTIONS.put(option, newValue);
                updated = true;
            }
        }
        
        return updated;
    }
}
