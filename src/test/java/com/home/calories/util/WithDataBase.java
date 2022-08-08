package com.home.calories.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@AutoConfigureMockMvc
@ExtendWith(TruncateExtension.class)
@SpringBootTest
public class WithDataBase {

    @Autowired
    protected ApiCaller caller;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected Repo repo;

    /**
     * for debug on embedded postgres
     */
    @SneakyThrows
    public String getConnectionString() {
        return jdbcTemplate.getDataSource().getConnection().getMetaData().getURL();
    }

}
