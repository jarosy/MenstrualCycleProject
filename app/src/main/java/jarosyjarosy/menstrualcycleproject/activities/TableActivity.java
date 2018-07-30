package jarosyjarosy.menstrualcycleproject.activities;

import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.alexvasilkov.gestures.views.GestureFrameLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import jarosyjarosy.menstrualcycleproject.R;
import jarosyjarosy.menstrualcycleproject.config.TemperatureValueFormatter;
import jarosyjarosy.menstrualcycleproject.config.VerticalTextView;
import jarosyjarosy.menstrualcycleproject.models.Cycle;
import jarosyjarosy.menstrualcycleproject.models.Day;
import jarosyjarosy.menstrualcycleproject.models.MucusType;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;

public class TableActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Bundle bundle;

    private DateTimeFormatter monthAndYearFormat = DateTimeFormat.forPattern("MM.yyyy");
    private DateTimeFormatter dayOfMonthFormat = DateTimeFormat.forPattern("dd");

    private DatabaseAdapter dbAdapter;
    private Cycle cycle;
    List<Day> dayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        setUpActionBar();
        getData();
        setUpChart();
        setUpTableLabels();
        setUpTableColumns();
    }

    private void setUpActionBar() {
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

    private int dpToPx(int dp) {
        float density = this.getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void setUpTableLabels() {
        GridLayout table = (GridLayout) findViewById(R.id.table);
        table.setColumnCount(dayList.size() + 4);
        table.setRowCount(21);
        table.setOrientation(GridLayout.VERTICAL);

        TextView dateLabel = new TextView(this);
        dateLabel.setWidth(dpToPx(220));
        addTextViewToTable(dateLabel, " DATA: " + monthAndYearFormat.print(cycle.getStartDate()), 0, 3, 0, 1);

        VerticalTextView temperatureLabel = new VerticalTextView(this);
        temperatureLabel.setWidth(dpToPx(400));
        addTextViewToTable(temperatureLabel, "                                       TEMPERATURA "
                , 0, 3, 1, 1);
        temperatureLabel.setHeight(dpToPx(220));

        TextView dayLabel = new TextView(this);
        dayLabel.setWidth(dpToPx(220));
        addTextViewToTable(dayLabel, " DZIEŃ CYKLU ", 0, 3, 2, 1);

        TextView bleedingLabel = new TextView(this);
        bleedingLabel.setWidth(dpToPx(220));
        addTextViewToTable(bleedingLabel, " KRWAWIENIE/PLAMIENIE ", 0, 3, 3, 1);

        TextView peekOfMucusLabel = new TextView(this);
        peekOfMucusLabel.setWidth(dpToPx(220));
        addTextViewToTable(peekOfMucusLabel, " SZCZYT ŚLUZU ", 0, 3, 4, 1);

        VerticalTextView mucusLabel = new VerticalTextView(this);
        mucusLabel.setWidth(dpToPx(7 * 20));
        addTextViewToTable(mucusLabel, "               ŚLUZ ", 0, 1, 5, 7);
        mucusLabel.setHeight(dpToPx(40));

        TextView mucusAnomalousLabel = new TextView(this);
        mucusAnomalousLabel.setWidth(dpToPx(220));
        addTextViewToTable(mucusAnomalousLabel, " ŚLUZ/WYDZIELINA NIETYPOWA ", 0, 3, 12, 1);

        TextView peekOfCervixLabel = new TextView(this);
        peekOfCervixLabel.setWidth(dpToPx(220));
        addTextViewToTable(peekOfCervixLabel, " SZCZYT SZYJKI MACICY ", 0, 3, 13, 1);

        VerticalTextView cervixLabel = new VerticalTextView(this);
        cervixLabel.setWidth(dpToPx(60));
        addTextViewToTable(cervixLabel, " SZYJKA ", 0, 1, 14, 2);
        cervixLabel.setHeight(dpToPx(40));

        VerticalTextView otherSymptomsLabel = new VerticalTextView(this);
        otherSymptomsLabel.setWidth(dpToPx(140));
        addTextViewToTable(otherSymptomsLabel, "   OBJAWY DODATK. ", 0, 1, 16, 3);
        otherSymptomsLabel.setHeight(dpToPx(40));

        TextView prolificDaysLabel = new TextView(this);
        prolificDaysLabel.setWidth(dpToPx(220));
        addTextViewToTable(prolificDaysLabel, " DNI PŁODNE ", 0, 3, 19, 1);

        TextView intercourseLabel = new TextView(this);
        intercourseLabel.setWidth(dpToPx(220));
        addTextViewToTable(intercourseLabel, " WSPÓŁŻYCIE ", 0, 3, 20, 1);

//        double startTemp = 37.3d;
//        for (int i = 1; i < 38; i++) {
//            String tempText = String.format(Locale.ENGLISH, "%,.2f", (double) Math.round((startTemp - ((i - 1) * 0.05d)) * 100000d) / 100000d);
//            TextView temp = new TextView(this);
//            temp.setWidth(dpToPx(180));
//            addTextViewToTable(temp, tempText, 1, 2, i, 1);
//        }


        TextView wetLabel = new TextView(this);
        wetLabel.setWidth(dpToPx(180));
        addTextViewToTable(wetLabel, " MOKRO/ŚLISKO ", 1, 2, 5, 1);
        TextView stretchyLabel = new TextView(this);
        stretchyLabel.setWidth(dpToPx(180));
        addTextViewToTable(stretchyLabel, " ROZCIĄGLIWY ", 1, 2, 6, 1);
        TextView transparentLabel = new TextView(this);
        transparentLabel.setWidth(dpToPx(180));
        addTextViewToTable(transparentLabel, " PRZEJRZYSTY ", 1, 2, 7, 1);
        TextView humidLabel = new TextView(this);
        humidLabel.setWidth(dpToPx(180));
        addTextViewToTable(humidLabel, " WILGOTNO ", 1, 2, 8, 1);
        TextView stickyLabel = new TextView(this);
        stickyLabel.setWidth(dpToPx(180));
        addTextViewToTable(stickyLabel, " LEPKI, GĘSTY ", 1, 2, 9, 1);
        TextView muzzyLabel = new TextView(this);
        muzzyLabel.setWidth(dpToPx(180));
        addTextViewToTable(muzzyLabel, " MĘTNY ", 1, 2, 10, 1);
        TextView dryLabel = new TextView(this);
        dryLabel.setWidth(dpToPx(180));
        addTextViewToTable(dryLabel, " SUCHO ", 1, 2, 11, 1);

        TextView posDilLabel = new TextView(this);
        posDilLabel.setWidth(dpToPx(180));
        addTextViewToTable(posDilLabel, " POZYCJA, ROZWARCIE ", 1, 2, 14, 1);
        posDilLabel.setHeight(dpToPx(40));
        TextView hardnessLabel = new TextView(this);
        hardnessLabel.setWidth(dpToPx(180));
        addTextViewToTable(hardnessLabel, " TWARDOŚĆ ", 1, 2, 15, 1);

        TextView painLabel = new TextView(this);
        painLabel.setWidth(dpToPx(180));
        addTextViewToTable(painLabel, " BÓL OWULACYJNY ", 1, 2, 16, 1);
        TextView tensionLabel = new TextView(this);
        tensionLabel.setWidth(dpToPx(180));
        addTextViewToTable(tensionLabel, " NAPIĘCIE W PIERSIACH ", 1, 2, 17, 1);
        TextView othersLabel = new TextView(this);
        othersLabel.setWidth(dpToPx(180));
        addTextViewToTable(othersLabel, " INNE ", 1, 2, 18, 1);
        othersLabel.setHeight(dpToPx(100));
    }

    private void addTextViewToTable(TextView tv, String text, int colStart, int colSize, int rowStart, int rowSize) {
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

    private void getData() {
        bundle = getIntent().getExtras();

        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();

        cycle = dbAdapter.getCycle(bundle.getLong("cycleId"));
        dayList = dbAdapter.getAllDaysFromCycle(cycle.getCycleId());
        dbAdapter.close();
    }

    private void setUpChart() {
        LineChart chart = new LineChart(this);

        GridLayout table = (GridLayout) findViewById(R.id.table);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(3, table.getColumnCount() - 3);
        params.rowSpec = GridLayout.spec(1, 1);
        params.setMarginStart(dpToPx(-35));
        params.height = dpToPx(400);
        params.width = dpToPx(20 * (dayList.get(dayList.size() - 1).getDayOfCycle() + 2));
        chart.setLayoutParams(params);
        table.addView(chart);


        Description desc = new Description();
        desc.setText("Temperatura / Dzień");
        desc.setEnabled(false);
        chart.setDescription(desc);

        chart.getAxisLeft().setEnabled(false);
        chart.setNoDataText("Brak pomiarów temperatury");

        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setAxisMinimum(-0.5F);
        chart.getXAxis().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setGranularity(1f);
        chart.setTouchEnabled(false);
        chart.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_shape_chart));

        List<ILineDataSet> lines = new ArrayList<>();
        List<List<Entry>> entries = new ArrayList<>();

        boolean isAnyTemp = false;

        int counter = 0;
        entries.add(new ArrayList<Entry>());
        for (int d = 0; d < dayList.size(); d++) {
            if (dayList.get(d).getTemperature() > 0) {
                isAnyTemp = true;
                entries.get(counter).add(new Entry(dayList.get(d).getDayOfCycle(), dayList.get(d).getTemperature()));
                if (dayList.get(d) == dayList.get(dayList.size()-1) ||
                        dayList.get(d).getDayOfCycle() +1 != dayList.get(d+1).getDayOfCycle() ||
                        dayList.get(d+1).getTemperature() <= 0) {
                    LineDataSet newLine = new LineDataSet(entries.get(counter), "Temperatura");
                    // @TODO does not work?
                    newLine.setColor(R.color.colorPrimary);
                    newLine.setValueTextColor(R.color.colorPrimaryDark);
                    newLine.setCircleColor(R.color.colorAccent);

                    newLine.setHighlightEnabled(false);
                    newLine.setLineWidth(2);
                    lines.add(newLine);
                    counter += 1;
                    entries.add(new ArrayList<Entry>());
                }
            }
        }

        chart.setData(new LineData(lines));
        chart.getXAxis().setAxisMaximum(dayList.get(dayList.size()-1).getDayOfCycle());
        chart.getLineData().setValueTextSize(7);
        chart.getLineData().setValueFormatter(new TemperatureValueFormatter());
        if (!isAnyTemp) {
            chart.clear();
        }

        chart.invalidate(); // refresh
    }

    private void setUpTableColumns() {
        ListIterator iterator = dayList.listIterator();
        for (Day day : dayList) {

            GridLayout table = (GridLayout) findViewById(R.id.table);
            table.setColumnCount(day.getDayOfCycle() + 5);

            if (iterator.hasPrevious()) {
                for (int col = dayList.get(iterator.previousIndex()).getDayOfCycle() + 4; col <= day.getDayOfCycle() + 3; col++) {
                    for (int row = 0; row < table.getRowCount(); row++) {
                        if (row != 1) {
                            TextView blankView = new TextView(this);
                            blankView.setWidth(dpToPx(20));
                            addTextViewToTable(blankView, " ", col, 1, row, 1);
                            if (row == 14) {
                                blankView.setHeight(dpToPx(40));
                            }
                            if (row == 18) {
                                blankView.setHeight(dpToPx(100));
                            }
                        }
                    }
                }
            }

            TextView dayOfMonth = new TextView(this);
            dayOfMonth.setWidth(dpToPx(20));
            addTextViewToTable(dayOfMonth, dayOfMonthFormat.print(day.getCreateDate()), day.getDayOfCycle() + 3, 1, 0, 1);

            //setTemperature(day);

            TextView dayOfCycle = new TextView(this);
            dayOfCycle.setWidth(dpToPx(20));
            addTextViewToTable(dayOfCycle, day.getDayOfCycle().toString(), day.getDayOfCycle() + 3, 1, 2, 1);

            TextView bleeding = new TextView(this);
            bleeding.setWidth(dpToPx(20));
            addTextViewToTable(bleeding, (day.getBleeding() != null) ? day.getBleeding().toString() : "", day.getDayOfCycle() + 3, 1, 3, 1);

            //tymczasowe
            TextView peekOfMucus = new TextView(this);
            peekOfMucus.setWidth(dpToPx(20));
            addTextViewToTable(peekOfMucus, " ", day.getDayOfCycle() + 3, 1, 4, 1);

            setMucus(day);

            //tymczasowe
            TextView peekOfCervix = new TextView(this);
            peekOfCervix.setWidth(dpToPx(20));
            addTextViewToTable(peekOfCervix, " ", day.getDayOfCycle() + 3, 1, 13, 1);

            setCervix(day);

            TextView cervixHardness = new TextView(this);
            cervixHardness.setWidth(dpToPx(20));
            addTextViewToTable(cervixHardness, (day.getHardnessOfCervix() != null) ? day.getHardnessOfCervix().toString() : "", day.getDayOfCycle() + 3, 1, 15, 1);

            TextView ovulatoryPain = new TextView(this);
            ovulatoryPain.setWidth(dpToPx(20));
            addTextViewToTable(ovulatoryPain, " ", day.getDayOfCycle() + 3, 1, 16, 1);
            if (day.getOvulatoryPain()) {
                ovulatoryPain.setBackgroundColor(Color.BLACK);
            }

            TextView breastTension = new TextView(this);
            breastTension.setWidth(dpToPx(20));
            addTextViewToTable(breastTension, " ", day.getDayOfCycle() + 3, 1, 17, 1);
            if (day.getTensionInTheBreasts()) {
                breastTension.setBackgroundColor(Color.BLACK);
            }
            //tymczasowe
            TextView otherSymptoms = new TextView(this);
            otherSymptoms.setWidth(dpToPx(20));
            addTextViewToTable(otherSymptoms, " ", day.getDayOfCycle() + 3, 1, 18, 1);
            otherSymptoms.setHeight(dpToPx(100));

            //tymczasowe
            TextView prolificDays = new TextView(this);
            prolificDays.setWidth(dpToPx(20));
            addTextViewToTable(prolificDays, " ", day.getDayOfCycle() + 3, 1, 19, 1);

            TextView intercourse = new TextView(this);
            intercourse.setWidth(dpToPx(20));
            addTextViewToTable(intercourse, " ", day.getDayOfCycle() + 3, 1, 20, 1);
            if (day.getIntercourse()) {
                intercourse.setBackgroundColor(Color.BLACK);
            }
            if (iterator.hasNext()) {
                iterator.next();
            }
        }

        GestureFrameLayout zoomLayout = (GestureFrameLayout) findViewById(R.id.zoomLayout);
        zoomLayout.getController().getSettings().setMaxZoom(4f).setDoubleTapZoom(2f).setGravity(Gravity.LEFT).setOverzoomFactor(1f);


    }

    public void setTemperature(Day day) {
        Float temps[] = {37.30F, 37.25F, 37.20F, 37.15F, 37.10F, 37.05F, 37.00F, 36.95F, 36.90F, 36.85F, 36.80F, 36.75F, 36.70F,
                36.65F, 36.60F, 36.55F, 36.50F, 36.45F, 36.40F, 36.35F, 36.30F, 36.25F, 36.20F, 36.15F, 36.10F, 36.05F, 36.00F,
                35.95F, 35.90F, 35.85F, 35.80F, 35.75F, 35.70F, 35.65F, 35.60F, 35.55F, 35.50F};
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

    private void setMucus(Day day) {
        List<MucusType> allMucusList = Arrays.asList(MucusType.WET, MucusType.STRETCHY, MucusType.TRANSPARENT, MucusType.HUMID,
                MucusType.STICKY, MucusType.MUZZY, MucusType.DRY, MucusType.ANOMALOUS);
        List<MucusType> mucusList = day.getMucus();

        int i = 5;
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

    private void setCervix(Day day) {
        ImageView cervixView = new ImageView(this);
        Paint cervixPaint = new Paint();

        cervixPaint.setColor(Color.BLACK);
        cervixPaint.setStyle(Paint.Style.STROKE);
        cervixPaint.setStrokeWidth(dpToPx(3));
        Bitmap cervixBitmap = Bitmap.createBitmap(dpToPx(20), dpToPx(40), Bitmap.Config.ARGB_8888);
        cervixView.setImageBitmap(cervixBitmap);
        Canvas cervixCanvas = new Canvas(cervixBitmap);
        if (day.getPositionOfCervix() >= 0 && day.getDilationOfCervix() >= 0) {
            cervixCanvas.drawCircle(dpToPx(10), dpToPx(30) - dpToPx(2) * day.getPositionOfCervix(), dpToPx(3) * 0.5f + dpToPx(1) * 0.5f * day.getDilationOfCervix(), cervixPaint);
        }
        GridLayout table = (GridLayout) findViewById(R.id.table);
        cervixView.setBackgroundResource(R.drawable.cell_shape);
        cervixView.setMinimumHeight(dpToPx(40));
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(day.getDayOfCycle() + 3, 1);
        params.rowSpec = GridLayout.spec(14, 2);
        cervixView.setLayoutParams(params);
        table.addView(cervixView);
    }

    private void openDayForm(View view) {
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
