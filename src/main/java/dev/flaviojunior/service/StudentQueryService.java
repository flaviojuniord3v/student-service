package dev.flaviojunior.service;

import dev.flaviojunior.common.service.QueryService;
import dev.flaviojunior.domain.Address_;
import dev.flaviojunior.domain.Person_;
import dev.flaviojunior.domain.Student;
import dev.flaviojunior.domain.Student_;
import dev.flaviojunior.repository.StudentRepository;
import dev.flaviojunior.service.criteria.StudentCriteria;
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
public class StudentQueryService extends QueryService<Student> {

    private final Logger log = LoggerFactory.getLogger(StudentQueryService.class);

    private final StudentRepository studentRepository;

    public StudentQueryService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<Student> findByCriteria(StudentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<Student> findByCriteria(StudentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(StudentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.count(specification);
    }

    protected Specification<Student> createSpecification(StudentCriteria criteria) {
        Specification<Student> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Student_.id));
            }
            if (criteria.getRegistrationNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegistrationNumber(), Student_.registrationNumber));
            }
            if (criteria.getPhoto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoto(), Student_.photo));
            }
            if (criteria.getPersonId() != null) {
                specification =
                        specification.and(
                                buildSpecification(criteria.getPersonId(), root -> root.join(Student_.person, JoinType.LEFT).get(Person_.id))
                        );
            }
            if (criteria.getAddressId() != null) {
                specification =
                        specification.and(
                                buildSpecification(criteria.getAddressId(), root -> root.join(Student_.address, JoinType.LEFT).get(Address_.id))
                        );
            }
        }
        return specification;
    }
}
