package jarosyjarosy.menstrualcycleproject.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import jarosyjarosy.menstrualcycleproject.R;
import jarosyjarosy.menstrualcycleproject.models.*;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Button newDayButton;
    private Button newCycleButton;

    private boolean doubleBackToExitPressedOnce = false;

    DateTimeFormatter appDateFormat = DateTimeFormat.forPattern("dd.MM.yyyy");

    private DatabaseAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpActionbar();
    }

    public void setUpActionbar() {
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
                        if (menuItem.getTitle().toString().matches("Dodaj dzień")) {
                            openDayForm(navigationView);
                        }
                        if (menuItem.getTitle().toString().matches("Moje cykle")) {
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

    public void openDayForm(View view) {
        Boolean setDate = false;
        if (view.getTag() != null) {
            setDate = Boolean.valueOf(view.getTag().toString());
        }
        Intent intent = new Intent(MainActivity.this, DayFormActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("setDate", setDate);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void openTable(View view) {
        Intent intent = new Intent(MainActivity.this, TableActivity.class);
        startActivity(intent);
    }

    private void openList(View view) {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);
    }

    public void openPopUp(View view) {
        setDatabaseStub(view);
        Intent intent = new Intent(MainActivity.this, Pop.class);
        startActivity(intent);
    }

    public void setDatabaseStub(View view) {
        this.deleteDatabase("menstrualcycle.db");

        Cycle cycle1 = new Cycle();
        cycle1.setStartDate(DateTime.parse("2018-01-21"));
        cycle1.setEndDate(DateTime.parse("2018-01-22"));

        Cycle cycle2 = new Cycle();
        cycle2.setStartDate(DateTime.parse("2018-01-23"));
        cycle2.setEndDate(DateTime.parse("2018-01-24"));

        Cycle cycle3 = new Cycle();
        cycle3.setStartDate(DateTime.parse("2018-01-25"));

        Day day1 = new Day();
        day1.setCreateDate(DateTime.parse("2018-01-21"));
        day1.setDayOfCycle(1);
        day1.setTemperature(36.00F);
        day1.setBleeding(BleedingType.BLEEDING_NO);
        day1.setMucus(Arrays.asList(MucusType.HUMID, MucusType.STRETCHY));
        day1.setDilationOfCervix(10);
        day1.setPositionOfCervix(10);
        day1.setHardnessOfCervix(CervixHardnessType.HARD);
        day1.setOvulatoryPain(false);
        day1.setTensionInTheBreasts(false);
        day1.setOtherSymptoms("Zakłócenia");
        day1.setIntercourse(false);
        day1.setCycleId(1L);

        Day day2 = new Day();
        day2.setCreateDate(DateTime.parse("2018-01-22"));
        day2.setDayOfCycle(2);
        day2.setTemperature(36.45F);
        day2.setBleeding(BleedingType.BLEEDING_YES);
        day2.setMucus(Collections.singletonList(MucusType.WET));
        day2.setDilationOfCervix(1);
        day2.setPositionOfCervix(1);
        day2.setHardnessOfCervix(CervixHardnessType.SOFT);
        day2.setOvulatoryPain(false);
        day2.setTensionInTheBreasts(false);
        day2.setOtherSymptoms(null);
        day2.setIntercourse(false);
        day2.setCycleId(1L);

        Day day3 = new Day();
        day3.setCreateDate(DateTime.parse("2018-01-23"));
        day3.setDayOfCycle(1);
        day3.setTemperature(37.30F);
        day3.setBleeding(BleedingType.BLEEDING_YES);
        day3.setMucus(Collections.singletonList(MucusType.MUZZY));
        day3.setDilationOfCervix(10);
        day3.setPositionOfCervix(1);
        day3.setHardnessOfCervix(CervixHardnessType.HARD);
        day3.setOvulatoryPain(true);
        day3.setTensionInTheBreasts(true);
        day3.setOtherSymptoms(null);
        day3.setIntercourse(false);
        day3.setCycleId(2L);

        Day day4 = new Day();
        day4.setCreateDate(DateTime.parse("2018-01-24"));
        day4.setDayOfCycle(2);
        day4.setTemperature(36.50F);
        day4.setBleeding(BleedingType.BLEEDING_SPOTTING);
        day4.setMucus(Collections.singletonList(MucusType.DRY));
        day4.setDilationOfCervix(1);
        day4.setPositionOfCervix(10);
        day4.setHardnessOfCervix(CervixHardnessType.SOFT);
        day4.setOvulatoryPain(true);
        day4.setTensionInTheBreasts(false);
        day4.setOtherSymptoms(null);
        day4.setIntercourse(false);
        day4.setCycleId(2L);

        Day day5 = new Day();
        day5.setCreateDate(DateTime.parse("2018-01-25"));
        day5.setDayOfCycle(1);
        day5.setTemperature(36.70F);
        day5.setBleeding(BleedingType.BLEEDING_SPOTTING);
        day5.setMucus(Arrays.asList(MucusType.STRETCHY, MucusType.WET, MucusType.TRANSPARENT));
        day5.setDilationOfCervix(5);
        day5.setPositionOfCervix(5);
        day5.setHardnessOfCervix(CervixHardnessType.SOFT);
        day5.setOvulatoryPain(false);
        day5.setTensionInTheBreasts(false);
        day5.setOtherSymptoms("Zakłócenia");
        day5.setIntercourse(true);
        day5.setCycleId(3L);

        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();

        dbAdapter.insertCycle(cycle1);
        dbAdapter.insertCycle(cycle2);
        dbAdapter.insertCycle(cycle3);

        dbAdapter.insertDay(day1);
        dbAdapter.insertDay(day2);
        dbAdapter.insertDay(day3);
        dbAdapter.insertDay(day4);
        dbAdapter.insertDay(day5);

        dbAdapter.close();

        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void writeToSD() throws IOException {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE},1);

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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Wciśnij WSTECZ ponownie, aby wyjść.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
