package jarosyjarosy.mentrualcycleproject.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MentrualCycleDbHelper extends SQLiteOpenHelper {

    public MentrualCycleDbHelper(Context context) {
        super(context, "mentrualcycle.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                        "create table cycles(" +
                        "cycle_id integer primary key autoincrement," +
                        "start_date text," +
                        "end_date text," +
                        "peak_of_mucus integer," +
                        "peak_of_cervix integer" +
                        ");"

        );
        sqLiteDatabase.execSQL(
                        "create table days(" +
                        "day_id integer primary key autoincrement," +
                        "create_date text," +
                        "day_of_cycle integer," +
                        "temperature real," +
                        "bleeding text," +
                        "mucus text," +
                        "dilation_of_cervix integer," +
                        "position_of_cervix integer," +
                        "hardness_of_cervix text," +
                        "ovulatory_pain integer," +
                        "tension_in_breasts integer," +
                        "other_symptoms text," +
                        "intercourse integer," +
                        "cycle_id integer," +
                        "foreign key (cycle_id) references cycles(cycle_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}