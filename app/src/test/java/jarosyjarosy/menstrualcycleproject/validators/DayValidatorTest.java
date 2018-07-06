package jarosyjarosy.menstrualcycleproject.validators;

import android.content.Context;
import android.widget.Toast;
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

/**
 * Based on: "https://github.com/lionel126/PowerMockDemo/blob/master/src/com/example/demo/powermocktest/TestTest.java"
 *
 * w/ PowerMock bytecode manipulation testing
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DayValidator.class, Context.class, DatabaseAdapter.class, Toast.class })
public class DayValidatorTest {
    private DayValidator dayValidator;
    private Context context;
    private DatabaseAdapter databaseAdapter;
    private Toast toast;

    @Before
    public void setUp() {
        dayValidator = PowerMockito.spy(new DayValidator());
        context = PowerMockito.mock(Context.class);
        databaseAdapter = PowerMockito.mock(DatabaseAdapter.class);
        PowerMockito.mockStatic(Toast.class);
        toast = PowerMockito.mock(Toast.class);
    }

    @Test
    public void validateDay_isCorrect() throws Exception {
        PowerMockito.whenNew(DatabaseAdapter.class)
			.withArguments(context).thenReturn(databaseAdapter);

        PowerMockito.when(
                Toast.makeText(Mockito.eq(context), Mockito.anyString(), Mockito.anyShort()))
                .thenReturn(toast);

        List<Day> mockedDays = new ArrayList<>();

        // @TODO way to shorten the mocking?
        Day day1 = new Day();
        day1.setCreateDate(DateTime.now().minusDays(1));

        Day day2 = new Day();
        day2.setCreateDate(DateTime.now());

        Day day3 = new Day();
        day3.setCreateDate(DateTime.now().plusDays(1));

        mockedDays.add(day1);
        mockedDays.add(day2);
        mockedDays.add(day3);


        PowerMockito.when(databaseAdapter.getAllDays()).thenReturn(mockedDays);

        Day day = new Day();
        day.setCreateDate(DateTime.now());

        Cycle cycle = new Cycle();
        cycle.setStartDate(DateTime.now());


        boolean resultFailure = dayValidator.validateDay(context, day2, cycle);
        assertFalse(resultFailure);

        boolean resultSuccess = dayValidator.validateDay(context, day, cycle);
        assertTrue(resultSuccess);
    }
}