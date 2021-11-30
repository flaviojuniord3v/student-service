package dev.flaviojunior.service;

import dev.flaviojunior.common.service.QueryService;
import dev.flaviojunior.domain.Person_;
import dev.flaviojunior.domain.StudentRelationship;
import dev.flaviojunior.domain.StudentRelationship_;
import dev.flaviojunior.domain.Student_;
import dev.flaviojunior.repository.StudentRelationshipRepository;
import dev.flaviojunior.service.criteria.StudentRelationshipCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentRelationshipQueryService extends QueryService<StudentRelationship> {

    private final Logger log = LoggerFactory.getLogger(StudentRelationshipQueryService.class);

    private final StudentRelationshipRepository studentRelationshipRepository;

    public StudentRelationshipQueryService(StudentRelationshipRepository studentRelationshipRepository) {
        this.studentRelationshipRepository = studentRelationshipRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentRelationship> findByCriteria(StudentRelationshipCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StudentRelationship> specification = createSpecification(criteria);
        return studentRelationshipRepository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<StudentRelationship> findByCriteria(StudentRelationshipCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudentRelationship> specification = createSpecification(criteria);
        return studentRelationshipRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(StudentRelationshipCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudentRelationship> specification = createSpecification(criteria);
        return studentRelationshipRepository.count(specification);
    }

    protected Specification<StudentRelationship> createSpecification(StudentRelationshipCriteria criteria) {
        Specification<StudentRelationship> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StudentRelationship_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), StudentRelationship_.type));
            }
            if (criteria.getStudentId() != null) {
                specification =
                        specification.and(
                                buildSpecification(
                                        criteria.getStudentId(),
                                        root -> root.join(StudentRelationship_.student, JoinType.LEFT).get(Student_.id)
                                )
                        );
            }
            if (criteria.getPersonId() != null) {
                specification =
                        specification.and(
                                buildSpecification(
                                        criteria.getPersonId(),
                                        root -> root.join(StudentRelationship_.person, JoinType.LEFT).get(Person_.id)
                                )
                        );
            }
        }
        return specification;
    }
}
