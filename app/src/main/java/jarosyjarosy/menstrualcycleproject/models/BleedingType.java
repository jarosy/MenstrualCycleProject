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
        if(text == null || text.equalsIgnoreCase("Nie")) {
            return BLEEDING_NO;
        }
        if (text.equalsIgnoreCase("Tak")) {
            return BLEEDING_YES;
        }
        if (text.equalsIgnoreCase("Plamienie")) {
            return BLEEDING_SPOTTING;
        }
        return null;
    }
}
