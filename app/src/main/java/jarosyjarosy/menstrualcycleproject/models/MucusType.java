package jarosyjarosy.menstrualcycleproject.models;

public enum MucusType {
    WET("Mokro / Ślisko"),
    STRETCHY("Rozciągliwy"),
    TRANSPARENT("Przejrzysty"),
    HUMID("Wilgotno"),
    STICKY("Lepki / Gęsty"),
    MUZZY("Mętny"),
    DRY("Sucho"),
    ANOMALOUS("Nietypowy");

    private final String text;

    MucusType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static MucusType fromString(String text) {
        for(MucusType b : MucusType.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
