package jarosyjarosy.mentrualcycleproject.models;

import java.util.Date;

public class Cycle {
    private Long cycleId;
    private Date startDate;
    private Date endDate;
    private Integer peakOfMucus;
    private Integer peakOfCervix;

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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
