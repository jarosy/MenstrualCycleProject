package jarosyjarosy.menstrualcycleproject.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import jarosyjarosy.menstrualcycleproject.R;
import jarosyjarosy.menstrualcycleproject.config.ExpandableListAdapter;
import jarosyjarosy.menstrualcycleproject.models.Cycle;
import jarosyjarosy.menstrualcycleproject.models.Day;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;
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

public class ListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    DateTimeFormatter appDateFormat = DateTimeFormat.forPattern("dd.MM.yyyy");

    private DatabaseAdapter dbAdapter;
    private Cursor cycleCursor;
    private Cursor dayCursor;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<Cycle> listDataHeader;
    private HashMap<Cycle, List<Day>> listDataChild;

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
                        if (menuItem.getTitle().toString().matches("Moje cykle")) {
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
        listDataHeader = new ArrayList<Cycle>();
        listDataChild = new HashMap<Cycle, List<Day>>();

        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();

        List<Cycle> cycleList = new ArrayList<>();
        cycleCursor = dbAdapter.getAllCycles();
        cycleCursor.moveToFirst();
        if(cycleCursor != null && cycleCursor.moveToFirst()) {
            do {
                Cycle cycle = dbAdapter.getCycle(cycleCursor.getLong(0));
                cycleList.add(cycle);
            } while (cycleCursor.moveToNext());
        }
        listDataHeader = cycleList;
        int cycleCounter = 0;
        for (Cycle cycle : cycleList) {
            List<Day> dayList = new ArrayList<>();
            dayCursor = dbAdapter.getAllDaysFromCycle(cycle.getCycleId());
            dayCursor.moveToFirst();
            do {
                Day day = dbAdapter.getDay(dayCursor.getLong(0));
                dayList.add(day);
            } while (dayCursor.moveToNext());
            listDataChild.put(listDataHeader.get(cycleCounter),dayList);
            cycleCounter++;
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

    public void addCycleManually(View view) {
        Toast.makeText(this, "Dodawanie cykli w produkcji",
                Toast.LENGTH_LONG).show();
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

}
