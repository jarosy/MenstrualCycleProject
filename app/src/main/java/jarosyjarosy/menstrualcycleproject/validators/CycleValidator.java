package jarosyjarosy.menstrualcycleproject.validators;

import android.content.Context;
import jarosyjarosy.menstrualcycleproject.models.Cycle;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;

public class CycleValidator {

    DatabaseAdapter dbAdapter;

    public void checkIfCyclesDontCross(Context context, Cycle cycleToCheck) {
        dbAdapter = new DatabaseAdapter(context);
    }
}
