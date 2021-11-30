package dev.flaviojunior.domain;

import dev.flaviojunior.domain.enumeration.TypeOfAddress;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * PersonAddress.
 */
@Entity
@Table(name = "person_address")
public class PersonAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGeneratorPersonAddress")
    @SequenceGenerator(name = "sequenceGeneratorPersonAddress", allocationSize = 1)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeOfAddress type;

    @ManyToOne
    private Address address;

    @ManyToOne
    private Person person;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonAddress id(Long id) {
        this.id = id;
        return this;
    }

    public TypeOfAddress getType() {
        return this.type;
    }

    public void setType(TypeOfAddress type) {
        this.type = type;
    }

    public PersonAddress type(TypeOfAddress type) {
        this.type = type;
        return this;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public PersonAddress address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public PersonAddress person(Person person) {
        this.setPerson(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonAddress)) {
            return false;
        }
        return id != null && id.equals(((PersonAddress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonAddress{" +
                "id=" + getId() +
                ", type='" + getType() + "'" +
                "}";
    }
}
