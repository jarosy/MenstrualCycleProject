package jarosyjarosy.menstrualcycleproject.validators;

import android.content.Context;
import jarosyjarosy.menstrualcycleproject.models.Cycle;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class CycleValidator {

    private DateTimeFormatter appDateFormat = DateTimeFormat.forPattern("dd.MM.yyyy");

    public boolean validateCycle(Context context, Cycle cycleToCheck) {
        return checkIfCyclesDontCross(context, cycleToCheck);
    }

    private boolean checkIfCyclesDontCross(Context context, Cycle cycleToCheck) {
        DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
        dbAdapter.open();
        List<Cycle> allCycles = dbAdapter.getAllCycles();
        for (Cycle cycle : allCycles) {
            if(appDateFormat.print(cycle.getStartDate()).equals(appDateFormat.print(cycleToCheck.getStartDate()))) {
                return false;
            }
            if (new Interval(cycle.getStartDate(), dbAdapter.getLastDayfromCycle(cycle.getCycleId()).getCreateDate()).contains(cycleToCheck.getStartDate())) {
                return false;
            }
        }
        dbAdapter.close();
        return true;
    }
}
