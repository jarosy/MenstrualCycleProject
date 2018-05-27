package jarosyjarosy.menstrualcycleproject.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import jarosyjarosy.menstrualcycleproject.R;
import jarosyjarosy.menstrualcycleproject.config.VerticalTextView;
import jarosyjarosy.menstrualcycleproject.models.Cycle;
import jarosyjarosy.menstrualcycleproject.models.Day;
import jarosyjarosy.menstrualcycleproject.models.MucusType;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class TableActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Bundle bundle;

    private DateTimeFormatter monthAndYearFormat = DateTimeFormat.forPattern("MM.yyyy");
    private DateTimeFormatter dayOfMonthFormat = DateTimeFormat.forPattern("dd");

    private DatabaseAdapter dbAdapter;
    private Cycle cycle;
    private Cursor dayCursor;
    List<Day> dayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        setUpActionBar();
        getData();
        setUpTableLabels();
        setUpTableColumns();
    }

    public void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        if (menuItem.getTitle().toString().matches(getString(R.string.show_cycles))) {
                            openList(navigationView);
                        }

                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int dpToPx(int dp) {
        float density = this.getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public void setUpTableLabels() {
        GridLayout table = (GridLayout) findViewById(R.id.table);
        table.setColumnCount(dayList.size() + 4);
        table.setRowCount(47);
        table.setOrientation(GridLayout.VERTICAL);

        TextView dateLabel = new TextView(this);
        dateLabel.setWidth(dpToPx(220));
        addTextViewToTable(dateLabel, " DATA: " + monthAndYearFormat.print(cycle.getStartDate()), 0, 3, 0, 1);

        VerticalTextView temperatureLabel = new VerticalTextView(this);
        temperatureLabel.setWidth(dpToPx(27 * 20));
        addTextViewToTable(temperatureLabel, "                                                               TEMPERATURA "
                , 0, 1, 1, 27);
        temperatureLabel.setHeight(dpToPx(40));

        TextView dayLabel = new TextView(this);
        dayLabel.setWidth(dpToPx(220));
        addTextViewToTable(dayLabel, " DZIEŃ CYKLU ", 0, 3, 28, 1);

        TextView bleedingLabel = new TextView(this);
        bleedingLabel.setWidth(dpToPx(220));
        addTextViewToTable(bleedingLabel, " KRWAWIENIE/PLAMIENIE ", 0, 3, 29, 1);

        TextView peekOfMucusLabel = new TextView(this);
        peekOfMucusLabel.setWidth(dpToPx(220));
        addTextViewToTable(peekOfMucusLabel, " SZCZYT ŚLUZU ", 0, 3, 30, 1);

        VerticalTextView mucusLabel = new VerticalTextView(this);
        mucusLabel.setWidth(dpToPx(7 * 20));
        addTextViewToTable(mucusLabel, "               ŚLUZ ", 0, 1, 31, 7);
        mucusLabel.setHeight(dpToPx(40));

        TextView mucusAnomalousLabel = new TextView(this);
        mucusAnomalousLabel.setWidth(dpToPx(220));
        addTextViewToTable(mucusAnomalousLabel, " ŚLUZ/WYDZIELINA NIETYPOWA ", 0, 3, 38, 1);

        TextView peekOfCervixLabel = new TextView(this);
        peekOfCervixLabel.setWidth(dpToPx(220));
        addTextViewToTable(peekOfCervixLabel, " SZCZYT SZYJKI MACICY ", 0, 3, 39, 1);

        VerticalTextView cervixLabel = new VerticalTextView(this);
        cervixLabel.setWidth(dpToPx(60));
        addTextViewToTable(cervixLabel, " SZYJKA ", 0, 1, 40, 2);
        cervixLabel.setHeight(dpToPx(40));

        VerticalTextView otherSymptomsLabel = new VerticalTextView(this);
        otherSymptomsLabel.setWidth(dpToPx(140));
        addTextViewToTable(otherSymptomsLabel, "   OBJAWY DODATK. ", 0, 1, 42, 3);
        otherSymptomsLabel.setHeight(dpToPx(40));

        TextView prolificDaysLabel = new TextView(this);
        prolificDaysLabel.setWidth(dpToPx(220));
        addTextViewToTable(prolificDaysLabel, " DNI PŁODNE ", 0, 3, 45, 1);

        TextView intercourseLabel = new TextView(this);
        intercourseLabel.setWidth(dpToPx(220));
        addTextViewToTable(intercourseLabel, " WSPÓŁŻYCIE ", 0, 3, 46, 1);

        TextView temp01 = new TextView(this);
        temp01.setWidth(dpToPx(180));
        addTextViewToTable(temp01, " 37,30 ", 1, 2, 1, 1);
        TextView temp02 = new TextView(this);
        temp02.setWidth(dpToPx(180));
        addTextViewToTable(temp02, " 37,25 ", 1, 2, 2, 1);
        TextView temp03 = new TextView(this);
        temp03.setWidth(dpToPx(180));
        addTextViewToTable(temp03, " 37,20 ", 1, 2, 3, 1);
        TextView temp04 = new TextView(this);
        temp04.setWidth(dpToPx(180));
        addTextViewToTable(temp04, " 37,15 ", 1, 2, 4, 1);
        TextView temp05 = new TextView(this);
        temp05.setWidth(dpToPx(180));
        addTextViewToTable(temp05, " 37,10 ", 1, 2, 5, 1);
        TextView temp06 = new TextView(this);
        temp06.setWidth(dpToPx(180));
        addTextViewToTable(temp06, " 37,05 ", 1, 2, 6, 1);
        TextView temp07 = new TextView(this);
        temp07.setWidth(dpToPx(180));
        addTextViewToTable(temp07, " 37,00 ", 1, 2, 7, 1);
        TextView temp08 = new TextView(this);
        temp08.setWidth(dpToPx(180));
        addTextViewToTable(temp08, " 36,95 ", 1, 2, 8, 1);
        TextView temp09 = new TextView(this);
        temp09.setWidth(dpToPx(180));
        addTextViewToTable(temp09, " 36,90 ", 1, 2, 9, 1);
        TextView temp10 = new TextView(this);
        temp10.setWidth(dpToPx(180));
        addTextViewToTable(temp10, " 36,85 ", 1, 2, 10, 1);
        TextView temp11 = new TextView(this);
        temp11.setWidth(dpToPx(180));
        addTextViewToTable(temp11, " 36,80 ", 1, 2, 11, 1);
        TextView temp12 = new TextView(this);
        temp12.setWidth(dpToPx(180));
        addTextViewToTable(temp12, " 36,75 ", 1, 2, 12, 1);
        TextView temp13 = new TextView(this);
        temp13.setWidth(dpToPx(180));
        addTextViewToTable(temp13, " 36,70 ", 1, 2, 13, 1);
        TextView temp14 = new TextView(this);
        temp14.setWidth(dpToPx(180));
        addTextViewToTable(temp14, " 36,65 ", 1, 2, 14, 1);
        TextView temp15 = new TextView(this);
        temp15.setWidth(dpToPx(180));
        addTextViewToTable(temp15, " 36,60 ", 1, 2, 15, 1);
        TextView temp16 = new TextView(this);
        temp16.setWidth(dpToPx(180));
        addTextViewToTable(temp16, " 36,55 ", 1, 2, 16, 1);
        TextView temp17 = new TextView(this);
        temp17.setWidth(dpToPx(180));
        addTextViewToTable(temp17, " 36,50 ", 1, 2, 17, 1);
        TextView temp18 = new TextView(this);
        temp18.setWidth(dpToPx(180));
        addTextViewToTable(temp18, " 36,45 ", 1, 2, 18, 1);
        TextView temp19 = new TextView(this);
        temp19.setWidth(dpToPx(180));
        addTextViewToTable(temp19, " 36,40 ", 1, 2, 19, 1);
        TextView temp20 = new TextView(this);
        temp20.setWidth(dpToPx(180));
        addTextViewToTable(temp20, " 36,35 ", 1, 2, 20, 1);
        TextView temp21 = new TextView(this);
        temp21.setWidth(dpToPx(180));
        addTextViewToTable(temp21, " 36,30 ", 1, 2, 21, 1);
        TextView temp22 = new TextView(this);
        temp22.setWidth(dpToPx(180));
        addTextViewToTable(temp22, " 36,25 ", 1, 2, 22, 1);
        TextView temp23 = new TextView(this);
        temp23.setWidth(dpToPx(180));
        addTextViewToTable(temp23, " 36,20 ", 1, 2, 23, 1);
        TextView temp24 = new TextView(this);
        temp24.setWidth(dpToPx(180));
        addTextViewToTable(temp24, " 36,15 ", 1, 2, 24, 1);
        TextView temp25 = new TextView(this);
        temp25.setWidth(dpToPx(180));
        addTextViewToTable(temp25, " 36,10 ", 1, 2, 25, 1);
        TextView temp26 = new TextView(this);
        temp26.setWidth(dpToPx(180));
        addTextViewToTable(temp26, " 36,05 ", 1, 2, 26, 1);
        TextView temp27 = new TextView(this);
        temp27.setWidth(dpToPx(180));
        addTextViewToTable(temp27, " 36,00 ", 1, 2, 27, 1);

        TextView wetLabel = new TextView(this);
        wetLabel.setWidth(dpToPx(180));
        addTextViewToTable(wetLabel, " MOKRO/ŚLISKO ", 1, 2, 31, 1);
        TextView stretchyLabel = new TextView(this);
        stretchyLabel.setWidth(dpToPx(180));
        addTextViewToTable(stretchyLabel, " ROZCIĄGLIWY ", 1, 2, 32, 1);
        TextView transparentLabel = new TextView(this);
        transparentLabel.setWidth(dpToPx(180));
        addTextViewToTable(transparentLabel, " PRZEJRZYSTY ", 1, 2, 33, 1);
        TextView humidLabel = new TextView(this);
        humidLabel.setWidth(dpToPx(180));
        addTextViewToTable(humidLabel, " WILGOTNO ", 1, 2, 34, 1);
        TextView stickyLabel = new TextView(this);
        stickyLabel.setWidth(dpToPx(180));
        addTextViewToTable(stickyLabel, " LEPKI, GĘSTY ", 1, 2, 35, 1);
        TextView muzzyLabel = new TextView(this);
        muzzyLabel.setWidth(dpToPx(180));
        addTextViewToTable(muzzyLabel, " MĘTNY ", 1, 2, 36, 1);
        TextView dryLabel = new TextView(this);
        dryLabel.setWidth(dpToPx(180));
        addTextViewToTable(dryLabel, " SUCHO ", 1, 2, 37, 1);

        TextView posDilLabel = new TextView(this);
        posDilLabel.setWidth(dpToPx(180));
        addTextViewToTable(posDilLabel, " POZYCJA, ROZWARCIE ", 1, 2, 40, 1);
        posDilLabel.setHeight(dpToPx(40));
        TextView hardnessLabel = new TextView(this);
        hardnessLabel.setWidth(dpToPx(180));
        addTextViewToTable(hardnessLabel, " TWARDOŚĆ ", 1, 2, 41, 1);

        TextView painLabel = new TextView(this);
        painLabel.setWidth(dpToPx(180));
        addTextViewToTable(painLabel, " BÓL OWULACYJNY ", 1, 2, 42, 1);
        TextView tensionLabel = new TextView(this);
        tensionLabel.setWidth(dpToPx(180));
        addTextViewToTable(tensionLabel, " NAPIĘCIE W PIERSIACH ", 1, 2, 43, 1);
        TextView othersLabel = new TextView(this);
        othersLabel.setWidth(dpToPx(180));
        addTextViewToTable(othersLabel, " INNE ", 1, 2, 44, 1);
        othersLabel.setHeight(dpToPx(100));
    }

    public void addTextViewToTable(TextView tv, String text, int colStart, int colSize, int rowStart, int rowSize) {
        GridLayout table = (GridLayout) findViewById(R.id.table);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setHeight(dpToPx(20));
        tv.setBackgroundResource(R.drawable.cell_shape);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(colStart, colSize);
        params.rowSpec = GridLayout.spec(rowStart, rowSize);
        tv.setLayoutParams(params);
        table.addView(tv);
    }

    public void getData() {
        bundle = getIntent().getExtras();

        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();

        cycle = dbAdapter.getCycle(bundle.getLong("cycleId"));
        dayList = dbAdapter.getAllDaysFromCycle(cycle.getCycleId());
        dbAdapter.close();
    }

    public void setUpTableColumns() {
        ListIterator iterator = dayList.listIterator();
        for (Day day : dayList) {

            GridLayout table = (GridLayout) findViewById(R.id.table);
            table.setColumnCount(day.getDayOfCycle() + 4);

            if(iterator.hasPrevious()) {
                for (int col = dayList.get(iterator.previousIndex()).getDayOfCycle() + 4; col <= day.getDayOfCycle() + 3; col++) {
                    for (int row = 0; row < table.getRowCount(); row++) {
                        TextView blankView = new TextView(this);
                        blankView.setWidth(dpToPx(20));
                        addTextViewToTable(blankView, " ", col, 1, row, 1);
                        if(row == 40) {
                            blankView.setHeight(dpToPx(40));
                        }
                        if(row == 44) {
                            blankView.setHeight(dpToPx(100));
                        }
                    }
                }
            }

            TextView dayOfMonth = new TextView(this);
            dayOfMonth.setWidth(dpToPx(20));
            addTextViewToTable(dayOfMonth, dayOfMonthFormat.print(day.getCreateDate()), day.getDayOfCycle() + 3, 1, 0, 1);

            setTemperature(day);

            TextView dayOfCycle = new TextView(this);
            dayOfCycle.setWidth(dpToPx(20));
            addTextViewToTable(dayOfCycle, day.getDayOfCycle().toString(), day.getDayOfCycle() + 3, 1, 28, 1);

            TextView bleeding = new TextView(this);
            bleeding.setWidth(dpToPx(20));
            addTextViewToTable(bleeding, (day.getBleeding() != null) ? day.getBleeding().toString() : "", day.getDayOfCycle() + 3, 1, 29, 1);

            //tymczasowe
            TextView peekOfMucus = new TextView(this);
            peekOfMucus.setWidth(dpToPx(20));
            addTextViewToTable(peekOfMucus, " ", day.getDayOfCycle() + 3, 1, 30, 1);

            setMucus(day);

            //tymczasowe
            TextView peekOfCervix = new TextView(this);
            peekOfCervix.setWidth(dpToPx(20));
            addTextViewToTable(peekOfCervix, " ", day.getDayOfCycle() + 3, 1, 39, 1);

            setCervix(day);

            TextView cervixHardness = new TextView(this);
            cervixHardness.setWidth(dpToPx(20));
            addTextViewToTable(cervixHardness, (day.getHardnessOfCervix() != null) ? day.getHardnessOfCervix().toString() : "", day.getDayOfCycle() + 3, 1, 41, 1);

            TextView ovulatoryPain = new TextView(this);
            ovulatoryPain.setWidth(dpToPx(20));
            addTextViewToTable(ovulatoryPain, " ", day.getDayOfCycle() + 3, 1, 42, 1);
            if (day.getOvulatoryPain()) {
                ovulatoryPain.setBackgroundColor(Color.BLACK);
            }

            TextView breastTension = new TextView(this);
            breastTension.setWidth(dpToPx(20));
            addTextViewToTable(breastTension, " ", day.getDayOfCycle() + 3, 1, 43, 1);
            if (day.getTensionInTheBreasts()) {
                breastTension.setBackgroundColor(Color.BLACK);
            }
            //tymczasowe
            TextView otherSymptoms = new TextView(this);
            otherSymptoms.setWidth(dpToPx(20));
            addTextViewToTable(otherSymptoms, " ", day.getDayOfCycle() + 3, 1, 44, 1);
            otherSymptoms.setHeight(dpToPx(100));

            //tymczasowe
            TextView prolificDays = new TextView(this);
            prolificDays.setWidth(dpToPx(20));
            addTextViewToTable(prolificDays, " ", day.getDayOfCycle() + 3, 1, 45, 1);

            TextView intercourse = new TextView(this);
            intercourse.setWidth(dpToPx(20));
            addTextViewToTable(intercourse, " ", day.getDayOfCycle() + 3, 1, 46, 1);
            if (day.getIntercourse()) {
                intercourse.setBackgroundColor(Color.BLACK);
            }
            if(iterator.hasNext()) {
                iterator.next();
            }
        }
    }

    public void setTemperature(Day day) {
        Float temps[] = {37.30F, 37.25F, 37.20F, 37.15F, 37.10F, 37.05F, 37.00F, 36.95F, 36.90F, 36.85F, 36.80F, 36.75F, 36.70F,
                36.65F, 36.60F, 36.55F, 36.50F, 36.45F, 36.40F, 36.35F, 36.30F, 36.25F, 36.20F, 36.15F, 35.10F, 36.05F, 36.00F};
        int setRow = Arrays.asList(temps).indexOf(day.getTemperature());

        for (int i = 1; i < temps.length + 1; i++) {
            TextView temp = new TextView(this);
            temp.setWidth(dpToPx(20));
            addTextViewToTable(temp, " ", day.getDayOfCycle() + 3, 1, i, 1);
            if (i == setRow + 1) {
                temp.setBackgroundColor(Color.BLACK);
            }
        }
    }

    public void setMucus(Day day) {
        List<MucusType> allMucusList = Arrays.asList(MucusType.WET, MucusType.STRETCHY, MucusType.TRANSPARENT, MucusType.HUMID,
                MucusType.STICKY, MucusType.MUZZY, MucusType.DRY, MucusType.ANOMALOUS);
        List<MucusType> mucusList = day.getMucus();

        int i = 31;
        for (MucusType type : allMucusList) {
            TextView mucus = new TextView(this);
            mucus.setWidth(dpToPx(20));
            addTextViewToTable(mucus, " ", day.getDayOfCycle() + 3, 1, i, 1);
            if (mucusList.contains(type)) {
                mucus.setBackgroundColor(Color.BLACK);
            }
            i++;
        }
    }

    public void setCervix(Day day) {
        ImageView cervixView = new ImageView(this);
        Paint cervixPaint = new Paint();

        cervixPaint.setColor(Color.BLACK);
        cervixPaint.setStyle(Paint.Style.STROKE);
        cervixPaint.setStrokeWidth(12);
        Bitmap cervixBitmap = Bitmap.createBitmap(80, 160, Bitmap.Config.ARGB_8888);
        cervixView.setImageBitmap(cervixBitmap);
        Canvas cervixCanvas = new Canvas(cervixBitmap);
        cervixCanvas.drawCircle(40, 120 - 8 * day.getPositionOfCervix(), 6 + 2 * day.getDilationOfCervix(), cervixPaint);

        GridLayout table = (GridLayout) findViewById(R.id.table);
        cervixView.setBackgroundResource(R.drawable.cell_shape);
        cervixView.setMinimumHeight(dpToPx(40));
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(day.getDayOfCycle() + 3, 1);
        params.rowSpec = GridLayout.spec(40, 2);
        cervixView.setLayoutParams(params);
        table.addView(cervixView);
    }

    public void openDayForm(View view) {
        Boolean setDate = false;
        if (view.getTag() != null) {
            setDate = Boolean.valueOf(view.getTag().toString());
        }
        Intent intent = new Intent(TableActivity.this, DayFormActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("setDate", setDate);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void openTable(View view) {
        Intent intent = new Intent(TableActivity.this, TableActivity.class);
        startActivity(intent);
    }

    private void openList(View view) {
        Intent intent = new Intent(TableActivity.this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
       openList(new View(this));
    }


}
