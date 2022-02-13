package com.tms.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConfigMap {

    private List<DBConfig> configs = new ArrayList<>();
    private Map<String, DBConfig> configMaps = null;

    private String filePath;

    private static Object lock = new Object();


    private void initMap() {
        if (configMaps == null) {
            synchronized (lock) {
                if (configMaps == null) {
                    configMaps = new HashMap<>(configs.size());
                    for (DBConfig config : configs) {
                        configMaps.put(config.getFuncName(), config);
                    }
                }
            }
        }
    }
    public DBConfig lookup(String fncName) {
        initMap();
        if (!configMaps.isEmpty()) {
            return configMaps.get(fncName);
        }
        return null;
    }

    public List<DBConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<DBConfig> configs) {
        this.configs = configs;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
