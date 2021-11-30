package dev.flaviojunior.service;

import dev.flaviojunior.common.service.QueryService;
import dev.flaviojunior.domain.Address_;
import dev.flaviojunior.domain.PersonAddress;
import dev.flaviojunior.domain.PersonAddress_;
import dev.flaviojunior.domain.Person_;
import dev.flaviojunior.repository.PersonAddressRepository;
import dev.flaviojunior.service.criteria.PersonAddressCriteria;
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
public class PersonAddressQueryService extends QueryService<PersonAddress> {

    private final Logger log = LoggerFactory.getLogger(PersonAddressQueryService.class);

    private final PersonAddressRepository personAddressRepository;

    public PersonAddressQueryService(PersonAddressRepository personAddressRepository) {
        this.personAddressRepository = personAddressRepository;
    }

    @Transactional(readOnly = true)
    public List<PersonAddress> findByCriteria(PersonAddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PersonAddress> specification = createSpecification(criteria);
        return personAddressRepository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<PersonAddress> findByCriteria(PersonAddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PersonAddress> specification = createSpecification(criteria);
        return personAddressRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(PersonAddressCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PersonAddress> specification = createSpecification(criteria);
        return personAddressRepository.count(specification);
    }

    protected Specification<PersonAddress> createSpecification(PersonAddressCriteria criteria) {
        Specification<PersonAddress> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PersonAddress_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), PersonAddress_.type));
            }
            if (criteria.getAddressId() != null) {
                specification =
                        specification.and(
                                buildSpecification(
                                        criteria.getAddressId(),
                                        root -> root.join(PersonAddress_.address, JoinType.LEFT).get(Address_.id)
                                )
                        );
            }
            if (criteria.getPersonId() != null) {
                specification =
                        specification.and(
                                buildSpecification(criteria.getPersonId(), root -> root.join(PersonAddress_.person, JoinType.LEFT).get(Person_.id))
                        );
            }
        }
        return specification;
    }
}
