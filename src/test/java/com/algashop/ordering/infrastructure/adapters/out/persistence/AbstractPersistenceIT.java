package com.algashop.ordering.infrastructure.adapters.out.persistence;

import com.algashop.ordering.infrastructure.config.auditing.SpringDataAuditingConfig;
import com.algashop.ordering.utils.TestcontainerPostgreSQLConfig;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainerPostgreSQLConfig.class, SpringDataAuditingConfig.class})
public abstract class AbstractPersistenceIT {
}
