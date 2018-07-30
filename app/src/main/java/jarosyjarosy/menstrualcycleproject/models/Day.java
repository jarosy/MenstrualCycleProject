package jarosyjarosy.menstrualcycleproject.models;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

public class Day {
    private Long dayId;
    private DateTime createDate;
    private Integer dayOfCycle;
    private Float temperature;
    private BleedingType bleeding;
    private List<MucusType> mucus;
    private Integer dilationOfCervix;
    private Integer positionOfCervix;
    private CervixHardnessType hardnessOfCervix;
    private Boolean ovulatoryPain;
    private Boolean tensionInTheBreasts;
    private String otherSymptoms;
    private Boolean intercourse;
    private Long cycleId;

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getDayOfCycle() {
        return dayOfCycle;
    }

    public void setDayOfCycle(Integer dayOfCycle) {
        this.dayOfCycle = dayOfCycle;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public BleedingType getBleeding() {
        return bleeding;
    }

    public void setBleeding(BleedingType bleeding) {
        this.bleeding = bleeding;
    }

    public List<MucusType> getMucus() { return mucus; }

    public void setMucus(List<MucusType> mucus) { this.mucus = mucus; }

    public Integer getDilationOfCervix() { return dilationOfCervix; }

    public void setDilationOfCervix(Integer dilationOfCervix) { this.dilationOfCervix = dilationOfCervix; }

    public Integer getPositionOfCervix() { return positionOfCervix; }

    public void setPositionOfCervix(Integer positionOfCervix) { this.positionOfCervix = positionOfCervix; }

    public CervixHardnessType getHardnessOfCervix() { return hardnessOfCervix; }

    public void setHardnessOfCervix(CervixHardnessType hardnessOfCervix) { this.hardnessOfCervix = hardnessOfCervix; }

    public Boolean getOvulatoryPain() {
        return ovulatoryPain;
    }

    public void setOvulatoryPain(Boolean ovulatoryPain) {
        this.ovulatoryPain = ovulatoryPain;
    }

    public Boolean getTensionInTheBreasts() {
        return tensionInTheBreasts;
    }

    public void setTensionInTheBreasts(Boolean tensionInTheBreasts) {
        this.tensionInTheBreasts = tensionInTheBreasts;
    }

    public String getOtherSymptoms() {
        return otherSymptoms;
    }

    public void setOtherSymptoms(String otherSymptoms) {
        this.otherSymptoms = otherSymptoms;
    }

    public Boolean getIntercourse() {
        return intercourse;
    }

    public void setIntercourse(Boolean intercourse) {
        this.intercourse = intercourse;
    }

    public Long getCycleId() { return cycleId; }

    public void setCycleId(Long cycleId) { this.cycleId = cycleId; }
}
