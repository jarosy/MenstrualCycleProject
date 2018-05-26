package jarosyjarosy.menstrualcycleproject.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import jarosyjarosy.menstrualcycleproject.R;
import jarosyjarosy.menstrualcycleproject.config.ExpandableListAdapter;
import jarosyjarosy.menstrualcycleproject.models.Cycle;
import jarosyjarosy.menstrualcycleproject.models.Day;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;
import jarosyjarosy.menstrualcycleproject.validators.CycleValidator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class ListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    DateTimeFormatter appDateFormat = DateTimeFormat.forPattern("dd.MM.yyyy");

    private DatabaseAdapter dbAdapter;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private EditText cycleDatePicker;
    private PopupWindow cycleEdit;
    private List<Cycle> listDataHeader;
    private HashMap<Cycle, List<Day>> listDataChild;

    private CycleValidator validator = new CycleValidator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setUpActionbar();

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableList);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
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
                        if (menuItem.getTitle().toString().matches(getString(R.string.show_cycles))) {
                            //openList(navigationView);
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

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();

        List<Cycle> cycleList = dbAdapter.getAllCycles();
        listDataHeader = cycleList;
        int cycleCounter = 0;
        for (Cycle cycle : cycleList) {
            List<Day> dayList = dbAdapter.getAllDaysFromCycle(cycle.getCycleId());
            listDataChild.put(listDataHeader.get(cycleCounter), dayList);
            cycleCounter++;
        }
    }

    public void openCycleForm(View view) {
        LayoutInflater inflater = (android.view.LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.cycle_edit, null);
        cycleEdit = new PopupWindow(popupView, DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
        cycleEdit.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));
        cycleEdit.setOutsideTouchable(true);
        cycleEdit.showAtLocation(drawerLayout, Gravity.CENTER, 0, 0);
        cycleDatePicker = (EditText) popupView.findViewById(R.id.cycledatePicker);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                DateTime dateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
                cycleDatePicker.setText(appDateFormat.print(dateTime));
            }

        };
        cycleDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ListActivity.this, date, DateTime.now().getYear(),
                        DateTime.now().getMonthOfYear() - 1,
                        DateTime.now().getDayOfMonth()).show();
            }
        });
    }

    public void onCycleSave(View view) {
        Cycle newCycle = new Cycle();
        newCycle.setStartDate(appDateFormat.parseDateTime(cycleDatePicker.getText().toString()));
        if (validator.validateCycle(this, newCycle)) {
            dbAdapter = new DatabaseAdapter(this);
            dbAdapter.open();
            dbAdapter.insertCycle(newCycle);
            dbAdapter.close();
            cycleEdit.dismiss();
            refreshActivity();
        } else {
            Toast.makeText(this, "Cykl w tym okresie już istnieje!", Toast.LENGTH_LONG).show();
        }
    }

    public void openDayForm(View view) {
        Boolean setDate = false;
        if (view.getTag() != null) {
            setDate = Boolean.valueOf(view.getTag().toString());
        }
        Intent intent = new Intent(ListActivity.this, DayFormActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("setDate", setDate);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void openTable(View view) {
        Intent intent = new Intent(ListActivity.this, TableActivity.class);
        startActivity(intent);
    }

    private void openList(View view) {
        Intent intent = new Intent(ListActivity.this, ListActivity.class);
        startActivity(intent);
    }

    private void writeToSD() throws IOException {
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

    public void refreshActivity() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
