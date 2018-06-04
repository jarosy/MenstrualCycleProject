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
import jarosyjarosy.menstrualcycleproject.validators.DayValidator;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static jarosyjarosy.menstrualcycleproject.R.id.radioBleedNo;
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

    private CheckBox checkNoTemperature;
    private CheckBox checkNoCervix;

    private RadioButton radioBleedYes;
    private RadioButton radioBleedNo;
    private RadioButton radioBleedSpot;
    private RadioButton radioHard;
    private RadioButton radioSoft;
    private RadioButton radioPainYes;
    private RadioButton radioPainNo;
    private RadioButton radioTensionYes;
    private RadioButton radioTensionNo;
    private RadioButton radioIntercourseYes;
    private RadioButton radioIntecourseNo;

    private DateTimeFormatter appDateFormat = DateTimeFormat.forPattern("dd.MM.yyyy");

    private Canvas cervixCanvas;
    private Paint cervixPaint = new Paint();
    private Bitmap cervixBitmap;
    private ImageView cervixView;
    private float circleY;
    private float circleRadius;

    private DatabaseAdapter dbAdapter;
    private Cycle cycle;
    private Day day;

    private DayValidator validator = new DayValidator();

    private List<String> temps = Arrays.asList("35.50℃", "35.55℃", "35.60℃", "35.65℃", "35.70℃", "35.75℃", "35.80℃", "35.85℃",
            "35.90℃", "35.95℃", "36.00℃", "36.05℃", "36.10℃", "36.15℃", "36.20℃", "36.25℃", "36.30℃", "36.35℃", "36.40℃", "36.45℃",
            "36.50℃", "36.55℃", "36.60℃", "36.65℃", "36.70℃", "36.75℃", "36.80℃", "36.85℃", "36.90℃", "36.95℃", "37.00℃", "37.05℃",
            "37.10℃", "37.15℃", "37.20℃", "37.25℃", "37.30℃");

    private List<Float> tempsLong = Arrays.asList(35.50F, 35.55F, 35.60F, 35.65F,35.70F, 35.75F, 35.80F, 35.85F, 35.90F, 35.95F, 36.00F,
            36.05F, 36.10F, 36.15F, 36.20F, 36.25F, 36.30F, 36.35F, 36.40F, 36.45F, 36.50F, 36.55F, 36.60F, 36.65F,
            36.70F, 36.75F, 36.80F, 36.85F, 36.90F, 36.95F, 37.00F, 37.05F, 37.10F, 37.15F, 37.20F, 37.25F, 37.30F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dayform);

        getCycleAndDay();
        setUpViews();
        setUpActionBar();
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

        checkNoTemperature = (CheckBox) findViewById(R.id.checkNoTemperature);
        checkNoCervix = (CheckBox) findViewById(R.id.checkNoCervix);

        checkNoTemperature.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(checkNoTemperature.isChecked()) {
                    temperaturePicker.setEnabled(false);
                } else {
                    temperaturePicker.setEnabled(true);
                }
            }
        });

        radioBleedNo = (RadioButton) findViewById(R.id.radioBleedNo);
        radioBleedYes = (RadioButton) findViewById(R.id.radioBleedYes);
        radioBleedSpot = (RadioButton) findViewById(R.id.radioSpotting);
        radioHard = (RadioButton) findViewById(R.id.radioHard);
        radioSoft = (RadioButton) findViewById(R.id.radioSoft);
        radioPainNo = (RadioButton) findViewById(R.id.radioPainNo);
        radioPainYes = (RadioButton) findViewById(R.id.radioPainYes);
        radioTensionNo = (RadioButton) findViewById(R.id.radioBreastNo);
        radioTensionYes = (RadioButton) findViewById(R.id.radioBreastYes);
        radioIntecourseNo = (RadioButton) findViewById(R.id.radioIntercourseNo);
        radioIntercourseYes = (RadioButton) findViewById(R.id.radioIntercourseYes);

        if (bundle.getLong("dayId") > 0) {
            if (day.getTemperature() < 35.0F) {
                checkNoTemperature.setChecked(true);
                temperaturePicker.setEnabled(false);
            }
            if (day.getDilationOfCervix() < 0 || day.getPositionOfCervix() < 0) {
                checkNoCervix.setChecked(true);
                seekBarDilation.setEnabled(false);
                seekBarPosition.setEnabled(false);
                for (int i = 0; i < hardnessGroup.getChildCount(); i++) {
                    hardnessGroup.getChildAt(i).setEnabled(false);
                }
            }

            List<MucusType> mucusList = day.getMucus();
            if (mucusList.contains(MucusType.WET)) wetCheckbox.setChecked(true);
            if (mucusList.contains(MucusType.STRETCHY)) stretchyCheckbox.setChecked(true);
            if (mucusList.contains(MucusType.TRANSPARENT)) transparentCheckbox.setChecked(true);
            if (mucusList.contains(MucusType.HUMID)) humidCheckbox.setChecked(true);
            if (mucusList.contains(MucusType.STICKY)) stickyCheckbox.setChecked(true);
            if (mucusList.contains(MucusType.MUZZY)) muzzyCheckbox.setChecked(true);
            if (mucusList.contains(MucusType.DRY)) dryCheckbox.setChecked(true);
            if (mucusList.contains(MucusType.ANOMALOUS)) anomalousCheckbox.setChecked(true);

            if (day.getHardnessOfCervix() != null && day.getHardnessOfCervix().toString().equals("T"))
                radioHard.setChecked(true);
            if (day.getHardnessOfCervix() != null && day.getHardnessOfCervix().toString().equals("M"))
                radioSoft.setChecked(true);
            if (day.getBleeding().toString().equals(" ")) radioBleedNo.setChecked(true);
            if (day.getBleeding().toString().equals("K")) radioBleedYes.setChecked(true);
            if (day.getBleeding().toString().equals("P")) radioBleedSpot.setChecked(true);
            if (day.getOvulatoryPain()) {
                radioPainYes.setChecked(true);
            } else {
                radioPainNo.setChecked(true);
            }
            if (day.getTensionInTheBreasts()) {
                radioTensionYes.setChecked(true);
            } else {
                radioTensionNo.setChecked(true);
            }
            if (day.getIntercourse()) {
                radioIntercourseYes.setChecked(true);
            } else {
                radioIntecourseNo.setChecked(true);
            }
            otherSymptoms.setText(day.getOtherSymptoms());

        }
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

    private void getCycleAndDay() {
        bundle = getIntent().getExtras();
        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        cycle = dbAdapter.getCycle(bundle.getLong("cycleId"));
        if (bundle.getLong("dayId") > 0) {
            day = dbAdapter.getDay(bundle.getLong("dayId"));
        }
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
        if (bundle.getLong("dayId") > 0) {
            datePicker.setText(appDateFormat.print(day.getCreateDate()));
            datePicker.setEnabled(false);
        }
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                DateTime dateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
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
        temperaturePicker.setMaxValue(temps.size() - 1);
        temperaturePicker.setMinValue(0);
        temperaturePicker.setWrapSelectorWheel(false);
        temperaturePicker.setDisplayedValues((String[]) temps.toArray());
        temperaturePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        temperaturePicker.setValue(22);

        if (bundle.getLong("dayId") > 0) {
            int index = tempsLong.indexOf(day.getTemperature());
            temperaturePicker.setValue(index);
        }
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
        if (bundle.getLong("dayId") > 0) {
            circleRadius = 15 + day.getDilationOfCervix() * 5;
            circleY = 230 - day.getPositionOfCervix() * 12;
            seekBarDilation.setProgress(day.getDilationOfCervix());
            seekBarPosition.setProgress(day.getPositionOfCervix());
        }

        if (checkNoCervix.isChecked()) {
            cervixCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        } else {
            cervixCanvas.drawCircle(160, circleY, circleRadius, cervixPaint);
        }

        checkNoCervix.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (checkNoCervix.isChecked()) {
                    cervixCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    seekBarPosition.setEnabled(false);
                    seekBarDilation.setEnabled(false);
                    for (int i = 0; i < hardnessGroup.getChildCount(); i++) {
                        hardnessGroup.getChildAt(i).setEnabled(false);
                    }
                } else {
                    cervixCanvas.drawCircle(160, circleY, circleRadius, cervixPaint);
                    seekBarPosition.setEnabled(true);
                    seekBarDilation.setEnabled(true);
                    for (int i = 0; i < hardnessGroup.getChildCount(); i++) {
                        hardnessGroup.getChildAt(i).setEnabled(true);
                    }
                }
            }
        });

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
        if (saveDay()) {
            Toast.makeText(this, "Nowy dzień zapisany!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(DayFormActivity.this, ListActivity.class);
            startActivity(intent);
        }
    }

    private Boolean saveDay() {
        if (datePicker.getText().toString().equals("")) {
            Toast.makeText(this, "Uzupełnij brakujące dane!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            dbAdapter = new DatabaseAdapter(this);
            dbAdapter.open();

            Day newDay = new Day();
            newDay.setCreateDate(appDateFormat.parseDateTime(datePicker.getText().toString()));
            newDay.setDayOfCycle(Days.daysBetween(cycle.getStartDate().minusDays(1), appDateFormat.parseDateTime(datePicker.getText().toString())).getDays());
            if (checkNoTemperature.isChecked()) {
                newDay.setTemperature(null);
            } else {
                newDay.setTemperature(getTemperature());
            }

            newDay.setBleeding(BleedingType.fromString(getStringFromRadioGroup(bleedingGroup)));
            newDay.setMucus(getMucusTypes());
            if (checkNoCervix.isChecked()) {
                newDay.setDilationOfCervix(-1);
                newDay.setPositionOfCervix(-1);
                newDay.setHardnessOfCervix(null);
            } else {
                newDay.setDilationOfCervix(seekBarDilation.getProgress());
                newDay.setPositionOfCervix(seekBarPosition.getProgress());
                newDay.setHardnessOfCervix(CervixHardnessType.fromString(getStringFromRadioGroup(hardnessGroup)));
            }
            newDay.setOvulatoryPain(getBooleanFromRadioGroup(painGroup));
            newDay.setTensionInTheBreasts(getBooleanFromRadioGroup(tensionGroup));
            newDay.setOtherSymptoms(otherSymptoms.getText().toString());
            newDay.setIntercourse(getBooleanFromRadioGroup(intercourseGroup));
            newDay.setCycleId(cycle.getCycleId());

            if (day != null) {
                newDay.setDayId(day.getDayId());
                dbAdapter.updateDay(newDay);
                dbAdapter.close();
                try {
                    writeToSD();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                if (validator.validateDay(this, newDay, cycle)) {
                    dbAdapter.insertDay(newDay);
                    dbAdapter.close();
                    try {
                        writeToSD();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    dbAdapter.close();
                    return false;
                }
            }
        }
    }

    private Float getTemperature() {
        String stringTemp = temps.get(temperaturePicker.getValue());
        return Float.valueOf(stringTemp.replace("℃", "").replace(",", "."));
    }

    private String getStringFromRadioGroup(RadioGroup radioGroup) {
        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        if (radioButton != null) {
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
        ActivityCompat.requestPermissions(DayFormActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, 1);

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
