package com.home.calories.util;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.event.ApplicationEventsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;


@AutoConfigureMockMvc
@ExtendWith(TruncateExtension.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        MockitoTestExecutionListener.class,
        ResetMocksTestExecutionListener.class,
        ApplicationEventsTestExecutionListener.class,
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = "dataSource")
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
