package jarosyjarosy.menstrualcycleproject.validators;

import android.content.Context;
import jarosyjarosy.menstrualcycleproject.models.Day;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;

public class DayValidator {

    DatabaseAdapter dbAdapter;

    public void checkIfDayExist(Context context, Day dayToCheck) {
        dbAdapter = new DatabaseAdapter(context);
    }
}
