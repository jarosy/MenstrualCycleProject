package jarosyjarosy.menstrualcycleproject.models;

import org.joda.time.DateTime;

public class Cycle {
    private Long cycleId;
    private DateTime startDate;
    private DateTime endDate;
    private Integer peakOfMucus;
    private Integer peakOfCervix;

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getPeakOfMucus() {
        return peakOfMucus;
    }

    public void setPeakOfMucus(Integer peakOfMucus) {
        this.peakOfMucus = peakOfMucus;
    }

    public Integer getPeakOfCervix() {
        return peakOfCervix;
    }

    public void setPeakOfCervix(Integer peakOfCervix) {
        this.peakOfCervix = peakOfCervix;
    }
}
