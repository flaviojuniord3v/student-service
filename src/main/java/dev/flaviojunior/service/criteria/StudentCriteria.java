package dev.flaviojunior.service.criteria;

import dev.flaviojunior.common.service.Criteria;
import dev.flaviojunior.common.service.filter.IntegerFilter;
import dev.flaviojunior.common.service.filter.LongFilter;
import dev.flaviojunior.common.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

public class StudentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter registrationNumber;

    private StringFilter photo;

    private LongFilter personId;

    private LongFilter addressId;

    public StudentCriteria() {
    }

    public StudentCriteria(StudentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.registrationNumber = other.registrationNumber == null ? null : other.registrationNumber.copy();
        this.photo = other.photo == null ? null : other.photo.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
    }

    @Override
    public StudentCriteria copy() {
        return new StudentCriteria(this);
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

    public IntegerFilter getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(IntegerFilter registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public IntegerFilter registrationNumber() {
        if (registrationNumber == null) {
            registrationNumber = new IntegerFilter();
        }
        return registrationNumber;
    }

    public StringFilter getPhoto() {
        return photo;
    }

    public void setPhoto(StringFilter photo) {
        this.photo = photo;
    }

    public StringFilter photo() {
        if (photo == null) {
            photo = new StringFilter();
        }
        return photo;
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

    public LongFilter getAddressId() {
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public LongFilter addressId() {
        if (addressId == null) {
            addressId = new LongFilter();
        }
        return addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentCriteria that = (StudentCriteria) o;
        return (
                Objects.equals(id, that.id) &&
                        Objects.equals(registrationNumber, that.registrationNumber) &&
                        Objects.equals(photo, that.photo) &&
                        Objects.equals(personId, that.personId) &&
                        Objects.equals(addressId, that.addressId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registrationNumber, photo, personId, addressId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (registrationNumber != null ? "registrationNumber=" + registrationNumber + ", " : "") +
                (photo != null ? "photo=" + photo + ", " : "") +
                (personId != null ? "personId=" + personId + ", " : "") +
                (addressId != null ? "addressId=" + addressId + ", " : "") +
                "}";
    }
}
