package dev.flaviojunior.repository;

import dev.flaviojunior.domain.PersonAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonAddressRepository extends JpaRepository<PersonAddress, Long>, JpaSpecificationExecutor<PersonAddress> {
}
