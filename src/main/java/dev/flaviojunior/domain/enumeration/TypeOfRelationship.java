package dev.flaviojunior.domain.enumeration;

/**
 * The TypeOfRelationship enumeration.
 */
public enum TypeOfRelationship {
    FATHER("Father"),
    MOTHER("Mother");

    private final String value;

    TypeOfRelationship(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
