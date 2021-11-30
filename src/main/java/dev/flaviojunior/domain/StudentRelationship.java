package dev.flaviojunior.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.flaviojunior.domain.enumeration.TypeOfRelationship;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * StudentRelationship.
 */
@Entity
@Table(name = "student_relationship")
public class StudentRelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGeneratorStudentRelationship")
    @SequenceGenerator(name = "sequenceGeneratorStudentRelationship", allocationSize = 1)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeOfRelationship type;

    @ManyToOne
    @JsonIgnoreProperties(value = {"person", "address"}, allowSetters = true)
    private Student student;

    @ManyToOne
    private Person person;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentRelationship id(Long id) {
        this.id = id;
        return this;
    }

    public TypeOfRelationship getType() {
        return this.type;
    }

    public void setType(TypeOfRelationship type) {
        this.type = type;
    }

    public StudentRelationship type(TypeOfRelationship type) {
        this.type = type;
        return this;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentRelationship student(Student student) {
        this.setStudent(student);
        return this;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public StudentRelationship person(Person person) {
        this.setPerson(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentRelationship)) {
            return false;
        }
        return id != null && id.equals(((StudentRelationship) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentRelationship{" +
                "id=" + getId() +
                ", type='" + getType() + "'" +
                "}";
    }
}
