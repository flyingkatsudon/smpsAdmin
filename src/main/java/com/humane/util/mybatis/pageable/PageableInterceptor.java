package com.humane.util.mybatis.pageable;


import com.google.common.base.CaseFormat;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Pagination plugin, use Pageable and Page objects as input and output.
 * As long as the method parameter contains Pageable parameter is sorted automatically.
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PageableInterceptor implements Interceptor {

    private Logger log = LoggerFactory.getLogger(PageableInterceptor.class);

    int MAPPED_STATEMENT_INDEX = 0;
    int PARAMETER_INDEX = 1;
    int ROWBOUNDS_INDEX = 2;

    private Dialect dialect;

    @Override
    public Object intercept(Invocation inv) throws Throwable {
        final Object[] queryArgs = inv.getArgs();

        // Find the method parameters paging request object
        Pageable pageRequest = this.findPageableObject(queryArgs[PARAMETER_INDEX]);

        // If you need to tab
        if (pageRequest != null) {

            final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
            final Object parameter = queryArgs[PARAMETER_INDEX];

            final BoundSql boundSql = ms.getBoundSql(parameter);

            // Delete trailing ';'
            String sql = boundSql.getSql().trim().replaceAll(";$", "");

            // 1. get the total number of records (if required)
            int total = this.queryTotal(sql, ms, boundSql);

            // 2. Get query limit
            // 2.1  Get paging SQL, and complete data preparation
            String orderSql = getOrderString(sql, pageRequest.getSort());
            String limitSql = dialect.getLimitString(orderSql, pageRequest.getOffset(), pageRequest.getPageSize());

            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
            queryArgs[MAPPED_STATEMENT_INDEX] = copyFromNewSql(ms, boundSql, limitSql);

            // 2.2 continue with the remaining steps to obtain query results
            List<?> ret = total > 0 ? (List<?>) inv.proceed() : Collections.emptyList();

            // 3. Composition tab objects
            Page<?> pi = new PageImpl<>(ret, pageRequest, total);

            // 4. MyBatis need to return a List object here is to meet MyBatis and for temporary packaging
            List<Page<?>> tmp = new ArrayList<>(1);
            tmp.add(pi);
            return tmp;
        }

        return inv.proceed();

    }

    private String getOrderString(String sql, Sort sort) {
        if (sort != null) {
            List<String> orders = new ArrayList<>();
            for (Sort.Order order : sort) {
                String column = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, order.getProperty());
                String direction = order.getDirection().name();
                orders.add(column + " " + direction);
            }
            return sql + " ORDER BY " + String.join(",", orders);
        }
        return sql;
    }

    /**
     * Find paging request object in the method parameters
     *
     * @param params Mapper interface method parameter object
     * @return
     */
    private Pageable findPageableObject(Object params) {

        if (params == null) {
            return null;
        }

        // Parameter exhibits a single parameter object
        if (Pageable.class.isAssignableFrom(params.getClass())) {
            return (Pageable) params;
        }

        // Parameter exhibits a plurality ParamMap
        else if (params instanceof ParamMap) {
            @SuppressWarnings("unchecked")
            ParamMap<Object> paramMap = (ParamMap<Object>) params;
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                Object paramValue = entry.getValue();

                if (paramValue != null && Pageable.class.isAssignableFrom(paramValue.getClass())) {
                    return (Pageable) paramValue;
                }
            }
        }

        return null;
    }

    @Override
    public Object plugin(Object target) {
//		return Plugin.wrap(target, this);
        if (Executor.class.isAssignableFrom(target.getClass())) {
            return Plugin.wrap(target, this);
        }

        return target;
    }

    @Override
    public void setProperties(Properties p) {
        String dialectClass = p.getProperty("dialectClass");

        try {
            setDialect((Dialect) Class.forName(dialectClass).newInstance());
        } catch (Exception e) {
            throw new RuntimeException("cannot create dialect instance by dialectClass:" + dialectClass, e);
        }

    }

    /**
     * Query the total number of records
     *
     * @param sql
     * @param mappedStatement
     * @param boundSql
     * @return
     * @throws SQLException
     */
    private int queryTotal(String sql, MappedStatement mappedStatement, BoundSql boundSql) throws SQLException {

        Connection connection = null;
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {

            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();

            String countSql = this.dialect.getCountString(sql);

            countStmt = connection.prepareStatement(countSql);
            BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());

            setParameters(countStmt, mappedStatement, countBoundSql, countBoundSql.getParameterObject());

            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }

            if (log.isDebugEnabled()) {
                log.debug("{}", countStmt.toString().replaceAll("[\\r\\n\\s]+", " "));
                log.debug("count : {}", totalCount);
            }

            return totalCount;
        } catch (SQLException e) {
            log.error("Query total number of records error", e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("exception happens when doing: ResultSet.close()", e);
                }
            }

            if (countStmt != null) {
                try {
                    countStmt.close();
                } catch (SQLException e) {
                    log.error("exception happens when doing: PreparedStatement.close()", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("exception happens when doing: Connection.close()", e);
                }
            }
        }

    }

    /**
     * For SQL parameters (?) Set value
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    private MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);
        return copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    //see: MapperBuilderAssistant
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuffer keyProperties = new StringBuffer();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        //setStatementTimeout()
        builder.timeout(ms.getTimeout());

        //setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        //setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        //setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }
}
