package jarosyjarosy.menstrualcycleproject.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import jarosyjarosy.menstrualcycleproject.models.*;
import org.joda.time.DateTime;

import java.util.*;
import java.util.stream.Collectors;

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
    private static final String DAY_KEY_OVULATORY_PAIN = "ovulatory_pain";
    private static final String DAY_KEY_TENSION_IN_BREASTS = "tension_in_breasts";
    private static final String DAY_KEY_OTHER_SYMPTOMS = "other_symptoms";
    private static final String DAY_KEY_INTERCOURSE = "intercourse";

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
                            CYCLE_KEY_END_DATE + " text," +
                            CYCLE_KEY_PEAK_OF_MUCUS + " integer," +
                            CYCLE_KEY_PEAK_OF_CERVIX + " integer" +
                            ");"

            );
            sqLiteDatabase.execSQL(
                    "create table days(" +
                            DAY_KEY_ID + " integer primary key autoincrement," +
                            DAY_KEY_CREATE_DATE + " text," +
                            DAY_KEY_DAY_OF_CYCLE + " integer," +
                            DAY_KEY_TEMPERATURE + " real," +
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
            Log.d("SQLiteManager", "Database creating...");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.d("SQLiteManager", "Database updating...");

        }
    }

    public DatabaseAdapter(Context context) {
        this.context = context;
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
        if(newCycle.getEndDate() != null) {
            newCycleValues.put(CYCLE_KEY_END_DATE, newCycle.getEndDate().toString());
        }
        return db.insert("cycles", null, newCycleValues);
    }

    public long insertDay(Day newDay) {
        ContentValues newDayValues = new ContentValues();
        newDayValues.put(DAY_KEY_CREATE_DATE, newDay.getCreateDate().toString());
        newDayValues.put(DAY_KEY_DAY_OF_CYCLE, newDay.getDayOfCycle());
        newDayValues.put(DAY_KEY_TEMPERATURE, newDay.getTemperature());
        newDayValues.put(DAY_KEY_BLEEDING, (newDay.getBleeding() != null) ? newDay.getBleeding().name() : "");

        List<MucusType> typeList = newDay.getMucus();
        ArrayList<String> list = new ArrayList<String>();
        for (MucusType type : typeList) {
            if(type != null) {
                list.add(type.name());
            }
        }
        StringBuilder sb = new StringBuilder();
        for(String s : list) {
            sb.append(s);
            sb.append(",");
        }

        newDayValues.put(DAY_KEY_MUCUS, sb.toString());
        newDayValues.put(DAY_KEY_DILATION_OF_CERVIX, newDay.getDilationOfCervix());
        newDayValues.put(DAY_KEY_POSITION_OF_CERVIX, newDay.getPositionOfCervix());
        newDayValues.put(DAY_KEY_HARDNESS_OF_CERVIX, (newDay.getHardnessOfCervix() != null) ? newDay.getHardnessOfCervix().name() : "");
        newDayValues.put(DAY_KEY_OVULATORY_PAIN, newDay.getOvulatoryPain());
        newDayValues.put(DAY_KEY_TENSION_IN_BREASTS, newDay.getTensionInTheBreasts());
        newDayValues.put(DAY_KEY_OTHER_SYMPTOMS, newDay.getOtherSymptoms());
        newDayValues.put(DAY_KEY_INTERCOURSE, newDay.getIntercourse());
        newDayValues.put(CYCLE_KEY_ID, newDay.getCycleId());
        return db.insert("days", null, newDayValues);
    }

    public boolean updateCycle(Cycle cycle) {
        String where = CYCLE_KEY_ID + "=" + cycle.getCycleId();
        ContentValues updatedCycleValues = new ContentValues();
        updatedCycleValues.put(CYCLE_KEY_START_DATE, cycle.getStartDate().toString());
        updatedCycleValues.put(CYCLE_KEY_END_DATE, cycle.getEndDate().toString());
        updatedCycleValues.put(CYCLE_KEY_PEAK_OF_MUCUS, cycle.getPeakOfMucus());
        updatedCycleValues.put(CYCLE_KEY_PEAK_OF_CERVIX, cycle.getPeakOfCervix());
        return db.update("cycles", updatedCycleValues, where, null) > 0;
    }

    public boolean updateDay(Day day) {
        String where = DAY_KEY_ID + "=" + day.getDayId();
        ContentValues updatedDayValues = new ContentValues();
        updatedDayValues.put(DAY_KEY_DAY_OF_CYCLE, day.getDayOfCycle());
        updatedDayValues.put(DAY_KEY_TEMPERATURE, day.getTemperature());
        updatedDayValues.put(DAY_KEY_BLEEDING, day.getBleeding().name());

        List<MucusType> typeList = day.getMucus();
        ArrayList<String> list = new ArrayList<String>();
        for (MucusType type : typeList) {
            if(type != null) {
                list.add(type.name());
            }
        }
        StringBuilder sb = new StringBuilder();
        for(String s : list) {
            sb.append(s);
            sb.append(",");
        }

        updatedDayValues.put(DAY_KEY_MUCUS, sb.toString());
        updatedDayValues.put(DAY_KEY_DILATION_OF_CERVIX, day.getDilationOfCervix());
        updatedDayValues.put(DAY_KEY_POSITION_OF_CERVIX, day.getPositionOfCervix());
        updatedDayValues.put(DAY_KEY_HARDNESS_OF_CERVIX, day.getHardnessOfCervix().name());
        updatedDayValues.put(DAY_KEY_OVULATORY_PAIN, day.getOvulatoryPain());
        updatedDayValues.put(DAY_KEY_TENSION_IN_BREASTS, day.getTensionInTheBreasts());
        updatedDayValues.put(DAY_KEY_OTHER_SYMPTOMS, day.getOtherSymptoms());
        updatedDayValues.put(DAY_KEY_INTERCOURSE, day.getIntercourse());
        return db.update("days", updatedDayValues, where, null) > 0;
    }

    public boolean deleteDay(long id) {
        String where = DAY_KEY_ID + "=" + id;
        return db.delete("days", where, null) > 0;
    }

    public boolean deleteCycle(long id) {
        String where = CYCLE_KEY_ID + "=" + id;
        return db.delete("cycle", where, null) > 0;
    }

    public List<Cycle> getAllCycles() {
        List<Cycle> cycleList = new ArrayList<>();
        String[] columns = {CYCLE_KEY_ID, CYCLE_KEY_START_DATE, CYCLE_KEY_END_DATE, CYCLE_KEY_PEAK_OF_MUCUS, CYCLE_KEY_PEAK_OF_CERVIX};
        String orderBy = CYCLE_KEY_START_DATE + " desc";
        Cursor cycleCursor = db.query("cycles", columns, null, null, null, null, orderBy);
        cycleCursor.moveToFirst();
        if(cycleCursor.moveToFirst()) {
            do {
                Cycle cycle = getCycle(cycleCursor.getLong(0));
                cycleList.add(cycle);
            } while (cycleCursor.moveToNext());
        }
        return cycleList;
    }

    public  List<Day> getAllDaysFromCycle(long cycleId) {
        List<Day> dayList = new ArrayList<>();
        String[] columns = {DAY_KEY_ID, DAY_KEY_CREATE_DATE, DAY_KEY_DAY_OF_CYCLE, DAY_KEY_TEMPERATURE, DAY_KEY_BLEEDING, DAY_KEY_MUCUS,
                DAY_KEY_DILATION_OF_CERVIX, DAY_KEY_POSITION_OF_CERVIX, DAY_KEY_HARDNESS_OF_CERVIX, DAY_KEY_OVULATORY_PAIN, DAY_KEY_TENSION_IN_BREASTS,
                DAY_KEY_OTHER_SYMPTOMS, DAY_KEY_INTERCOURSE};
        String where = CYCLE_KEY_ID + "=" + cycleId;
        String orderBy = DAY_KEY_DAY_OF_CYCLE + " asc";
        Cursor dayCursor = db.query("days", columns, where, null, null, null, orderBy);
        if(dayCursor.getCount() == 0) {
            return Collections.emptyList();
        }
        dayCursor.moveToFirst();
        do {
            Day day = getDay(dayCursor.getLong(0));
            dayList.add(day);
        } while (dayCursor.moveToNext());
        return dayList;
    }

    public  List<Day> getAllDays() {
        List<Day> dayList = new ArrayList<>();
        String[] columns = {DAY_KEY_ID, DAY_KEY_CREATE_DATE, DAY_KEY_DAY_OF_CYCLE, DAY_KEY_TEMPERATURE, DAY_KEY_BLEEDING, DAY_KEY_MUCUS,
                DAY_KEY_DILATION_OF_CERVIX, DAY_KEY_POSITION_OF_CERVIX, DAY_KEY_HARDNESS_OF_CERVIX, DAY_KEY_OVULATORY_PAIN, DAY_KEY_TENSION_IN_BREASTS,
                DAY_KEY_OTHER_SYMPTOMS, DAY_KEY_INTERCOURSE};
        String orderBy = DAY_KEY_CREATE_DATE + " desc";
        Cursor dayCursor = db.query("days", columns, null, null, null, null, orderBy);
        dayCursor.moveToFirst();
        do {
            Day day = getDay(dayCursor.getLong(0));
            dayList.add(day);
        } while (dayCursor.moveToNext());
        return dayList;
    }

    public Cycle getCycle(long id) {
        String[] columns = {CYCLE_KEY_START_DATE, CYCLE_KEY_END_DATE, CYCLE_KEY_PEAK_OF_MUCUS, CYCLE_KEY_PEAK_OF_CERVIX};
        String where = CYCLE_KEY_ID + "=" + id;
        Cursor cursor = db.query("cycles", columns, where, null, null, null, null);
        Cycle cycle = new Cycle();
        if (cursor != null && cursor.moveToFirst()) {
            cycle.setCycleId(id);
            cycle.setStartDate(DateTime.parse(cursor.getString(0)));
            if(cursor.getString(1) != null) {
                cycle.setEndDate(DateTime.parse(cursor.getString(1)));
            }
            cycle.setPeakOfMucus(cursor.getInt(2));
            cycle.setPeakOfCervix(cursor.getInt(3));
        }
        return cycle;
    }

    public Cycle getLatestCycle() {
        String[] columns = {CYCLE_KEY_ID, CYCLE_KEY_START_DATE, CYCLE_KEY_END_DATE, CYCLE_KEY_PEAK_OF_MUCUS, CYCLE_KEY_PEAK_OF_CERVIX};
        String orderBy = CYCLE_KEY_START_DATE + " desc" ;
        Cursor cursor = db.query("cycles", columns, null, null, null, null, orderBy);
        Cycle cycle = new Cycle();
        if (cursor != null && cursor.moveToFirst()) {
            cycle.setCycleId(cursor.getLong(0));
            cycle.setStartDate(DateTime.parse(cursor.getString(1)));
            if(cursor.getString(2) != null) {
                cycle.setEndDate(DateTime.parse(cursor.getString(2)));
            }
            cycle.setPeakOfMucus(cursor.getInt(3));
            cycle.setPeakOfCervix(cursor.getInt(4));
        }
        return cycle;
    }

    public Day getDay(long id) {
        String[] columns = {DAY_KEY_CREATE_DATE, DAY_KEY_DAY_OF_CYCLE, DAY_KEY_TEMPERATURE, DAY_KEY_BLEEDING, DAY_KEY_MUCUS,
                DAY_KEY_DILATION_OF_CERVIX, DAY_KEY_POSITION_OF_CERVIX, DAY_KEY_HARDNESS_OF_CERVIX, DAY_KEY_OVULATORY_PAIN, DAY_KEY_TENSION_IN_BREASTS,
                DAY_KEY_OTHER_SYMPTOMS, DAY_KEY_INTERCOURSE, CYCLE_KEY_ID};
        String where = DAY_KEY_ID + "=" + id;
        Cursor cursor = db.query("days", columns, where, null, null, null, null);
        Day day = new Day();
        if (cursor != null && cursor.moveToFirst()) {
            day.setDayId(id);
            day.setCreateDate(DateTime.parse(cursor.getString(0)));
            day.setDayOfCycle(cursor.getInt(1));
            day.setTemperature(cursor.getFloat(2));
            day.setBleeding(BleedingType.valueOf(cursor.getString(3)));
            String[] mucusString = cursor.getString(4).split(",");
            List<MucusType> mucusList= new ArrayList<>();
            for (String type : mucusString) {
                if(!type.isEmpty()){
                    mucusList.add(MucusType.valueOf(type));
                }
            }
            day.setMucus(mucusList);
            day.setDilationOfCervix(cursor.getInt(5));
            day.setPositionOfCervix(cursor.getInt(6));
            if(!cursor.getString(7).isEmpty()) {
                day.setHardnessOfCervix(CervixHardnessType.valueOf(cursor.getString(7)));
            }
            day.setOvulatoryPain(cursor.getInt(8) > 0);
            day.setTensionInTheBreasts(cursor.getInt(9) > 0);
            day.setOtherSymptoms(cursor.getString(10));
            day.setIntercourse(cursor.getInt(11) > 0);
            day.setCycleId(cursor.getLong(12));
        }
        return day;
    }
}
