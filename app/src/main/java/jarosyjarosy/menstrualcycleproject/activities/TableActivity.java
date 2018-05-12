package jarosyjarosy.menstrualcycleproject.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import jarosyjarosy.menstrualcycleproject.R;
import jarosyjarosy.menstrualcycleproject.config.VerticalTextView;
import jarosyjarosy.menstrualcycleproject.repository.MenstrualCycleDbHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TableActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBar actionbar;
    private MenstrualCycleDbHelper menstrualCycleDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        menstrualCycleDbHelper = new MenstrualCycleDbHelper(this);

        setUpActionBar();
        setUpTable();
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

    public void setUpTable() {
        GridLayout.LayoutParams params;
        GridLayout table = (GridLayout) findViewById(R.id.table);
        table.setColumnCount(3);
        table.setOrientation(GridLayout.HORIZONTAL);


        TextView date = new TextView(this);
        date.setText("DATA:");
        date.setBackgroundResource(R.drawable.cell_shape);
        table.addView(date);
        params = new GridLayout.LayoutParams(date.getLayoutParams());
        params.columnSpec = GridLayout.spec(0, 3);
        date.setLayoutParams(params);

        VerticalTextView temperature = new VerticalTextView(this);
        temperature.setText("TEMPERATURA");
        temperature.set
        temperature.setBackgroundResource(R.drawable.cell_shape);
        table.addView(temperature);
        params = new GridLayout.LayoutParams(temperature.getLayoutParams());
        params.rowSpec = GridLayout.spec(0, 27);
        temperature.setLayoutParams(params);



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
