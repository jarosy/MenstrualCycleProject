package jarosyjarosy.menstrualcycleproject.models;

public enum BleedingType {
    BLEEDING_NO(" "),
    BLEEDING_YES("K"),
    BLEEDING_SPOTTING("P");

    private final String text;

    BleedingType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static BleedingType fromString(String text) {
        for(BleedingType b : BleedingType.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
