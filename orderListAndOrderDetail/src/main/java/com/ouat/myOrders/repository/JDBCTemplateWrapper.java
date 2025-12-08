package com.ouat.myOrders.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class JDBCTemplateWrapper {

    private static final int ONE_SECOND = 1000;

    private final Logger logger = LoggerFactory.getLogger(JDBCTemplateWrapper.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public <T> List<T> findUsingNamedParameters(String sql, Map<String, Object> mapOfparameters, RowMapper<T> rowMapper) {
        List<T> results = namedParameterJdbcTemplate.query(sql, mapOfparameters, rowMapper);
        return results;
    }

    public <T> List<T> find(String sql, RowMapper<T> rowMapper, Object... params) {
        StopWatch watch = new StopWatch();
        int returnedRows = 0;
        try {
            List<T> results = jdbcTemplate.query(sql, params, rowMapper);
            returnedRows = results.size();
            return results;
        } finally {
            if (watch.elapsedTime() >= ONE_SECOND)
                logger.info("find|query={}|params={}|returnedRows={}|elapsedTime={}", sql, params, returnedRows, watch.elapsedTime());
        }
    }

    public <T> T findOne(String sql, RowMapper<T> rowMapper, Object... params) {
        StopWatch watch = new StopWatch();
        try {
            return jdbcTemplate.queryForObject(sql, params, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("findOne did not find any result", e);
            return null;
        } finally {
            if (watch.elapsedTime() >= ONE_SECOND)
                logger.info("findOne|query={}|params={}|elapsedTime={}", sql, params, watch.elapsedTime());
        }
    }

    public Integer findInteger(String sql, Object... params) {
        StopWatch watch = new StopWatch();
        try {
            Number number = jdbcTemplate.queryForObject(sql, params, Integer.class);
            return number != null ? number.intValue() : 0;
        } catch (EmptyResultDataAccessException e) {
            logger.debug("findInteger did not find any result", e);
            return null;
        } finally {
            if (watch.elapsedTime() >= ONE_SECOND)
                logger.info("findInteger|query={}|params={}|elapsedTime={}", sql, params, watch.elapsedTime());
        }
    }

    public String findString(String sql, Object... params) {
        StopWatch watch = new StopWatch();
        try {
            return jdbcTemplate.queryForObject(sql, params, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                    return resultSet.getString(1);
                }
            });
        } catch (EmptyResultDataAccessException e) {
            logger.debug("findString did not find any result", e);
            return null;
        } finally {
            if (watch.elapsedTime() >= ONE_SECOND)
                logger.info("findString|query={}|params={}|elapsedTime={}", sql, params, watch.elapsedTime());
        }
    }

    public int execute(String sql, Object... params) {
        StopWatch watch = new StopWatch();
        int updatedRows = 0;
        try {
            updatedRows = jdbcTemplate.update(sql, params);
            return updatedRows;
        } finally {
            if (watch.elapsedTime() >= ONE_SECOND)
                logger.info("execute|query={}|params={}|updatedRows={}|elapsedTime={}", sql, params, updatedRows, watch.elapsedTime());
        }
    }

    public int[] batchExecute(String sql, List<Object[]> params) {
        StopWatch watch = new StopWatch();
        int totalUpdatedRows = 0;
        try {
            int[] results = jdbcTemplate.batchUpdate(sql, params);
            for (int updatedRows : results) {
                totalUpdatedRows += updatedRows;
            }
            return results;
        } finally {
            if (watch.elapsedTime() >= ONE_SECOND)
                logger.info("batchExecute|query={}|params={}|totalUpdatedRows={}|elapsedTime={}", sql, params, totalUpdatedRows, watch.elapsedTime());
        }
    }




}

