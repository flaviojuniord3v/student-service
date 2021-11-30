package dev.flaviojunior.domain.enumeration;

/**
 * The TypeOfAddress enumeration.
 */
public enum TypeOfAddress {
    HOME("Home"),
    WORK("Work"),
    OTHER("Other");

    private final String value;

    TypeOfAddress(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
