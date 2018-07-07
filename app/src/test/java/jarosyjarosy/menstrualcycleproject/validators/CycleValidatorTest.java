package jarosyjarosy.menstrualcycleproject.validators;

import android.content.Context;
import jarosyjarosy.menstrualcycleproject.models.Cycle;
import jarosyjarosy.menstrualcycleproject.models.Day;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CycleValidator.class, Context.class, DatabaseAdapter.class })
public class CycleValidatorTest {
    private CycleValidator cycleValidator;
    private Context context;
    private DatabaseAdapter databaseAdapter;

    @Before
    public void setUp() {
        cycleValidator = PowerMockito.spy(new CycleValidator());
        context = PowerMockito.mock(Context.class);
        databaseAdapter = PowerMockito.mock(DatabaseAdapter.class);
    }

    @Test
    public void validateCycle() throws Exception {
        PowerMockito.whenNew(DatabaseAdapter.class)
                .withArguments(context).thenReturn(databaseAdapter);

        List<Cycle> mockedCycles = new ArrayList<>();

        Cycle cycle1 = new Cycle();
        cycle1.setCycleId(new Long(1));
        cycle1.setStartDate(DateTime.now().minusDays(1));

        Cycle cycle2 = new Cycle();
        cycle2.setCycleId(new Long(2));
        cycle2.setStartDate(DateTime.now());

        Cycle cycle3 = new Cycle();
        cycle3.setCycleId(new Long(3));
        cycle3.setStartDate(DateTime.now().plusDays(1));

        mockedCycles.add(cycle1);
        mockedCycles.add(cycle2);
        mockedCycles.add(cycle3);


        PowerMockito.when(databaseAdapter.getAllCycles()).thenReturn(mockedCycles);

        Day day = new Day();
        day.setCreateDate(DateTime.now().plusDays(2));

        PowerMockito.when(databaseAdapter.getLastDayfromCycle(Mockito.anyLong())).thenReturn(day);

        Cycle cycle = new Cycle();
        cycle.setStartDate(DateTime.now().plusDays(3));

        boolean resultFailure = cycleValidator.validateCycle(context, cycle2);
        assertFalse(resultFailure);

        boolean resultSuccess = cycleValidator.validateCycle(context, cycle);
        assertTrue(resultSuccess);
    }
}