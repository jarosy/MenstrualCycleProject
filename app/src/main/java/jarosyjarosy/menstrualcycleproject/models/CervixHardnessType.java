package jarosyjarosy.menstrualcycleproject.models;

public enum CervixHardnessType {

    HARD("T"),
    SOFT("M");

    private final String text;

    CervixHardnessType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static CervixHardnessType fromString(String text) {
        if (text == null) {
            return null;
        }
        if (text.equalsIgnoreCase("Twarda")) {
            return HARD;
        }
        if (text.equalsIgnoreCase("Miękka")) {
            return SOFT;
        }
        return null;
    }
}
