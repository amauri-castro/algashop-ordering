package com.algashop.ordering.infrastructure.persistence.repository;

import com.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import com.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SpringDataAuditingConfig.class)
class CustomerPersistenceEntityRepositoryIT {

    private final CustomerPersistenceEntityRepository persistenceEntityRepository;

    @Autowired
    public CustomerPersistenceEntityRepositoryIT(CustomerPersistenceEntityRepository persistenceEntityRepository) {
        this.persistenceEntityRepository = persistenceEntityRepository;
    }

    @Test
    public void shouldPersist() {
        CustomerPersistenceEntity entity = CustomerPersistenceEntityTestDataBuilder.existing().build();

        persistenceEntityRepository.saveAndFlush(entity);
        Assertions.assertThat(persistenceEntityRepository.existsById(entity.getId())).isTrue();

        CustomerPersistenceEntity savedEntity = persistenceEntityRepository
                .findById(entity.getId()).orElseThrow();

        Assertions.assertThat(savedEntity).isNotNull();
    }

    @Test
    public void shouldCount() {
        long customersCount = persistenceEntityRepository.count();

        Assertions.assertThat(customersCount).isZero();
    }

    @Test
    public void shouldSetAuditingValues() {
        CustomerPersistenceEntity entity = CustomerPersistenceEntityTestDataBuilder.existing().build();
        entity = persistenceEntityRepository.saveAndFlush(entity);

        Assertions.assertThat(entity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedByUserId()).isNotNull();
    }

}