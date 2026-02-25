package com.algashop.ordering.core.domain.model;

import com.algashop.ordering.utils.TestcontainerPostgreSQLConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainerPostgreSQLConfig.class)
public abstract class AbstractDomainIT {

}
