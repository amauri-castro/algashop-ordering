package com.algashop.ordering.infrastructure.persistence.customer;

import com.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString(of = "id")
@Entity
@Table(name = "customer")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class CustomerPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String document;
    private Boolean promotionNotificationsAllowed;
    private Boolean archived;
    private OffsetDateTime registeredAt;
    private OffsetDateTime archivedAt;
    private Integer loyaltyPoints;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "street", column = @Column(name = "address_street")),
                    @AttributeOverride(name = "number", column = @Column(name = "address_number")),
                    @AttributeOverride(name = "complement", column = @Column(name = "address_complement")),
                    @AttributeOverride(name = "neighborhood", column = @Column(name = "address_neighborhood")),
                    @AttributeOverride(name = "city", column = @Column(name = "address_city")),
                    @AttributeOverride(name = "state", column = @Column(name = "address_state")),
                    @AttributeOverride(name = "zipCode", column = @Column(name = "address_zip_code"))
            }
    )
    private AddressEmbeddable address;

    @Version
    private Long version;

    @CreatedBy
    private UUID createdByUserId;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @Builder
    public CustomerPersistenceEntity(UUID id, String firstName,
                                     String lastName, LocalDate birthDate,
                                     String email, String phone, String document,
                                     Boolean promotionNotificationsAllowed, Boolean archived,
                                     OffsetDateTime registeredAt, OffsetDateTime archivedAt,
                                     Integer loyaltyPoints, AddressEmbeddable address,
                                     Long version, UUID createdByUserId, UUID lastModifiedByUserId,
                                     OffsetDateTime lastModifiedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.document = document;
        this.promotionNotificationsAllowed = promotionNotificationsAllowed;
        this.archived = archived;
        this.registeredAt = registeredAt;
        this.archivedAt = archivedAt;
        this.loyaltyPoints = loyaltyPoints;
        this.address = address;
        this.version = version;
        this.createdByUserId = createdByUserId;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.lastModifiedAt = lastModifiedAt;
    }
}
