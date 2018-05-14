package jarosyjarosy.menstrualcycleproject.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import jarosyjarosy.menstrualcycleproject.models.Cycle;
import jarosyjarosy.menstrualcycleproject.models.Day;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DatabaseAdapter {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "menstrualcycle.db";

    private static final String CYCLE_KEY_ID = "cycle_id";
    private static final String CYCLE_KEY_START_DATE = "start_date";
    private static final String CYCLE_KEY_END_DATE = "end_date";
    private static final String CYCLE_KEY_PEAK_OF_MUCUS = "peak_of_mucus";
    private static final String CYCLE_KEY_PEAK_OF_CERVIX = "peak_of_cervix";
    private static final String DAY_KEY_ID = "day_id";
    private static final String DAY_KEY_CREATE_DATE = "create_day";
    private static final String DAY_KEY_DAY_OF_CYCLE = "day_of_cycle";
    private static final String DAY_KEY_TEMPERATURE = "temperature";
    private static final String DAY_KEY_BLEEDING = "bleeding";
    private static final String DAY_KEY_MUCUS = "mucus";
    private static final String DAY_KEY_DILATION_OF_CERVIX = "dilation_of_cervix";
    private static final String DAY_KEY_POSITION_OF_CERVIX = "position_of_cervix";
    private static final String DAY_KEY_HARDNESS_OF_CERVIX = "hardness_of_cervix";
    private static final String DAY_KEY_OVULATORY_PAIN = "ovulatory pain";
    private static final String DAY_KEY_TENSION_IN_BREASTS = "tension_in_breasts";
    private static final String DAY_KEY_OTHER_SYMPTOMS = "other_symptoms";
    private static final String DAY_KEY_INTERCOURSE = "intercourse";

    private SimpleDateFormat appDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
            super(context, name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(
                    "create table cycles(" +
                            CYCLE_KEY_ID + " integer primary key autoincrement," +
                            CYCLE_KEY_START_DATE + " text," +
                            CYCLE_KEY_END_DATE+ " text," +
                            CYCLE_KEY_PEAK_OF_MUCUS + " integer," +
                            CYCLE_KEY_PEAK_OF_CERVIX + " integer" +
                            ");"

            );
            sqLiteDatabase.execSQL(
                    "create table days(" +
                            DAY_KEY_ID + " integer primary key autoincrement," +
                            DAY_KEY_CREATE_DATE + " text," +
                            DAY_KEY_DAY_OF_CYCLE + " integer," +
                            DAY_KEY_TEMPERATURE+ " real," +
                            DAY_KEY_BLEEDING + " text," +
                            DAY_KEY_MUCUS + " mucus text," +
                            DAY_KEY_DILATION_OF_CERVIX + " integer," +
                            DAY_KEY_POSITION_OF_CERVIX + " integer," +
                            DAY_KEY_HARDNESS_OF_CERVIX + " text," +
                            DAY_KEY_OVULATORY_PAIN + " integer," +
                            DAY_KEY_TENSION_IN_BREASTS + " integer," +
                            DAY_KEY_OTHER_SYMPTOMS + " text," +
                            DAY_KEY_INTERCOURSE + " integer," +
                            CYCLE_KEY_ID + " integer," +
                            "foreign key (" + CYCLE_KEY_ID + ") references cycles(" + CYCLE_KEY_ID + "));");
            Log.d("SQLiteManager","Database creating...");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.d("SQLiteManager", "Database updating...");

        }
    }

    public DatabaseAdapter open() {
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertCycle(Cycle newCycle) {
        ContentValues newCycleValues = new ContentValues();
        newCycleValues.put(CYCLE_KEY_START_DATE, newCycle.getStartDate().toString());
        return  db.insert("cycles",null, newCycleValues);
    }

    public long insertDay(Day newDay) {
        ContentValues newDayValues = new ContentValues();
        newDayValues.put(DAY_KEY_DAY_OF_CYCLE, newDay.getDayOfCycle());
        newDayValues.put(DAY_KEY_TEMPERATURE, newDay.getTemperature());
        newDayValues.put(DAY_KEY_BLEEDING, newDay.getBleeding());
        newDayValues.put(DAY_KEY_MUCUS, newDay.getMucus());
        newDayValues.put(DAY_KEY_DILATION_OF_CERVIX, newDay.getDilationOfCervix());
        newDayValues.put(DAY_KEY_POSITION_OF_CERVIX, newDay.getPositionOfCervix());
        newDayValues.put(DAY_KEY_HARDNESS_OF_CERVIX, newDay.getHardnessOfCervix());
        newDayValues.put(DAY_KEY_OVULATORY_PAIN, newDay.getOvulatoryPain());
        newDayValues.put(DAY_KEY_TENSION_IN_BREASTS, newDay.getTensionInTheBreasts());
        newDayValues.put(DAY_KEY_OTHER_SYMPTOMS, newDay.getOtherSymptoms());
        newDayValues.put(DAY_KEY_INTERCOURSE, newDay.getIntercourse());
        newDayValues.put(CYCLE_KEY_ID, newDay.getCycleId());
                return  db.insert("days",null, newDayValues);
    }

    public boolean updateCycle(Cycle cycle) {
        String where = CYCLE_KEY_ID + "=" + cycle.getCycleId();
        ContentValues updatedCycleValues = new ContentValues();
        updatedCycleValues.put(CYCLE_KEY_START_DATE, cycle.getStartDate().toString());
        updatedCycleValues.put(CYCLE_KEY_END_DATE, cycle.getEndDate().toString());
        updatedCycleValues.put(CYCLE_KEY_PEAK_OF_MUCUS, cycle.getPeakOfMucus());
        updatedCycleValues.put(CYCLE_KEY_PEAK_OF_CERVIX, cycle.getPeakOfCervix());
        return db.update("cycles", updatedCycleValues, where,null) > 0;
    }

    public boolean updateDay(Day day) {
        String where = DAY_KEY_ID + "=" + day.getDayId();
        ContentValues updatedDayValues = new ContentValues();
        updatedDayValues.put(DAY_KEY_DAY_OF_CYCLE, day.getDayOfCycle());
        updatedDayValues.put(DAY_KEY_TEMPERATURE, day.getTemperature());
        updatedDayValues.put(DAY_KEY_BLEEDING, day.getBleeding());
        updatedDayValues.put(DAY_KEY_MUCUS, day.getMucus());
        updatedDayValues.put(DAY_KEY_DILATION_OF_CERVIX, day.getDilationOfCervix());
        updatedDayValues.put(DAY_KEY_POSITION_OF_CERVIX, day.getPositionOfCervix());
        updatedDayValues.put(DAY_KEY_HARDNESS_OF_CERVIX, day.getHardnessOfCervix());
        updatedDayValues.put(DAY_KEY_OVULATORY_PAIN, day.getOvulatoryPain());
        updatedDayValues.put(DAY_KEY_TENSION_IN_BREASTS, day.getTensionInTheBreasts());
        updatedDayValues.put(DAY_KEY_OTHER_SYMPTOMS, day.getOtherSymptoms());
        updatedDayValues.put(DAY_KEY_INTERCOURSE, day.getIntercourse());
        return db.update("days", updatedDayValues, where, null) > 0;
    }

    public boolean deleteDay(long id){
        String where = DAY_KEY_ID + "=" + id;
        return db.delete("days", where, null) > 0;
    }

    public boolean deleteCycle(long id){
        String where = CYCLE_KEY_ID + "=" + id;
        return db.delete("cycle", where, null) > 0;
    }

    public Cursor getAllCycles() {
        String[] columns = {CYCLE_KEY_START_DATE, CYCLE_KEY_END_DATE, CYCLE_KEY_PEAK_OF_MUCUS, CYCLE_KEY_PEAK_OF_CERVIX};
        String orderBy = CYCLE_KEY_START_DATE + " desc";
        return db.query("cycles", columns, null, null, null, null, orderBy);
    }

    public Cursor getAllDaysFromCycle(long cycleId) {
        String[] columns = {DAY_KEY_CREATE_DATE, DAY_KEY_DAY_OF_CYCLE, DAY_KEY_TEMPERATURE, DAY_KEY_BLEEDING, DAY_KEY_MUCUS,
        DAY_KEY_DILATION_OF_CERVIX, DAY_KEY_POSITION_OF_CERVIX, DAY_KEY_HARDNESS_OF_CERVIX,DAY_KEY_OVULATORY_PAIN, DAY_KEY_TENSION_IN_BREASTS,
        DAY_KEY_OTHER_SYMPTOMS, DAY_KEY_INTERCOURSE};
        String where = CYCLE_KEY_ID + "=" + cycleId;
        String orderBy = DAY_KEY_DAY_OF_CYCLE + "asc";
        return  db.query("days", columns, where, null, null, null, orderBy);
    }

    public Cycle getCycle(long id) {
        String[] columns = {CYCLE_KEY_START_DATE, CYCLE_KEY_END_DATE, CYCLE_KEY_PEAK_OF_MUCUS, CYCLE_KEY_PEAK_OF_CERVIX};
        String where = CYCLE_KEY_ID + "=" + id;
        Cursor cursor = db.query("cycles", columns, where, null, null, null, null);
        Cycle cycle = new Cycle();
        if(cursor != null && cursor.moveToFirst()) {
            cycle.setCycleId(id);
            try {
                cycle.setStartDate(appDateFormat.parse(cursor.getString(1)));
                cycle.setEndDate(appDateFormat.parse(cursor.getString(2)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cycle.setPeakOfMucus(cursor.getInt(3));
            cycle.setPeakOfCervix(cursor.getInt(4));
        }
        return cycle;
    }

    public Day getDay(long id) {
        String[] columns = {DAY_KEY_CREATE_DATE, DAY_KEY_DAY_OF_CYCLE, DAY_KEY_TEMPERATURE, DAY_KEY_BLEEDING, DAY_KEY_MUCUS,
                DAY_KEY_DILATION_OF_CERVIX, DAY_KEY_POSITION_OF_CERVIX, DAY_KEY_HARDNESS_OF_CERVIX,DAY_KEY_OVULATORY_PAIN, DAY_KEY_TENSION_IN_BREASTS,
                DAY_KEY_OTHER_SYMPTOMS, DAY_KEY_INTERCOURSE, CYCLE_KEY_ID};
        String where = DAY_KEY_ID + "=" + id;
        Cursor cursor = db.query("days", columns, where, null, null, null, null);
        Day day = new Day();
        if(cursor != null && cursor.moveToFirst()) {
            day.setDayId(id);
            try {
                day.setCreateDate(appDateFormat.parse(cursor.getString(1)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            day.setDayOfCycle(cursor.getInt(2));
            day.setTemperature(cursor.getFloat(3));
            day.setBleeding(cursor.getString(4));
            day.setMucus(cursor.getString(5));
            day.setDilationOfCervix(cursor.getInt(6));
            day.setPositionOfCervix(cursor.getInt(7));
            day.setHardnessOfCervix(cursor.getString(8));
            day.setOvulatoryPain(cursor.getInt(9) > 0);
            day.setTensionInTheBreasts(cursor.getInt(10) > 0);
            day.setOtherSymptoms(cursor.getString(11));
            day.setIntercourse(cursor.getInt(12) > 0);
            day.setCycleId(cursor.getLong(13));
        }
        return day;
    }
}
