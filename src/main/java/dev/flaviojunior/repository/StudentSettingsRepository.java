package dev.flaviojunior.repository;

import dev.flaviojunior.domain.StudentSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentSettingsRepository extends JpaRepository<StudentSettings, Long> {
}
