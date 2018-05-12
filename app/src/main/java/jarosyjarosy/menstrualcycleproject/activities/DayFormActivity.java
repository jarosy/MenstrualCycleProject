package jarosyjarosy.menstrualcycleproject.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import jarosyjarosy.menstrualcycleproject.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DayFormActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionbar;
    private DrawerLayout drawerLayout;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat appDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private EditText datePicker;
    private NumberPicker temperaturePicker;
    private SeekBar seekBarPosition;
    private SeekBar seekBarDilation;

    private Canvas cervixCanvas;
    private Paint cervixPaint = new Paint();
    private Bitmap cervixBitmap;
    private ImageView cervixView;
    private float circleY;
    private float circleRadius;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dayform);

        setUpActionBar();
        setUpDate();
        setUpTemperaturePicker();
        setUpCervixDrawing();

    }

    public void setUpActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
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
                        if (menuItem.getTitle().toString().matches("Dodaj dzień")) {
                            openDayForm(navigationView);
                        }
                        if (menuItem.getTitle().toString().matches("Moje cykle")) {
                            openTable(navigationView);
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

    private void setUpDate() {
        Bundle b = getIntent().getExtras();
        boolean setDate = false;
        if (b != null) {
            setDate = b.getBoolean("setDate");
        }

        datePicker = (EditText) findViewById(R.id.dateEdit);
        if (setDate) {
            calendar.add(Calendar.DATE, 1);
            datePicker.setText(appDateFormat.format(calendar.getTime()));
            datePicker.setEnabled(false);
        }
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                datePicker.setText(appDateFormat.format(calendar.getTime()));
            }

        };
        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(DayFormActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setUpTemperaturePicker() {
        temperaturePicker = (NumberPicker) findViewById(R.id.temperaturePicker);
        String temps[] = {"36,00℃", "36,05℃", "36,10℃", "36,15℃", "36,20℃", "36,25℃", "36,30℃", "36,35℃", "36,40℃", "36,45℃", "36,50℃", "36,55℃", "36,60℃", "36,65℃",
                "36,70℃", "36,75℃", "36,80℃", "36,85℃", "36,90℃", "36,95℃", "37,00℃", "37,05℃", "37,10℃", "37,15℃", "37,20℃", "37,25℃", "37,30℃", "37,35℃", "37,40℃"};
        temperaturePicker.setMaxValue(temps.length - 1);
        temperaturePicker.setMinValue(0);
        temperaturePicker.setWrapSelectorWheel(false);
        temperaturePicker.setDisplayedValues(temps);
        temperaturePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        temperaturePicker.setValue(12);
    }

    public void setUpCervixDrawing() {
        seekBarDilation = (SeekBar) findViewById(R.id.seekDilation);
        seekBarPosition = (SeekBar) findViewById(R.id.seekPosition);
        cervixView = (ImageView) findViewById(R.id.cervixCanvas);
        cervixPaint.setColor(Color.BLACK);
        cervixPaint.setStyle(Paint.Style.STROKE);
        cervixPaint.setStrokeWidth(10);
        cervixBitmap = Bitmap.createBitmap(160, 160, Bitmap.Config.ARGB_8888);
        cervixView.setImageBitmap(cervixBitmap);
        cervixCanvas = new Canvas(cervixBitmap);
        circleRadius = 20;
        circleY = 105;

        cervixCanvas.drawCircle(80, circleY, circleRadius ,cervixPaint);

        seekBarDilation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                circleRadius = 20 + 3*i;
                cervixCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                cervixCanvas.drawCircle(80, circleY, circleRadius ,cervixPaint);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        seekBarPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                circleY = 105 - 5*i;
                cervixCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                cervixCanvas.drawCircle(80, circleY, circleRadius ,cervixPaint);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    public void openDayForm(View view) {
        Boolean setDate = false;
        if (view.getTag() != null) {
            setDate = Boolean.valueOf(view.getTag().toString());
        }
        Intent intent = new Intent(DayFormActivity.this, DayFormActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("setDate", setDate);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void openTable(View view) {
        Intent intent = new Intent(DayFormActivity.this, TableActivity.class);
        startActivity(intent);
    }

}
