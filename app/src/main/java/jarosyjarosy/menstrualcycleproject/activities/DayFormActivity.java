package jarosyjarosy.menstrualcycleproject.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import jarosyjarosy.menstrualcycleproject.R;
import jarosyjarosy.menstrualcycleproject.models.*;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static jarosyjarosy.menstrualcycleproject.R.id.radioGroupBleeding;
import static jarosyjarosy.menstrualcycleproject.R.id.sticky_checkbox;

public class DayFormActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Bundle bundle;
    private EditText datePicker;
    private NumberPicker temperaturePicker;
    private RadioGroup bleedingGroup;
    private CheckBox wetCheckbox;
    private CheckBox stretchyCheckbox;
    private CheckBox transparentCheckbox;
    private CheckBox humidCheckbox;
    private CheckBox stickyCheckbox;
    private CheckBox muzzyCheckbox;
    private CheckBox dryCheckbox;
    private CheckBox anomalousCheckbox;
    private SeekBar seekBarPosition;
    private SeekBar seekBarDilation;
    private RadioGroup hardnessGroup;
    private RadioGroup painGroup;
    private RadioGroup tensionGroup;
    private EditText otherSymptoms;
    private RadioGroup intercourseGroup;

    private DateTimeFormatter appDateFormat = DateTimeFormat.forPattern("dd.MM.yyyy");

    private Canvas cervixCanvas;
    private Paint cervixPaint = new Paint();
    private Bitmap cervixBitmap;
    private ImageView cervixView;
    private float circleY;
    private float circleRadius;

    private DatabaseAdapter dbAdapter;
    private Cycle cycle;

    private String temps[] = {"36,00℃", "36,05℃", "36,10℃", "36,15℃", "36,20℃", "36,25℃", "36,30℃", "36,35℃", "36,40℃", "36,45℃", "36,50℃", "36,55℃", "36,60℃", "36,65℃",
            "36,70℃", "36,75℃", "36,80℃", "36,85℃", "36,90℃", "36,95℃", "37,00℃", "37,05℃", "37,10℃", "37,15℃", "37,20℃", "37,25℃", "37,30℃"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dayform);

        setUpViews();
        setUpActionBar();
        getCycle();
        setUpDate();
        setUpTemperaturePicker();
        setUpCervixDrawing();

    }

    public void setUpViews() {
        datePicker = (EditText) findViewById(R.id.dateEdit);
        temperaturePicker = (NumberPicker) findViewById(R.id.temperaturePicker);
        bleedingGroup = (RadioGroup) findViewById(R.id.radioGroupBleeding);
        wetCheckbox = (CheckBox) findViewById(R.id.wet_checkbox);
        stretchyCheckbox = (CheckBox) findViewById(R.id.stretchy_checkbox);
        transparentCheckbox = (CheckBox) findViewById(R.id.transparent_checkbox);
        humidCheckbox = (CheckBox) findViewById(R.id.humid_checkbox);
        stickyCheckbox = (CheckBox) findViewById(R.id.sticky_checkbox);
        muzzyCheckbox = (CheckBox) findViewById(R.id.muzzy_checkbox);
        dryCheckbox = (CheckBox) findViewById(R.id.dry_checkbox);
        anomalousCheckbox = (CheckBox) findViewById(R.id.anomalous_checkbox);
        seekBarPosition = (SeekBar) findViewById(R.id.seekPosition);
        seekBarDilation = (SeekBar) findViewById(R.id.seekDilation);
        hardnessGroup = (RadioGroup) findViewById(R.id.radioGroupHardness);
        painGroup = (RadioGroup) findViewById(R.id.radioGroupOvulatoryPain);
        tensionGroup = (RadioGroup) findViewById(R.id.radioGroupBreastTension);
        otherSymptoms = (EditText) findViewById(R.id.other);
        intercourseGroup = (RadioGroup) findViewById(R.id.radioIntercourse);
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

    private void getCycle() {
        bundle = getIntent().getExtras();
        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        cycle = dbAdapter.getCycle(bundle.getLong("cycleId"));
        dbAdapter.close();
    }

    private void setUpDate() {
        bundle = getIntent().getExtras();
        boolean setDate = false;
        if (bundle != null) {
            setDate = bundle.getBoolean("setDate");
        }
        if (setDate) {
            datePicker.setText(appDateFormat.print(new DateTime()));
            datePicker.setEnabled(false);
        }
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                DateTime dateTime = new DateTime(year, monthOfYear +1 , dayOfMonth, 0, 0);
                datePicker.setText(appDateFormat.print(dateTime));
            }

        };
        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(DayFormActivity.this, date, DateTime.now().getYear(),
                        DateTime.now().getMonthOfYear() - 1,
                        DateTime.now().getDayOfMonth()).show();
            }
        });
    }

    private void setUpTemperaturePicker() {
        temperaturePicker.setMaxValue(temps.length - 1);
        temperaturePicker.setMinValue(0);
        temperaturePicker.setWrapSelectorWheel(false);
        temperaturePicker.setDisplayedValues(temps);
        temperaturePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        temperaturePicker.setValue(12);
    }

    public void setUpCervixDrawing() {
        cervixView = (ImageView) findViewById(R.id.cervixCanvas);
        cervixPaint.setColor(Color.BLACK);
        cervixPaint.setStyle(Paint.Style.STROKE);
        cervixPaint.setStrokeWidth(25);
        cervixBitmap = Bitmap.createBitmap(320, 320, Bitmap.Config.ARGB_8888);
        cervixView.setImageBitmap(cervixBitmap);
        cervixCanvas = new Canvas(cervixBitmap);
        circleRadius = 15;
        circleY = 230;

        cervixCanvas.drawCircle(160, circleY, circleRadius, cervixPaint);

        seekBarDilation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                circleRadius = 15 + 5 * i;
                cervixCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                cervixCanvas.drawCircle(160, circleY, circleRadius, cervixPaint);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBarPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                circleY = 230 - 12 * i;
                cervixCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                cervixCanvas.drawCircle(160, circleY, circleRadius, cervixPaint);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
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

    private void openList(View view) {
        Intent intent = new Intent(DayFormActivity.this, ListActivity.class);
        startActivity(intent);
    }

    public void onSaveButtonClick(View view) {
        if(saveDay()) {
            Toast.makeText(this, "Nowy dzień zapisany!",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(DayFormActivity.this, ListActivity.class);
            startActivity(intent);
        }
    }

    private Boolean saveDay() {
        if (datePicker.getText().toString().equals("")) {
            Toast.makeText(this, "Uzupełnij brakujące dane!",
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            dbAdapter = new DatabaseAdapter(this);
            dbAdapter.open();

            Day newDay = new Day();
            newDay.setCreateDate(appDateFormat.parseDateTime(datePicker.getText().toString()));
            newDay.setDayOfCycle(Days.daysBetween(cycle.getStartDate(), appDateFormat.parseDateTime(datePicker.getText().toString())).getDays() + 1);
            newDay.setTemperature(getTemperature());
            newDay.setBleeding(BleedingType.fromString(getStringFromRadioGroup(bleedingGroup)));
            newDay.setMucus(getMucusTypes());
            newDay.setDilationOfCervix(seekBarDilation.getProgress());
            newDay.setPositionOfCervix(seekBarPosition.getProgress());
            newDay.setHardnessOfCervix(CervixHardnessType.fromString(getStringFromRadioGroup(hardnessGroup)));
            newDay.setOvulatoryPain(getBooleanFromRadioGroup(painGroup));
            newDay.setTensionInTheBreasts(getBooleanFromRadioGroup(tensionGroup));
            newDay.setOtherSymptoms(otherSymptoms.getText().toString());
            newDay.setIntercourse(getBooleanFromRadioGroup(intercourseGroup));
            newDay.setCycleId(cycle.getCycleId());

            dbAdapter.insertDay(newDay);
            dbAdapter.close();

            try {
                writeToSD();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  true;
        }
    }

    private Float getTemperature() {
        String stringTemp = temps[temperaturePicker.getValue()];
        return Float.valueOf(stringTemp.replace("℃", "").replace(",", "."));
    }

    private String getStringFromRadioGroup(RadioGroup radioGroup) {
        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        if(radioButton != null){
            return radioButton.getText().toString();
        }
        return null;
    }

    private Boolean getBooleanFromRadioGroup(RadioGroup radioGroup) {
        String answer = getStringFromRadioGroup(radioGroup);
        return answer != null && answer.equalsIgnoreCase("tak");
    }

    private List<MucusType> getMucusTypes() {
        List<CheckBox> checkboxList = Arrays.asList(wetCheckbox, stretchyCheckbox, transparentCheckbox, humidCheckbox,
                stickyCheckbox, muzzyCheckbox, dryCheckbox, anomalousCheckbox);
        List<MucusType> mucusTypeList = new ArrayList<>();
        for (CheckBox checkbox : checkboxList) {
            if (checkbox.isChecked()) {
                mucusTypeList.add(MucusType.fromString(checkbox.getText().toString()));
            }
        }
        return mucusTypeList;
    }

    private void writeToSD() throws IOException {
        ActivityCompat.requestPermissions(DayFormActivity.this, new String[]{WRITE_EXTERNAL_STORAGE},1);

        File sd = Environment.getExternalStorageDirectory();
        String backupDBPath = "menstrualcyclebackup.db";
        File currentDB = new File(this.getDatabasePath("menstrualcycle.db").toString());
        File backupDB = new File(sd, backupDBPath);

        if (currentDB.exists()) {
            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        }
    }

}
