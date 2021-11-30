package dev.flaviojunior.repository;

import dev.flaviojunior.domain.StudentRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentRelationship entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRelationshipRepository
        extends JpaRepository<StudentRelationship, Long>, JpaSpecificationExecutor<StudentRelationship> {
}
