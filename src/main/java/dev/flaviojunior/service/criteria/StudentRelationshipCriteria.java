package dev.flaviojunior.service.criteria;

import dev.flaviojunior.common.service.Criteria;
import dev.flaviojunior.common.service.filter.Filter;
import dev.flaviojunior.common.service.filter.LongFilter;
import dev.flaviojunior.domain.enumeration.TypeOfRelationship;

import java.io.Serializable;
import java.util.Objects;

public class StudentRelationshipCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;
    private LongFilter id;
    private TypeOfRelationshipFilter type;
    private LongFilter studentId;
    private LongFilter personId;

    public StudentRelationshipCriteria() {
    }

    public StudentRelationshipCriteria(StudentRelationshipCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
    }

    @Override
    public StudentRelationshipCriteria copy() {
        return new StudentRelationshipCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public TypeOfRelationshipFilter getType() {
        return type;
    }

    public void setType(TypeOfRelationshipFilter type) {
        this.type = type;
    }

    public TypeOfRelationshipFilter type() {
        if (type == null) {
            type = new TypeOfRelationshipFilter();
        }
        return type;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public LongFilter studentId() {
        if (studentId == null) {
            studentId = new LongFilter();
        }
        return studentId;
    }

    public LongFilter getPersonId() {
        return personId;
    }

    public void setPersonId(LongFilter personId) {
        this.personId = personId;
    }

    public LongFilter personId() {
        if (personId == null) {
            personId = new LongFilter();
        }
        return personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentRelationshipCriteria that = (StudentRelationshipCriteria) o;
        return (
                Objects.equals(id, that.id) &&
                        Objects.equals(type, that.type) &&
                        Objects.equals(studentId, that.studentId) &&
                        Objects.equals(personId, that.personId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, studentId, personId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentRelationshipCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (studentId != null ? "studentId=" + studentId + ", " : "") +
                (personId != null ? "personId=" + personId + ", " : "") +
                "}";
    }

    /**
     * Class for filtering TypeOfRelationship
     */
    public static class TypeOfRelationshipFilter extends Filter<TypeOfRelationship> {

        public TypeOfRelationshipFilter() {
        }

        public TypeOfRelationshipFilter(TypeOfRelationshipFilter filter) {
            super(filter);
        }

        @Override
        public TypeOfRelationshipFilter copy() {
            return new TypeOfRelationshipFilter(this);
        }
    }
}
