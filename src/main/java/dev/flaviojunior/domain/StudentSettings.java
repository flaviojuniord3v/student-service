package dev.flaviojunior.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * StudentSettings.
 */
@Entity
@Table(name = "student_settings")
public class StudentSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGeneratorStudentSettings")
    @SequenceGenerator(name = "sequenceGeneratorStudentSettings", allocationSize = 1)
    private Long id;

    @Column(name = "receive_email_notifications")
    private Boolean receiveEmailNotifications;

    @ManyToOne
    @JsonIgnoreProperties(value = {"person", "address"}, allowSetters = true)
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentSettings id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getReceiveEmailNotifications() {
        return this.receiveEmailNotifications;
    }

    public void setReceiveEmailNotifications(Boolean receiveEmailNotifications) {
        this.receiveEmailNotifications = receiveEmailNotifications;
    }

    public StudentSettings receiveEmailNotifications(Boolean receiveEmailNotifications) {
        this.receiveEmailNotifications = receiveEmailNotifications;
        return this;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentSettings student(Student student) {
        this.setStudent(student);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentSettings)) {
            return false;
        }
        return id != null && id.equals(((StudentSettings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentSettings{" +
                "id=" + getId() +
                ", receiveEmailNotifications='" + getReceiveEmailNotifications() + "'" +
                "}";
    }
}
