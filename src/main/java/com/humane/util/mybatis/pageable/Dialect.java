package com.humane.util.mybatis.pageable;

public abstract class Dialect {

    /**
     * Return paging sql, no placeholders, limit and offset the direct write died in sql
     */
    public abstract String getLimitString(String sql, int offset, int limit);

    /**
     * The total number of records sql converted to SQL
     *
     * @param sql SQL statements
     * @return The total number of records sql
     */
    public String getCountString(String sql) {
        return "SELECT COUNT(1) FROM (" + sql + ") tmp_count";
    }
}
