package jarosyjarosy.mentrualcycleproject.models;

import java.util.Date;
import java.util.List;

public class Day {
    private Integer dayId;
    private Date createDate;
    private Integer dayOfCycle;
    private Float temperature;
    private String bleeding;
    private List<String> mucus;
    private String dilationOfCervix;
    private String positionOfCervix;
    private String hardnessOfCervix;
    private Boolean ovulatoryPain;
    private Boolean tensionInTheBreasts;
    private String otherSymptoms;
    private Boolean intercourse;

    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
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

    public String getBleeding() {
        return bleeding;
    }

    public void setBleeding(String bleeding) {
        this.bleeding = bleeding;
    }

    public List<String> getMucus() {
        return mucus;
    }

    public void setMucus(List<String> mucus) {
        this.mucus = mucus;
    }

    public String getDilationOfCervix() {
        return dilationOfCervix;
    }

    public void setDilationOfCervix(String dilationOfCervix) {
        this.dilationOfCervix = dilationOfCervix;
    }

    public String getPositionOfCervix() {
        return positionOfCervix;
    }

    public void setPositionOfCervix(String positionOfCervix) {
        this.positionOfCervix = positionOfCervix;
    }

    public String getHardnessOfCervix() {
        return hardnessOfCervix;
    }

    public void setHardnessOfCervix(String hardnessOfCervix) {
        this.hardnessOfCervix = hardnessOfCervix;
    }

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

}
