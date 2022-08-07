package com.home.calories.util;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;

public class TruncateExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApplicationContext appContext = SpringExtension.getApplicationContext(context);

        JdbcTemplate template = appContext.getBean(JdbcTemplate.class);

        Resource resource = appContext.getResource("classpath:truncate.sql");

        String sql = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);

        template.execute(sql);
    }
}
