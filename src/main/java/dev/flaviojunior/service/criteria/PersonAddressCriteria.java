package dev.flaviojunior.service.criteria;

import dev.flaviojunior.common.service.Criteria;
import dev.flaviojunior.common.service.filter.Filter;
import dev.flaviojunior.common.service.filter.LongFilter;
import dev.flaviojunior.domain.enumeration.TypeOfAddress;

import java.io.Serializable;
import java.util.Objects;

public class PersonAddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;
    private LongFilter id;
    private TypeOfAddressFilter type;
    private LongFilter addressId;
    private LongFilter personId;

    public PersonAddressCriteria() {
    }

    public PersonAddressCriteria(PersonAddressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
    }

    @Override
    public PersonAddressCriteria copy() {
        return new PersonAddressCriteria(this);
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

    public TypeOfAddressFilter getType() {
        return type;
    }

    public void setType(TypeOfAddressFilter type) {
        this.type = type;
    }

    public TypeOfAddressFilter type() {
        if (type == null) {
            type = new TypeOfAddressFilter();
        }
        return type;
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
        final PersonAddressCriteria that = (PersonAddressCriteria) o;
        return (
                Objects.equals(id, that.id) &&
                        Objects.equals(type, that.type) &&
                        Objects.equals(addressId, that.addressId) &&
                        Objects.equals(personId, that.personId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, addressId, personId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonAddressCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (addressId != null ? "addressId=" + addressId + ", " : "") +
                (personId != null ? "personId=" + personId + ", " : "") +
                "}";
    }

    /**
     * Class for filtering TypeOfAddress
     */
    public static class TypeOfAddressFilter extends Filter<TypeOfAddress> {

        public TypeOfAddressFilter() {
        }

        public TypeOfAddressFilter(TypeOfAddressFilter filter) {
            super(filter);
        }

        @Override
        public TypeOfAddressFilter copy() {
            return new TypeOfAddressFilter(this);
        }
    }
}
