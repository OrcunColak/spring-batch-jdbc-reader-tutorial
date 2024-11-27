package com.colak.springtutorial.config;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

public class NumberWriter implements ItemWriter<Map<Integer, Integer>> {

    protected final DataSource dataSource;

    protected final JdbcTemplate jdbcTemplate;

    public NumberWriter(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void write(Chunk<? extends Map<Integer, Integer>> chunk) {

        for (Map<Integer, Integer> value : chunk.getItems()) {

            for (Map.Entry<Integer, Integer> mapValue : value.entrySet())
                jdbcTemplate.update("UPDATE numbers SET number = (?) WHERE id = (?)", mapValue.getValue(), mapValue.getKey());
        }
    }
}
