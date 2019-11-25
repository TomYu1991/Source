package org.mybatis.spring;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DataSourceHolder {

    private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();

    public static void setDataSources(String dataSource) {
        dataSources.set(dataSource);
    }

    public static String getDataSources() {
        return dataSources.get();
    }
}
