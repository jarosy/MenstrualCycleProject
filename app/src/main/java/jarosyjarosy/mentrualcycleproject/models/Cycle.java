package jarosyjarosy.mentrualcycleproject.models;

import java.util.Date;
import java.util.List;

public class Cycle {
    private Date startDate;
    private Date endDate;
    private List<Day> days;
    private Integer peakOfMucus;
    private Integer peakOfCervix;

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

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
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
