package com.algashop.ordering.infrastructure.persistence.disassembler;

import com.algashop.ordering.domain.model.entity.Customer;
import com.algashop.ordering.domain.model.valueobject.*;
import com.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;

public class CustomerPersistenceEntityDisassembler {

    public Customer toDomainEntity(CustomerPersistenceEntity persistenceEntity) {
        return Customer.existing()
                .id(new CustomerId(persistenceEntity.getId()))
                .fullName(new FullName(persistenceEntity.getFirstName(), persistenceEntity.getLastName()))
                .birthDate(persistenceEntity.getBirthDate() != null ? new BirthDate(persistenceEntity.getBirthDate()) : null)
                .email(new Email(persistenceEntity.getEmail()))
                .phone(new Phone(persistenceEntity.getPhone()))
                .document(new Document(persistenceEntity.getDocument()))
                .promotionNotificationsAllowed(persistenceEntity.getPromotionNotificationsAllowed())
                .archived(persistenceEntity.getArchived())
                .registeredAt(persistenceEntity.getRegisteredAt())
                .archivedAt(persistenceEntity.getArchivedAt())
                .loyaltyPoints(new LoyaltyPoints(persistenceEntity.getLoyaltyPoints()))
                .address(toAddressValueObject(persistenceEntity.getAddress()))
                .version(persistenceEntity.getVersion())
                .build();

    }

    private Address toAddressValueObject(AddressEmbeddable addressEmbeddable) {
        if (addressEmbeddable == null) return null;

        return Address.builder()
                .street(addressEmbeddable.getStreet())
                .number(addressEmbeddable.getNumber())
                .complement(addressEmbeddable.getComplement())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .city(addressEmbeddable.getCity())
                .state(addressEmbeddable.getState())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .build();
    }
}
