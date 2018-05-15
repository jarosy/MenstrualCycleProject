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
        for(CervixHardnessType b : CervixHardnessType.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
