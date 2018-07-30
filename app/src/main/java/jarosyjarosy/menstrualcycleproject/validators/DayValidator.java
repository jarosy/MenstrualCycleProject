package jarosyjarosy.menstrualcycleproject.validators;

import android.content.Context;
import android.widget.Toast;
import jarosyjarosy.menstrualcycleproject.models.Cycle;
import jarosyjarosy.menstrualcycleproject.models.Day;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;

import java.util.List;

public class DayValidator {

    private DatabaseAdapter dbAdapter;

    public boolean validateDay(Context context, Day dayToCheck,  Cycle cycleToCheck) {
        return checkIfDayExist(context, dayToCheck)
                && canDayBeInCycle(context, dayToCheck, cycleToCheck)
                && isThereNewerCycle(context, dayToCheck, cycleToCheck);
    }

    private boolean checkIfDayExist(Context context, Day dayToCheck) {
        dbAdapter = new DatabaseAdapter(context);
        dbAdapter.open();
        List<Day> allDayList = dbAdapter.getAllDays();
        dbAdapter.close();
        for (Day day : allDayList) {
            if(day.getCreateDate().isEqual(dayToCheck.getCreateDate())) {
                Toast.makeText(context, "Istnieje już dzień z taką datą!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private boolean canDayBeInCycle(Context context, Day dayToCheck, Cycle cycleToCheck) {
        if(dayToCheck.getCreateDate().isAfter(cycleToCheck.getStartDate().minusDays(1))) {
            return true;
        }
        Toast.makeText(context, "Data jest młodsza niż data rozpoczęcia cyklu.", Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean isThereNewerCycle(Context context, Day dayToCheck, Cycle cycleToCheck) {
        dbAdapter = new DatabaseAdapter(context);
        dbAdapter.open();
        List<Cycle> allCycles = dbAdapter.getAllCycles();
        dbAdapter.close();
        for (Cycle cycle : allCycles) {
            if(cycle.getStartDate().isAfter(cycleToCheck.getStartDate())
                    && dayToCheck.getCreateDate().isAfter(cycle.getStartDate().minusDays(1))) {
                Toast.makeText(context, "Dzień pasuje do nowszego cyklu.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

}
