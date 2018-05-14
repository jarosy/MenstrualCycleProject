package jarosyjarosy.menstrualcycleproject.activities;

import android.content.Intent;
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
import android.widget.TextView;
import jarosyjarosy.menstrualcycleproject.R;
import jarosyjarosy.menstrualcycleproject.config.VerticalTextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TableActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        setUpActionBar();
        setUpTable();
    }

    public void getDatabaseStub() {

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
                            openList(navigationView);
                        }
                        if (menuItem.getTitle().toString().matches("Tabelka")) {
                            //openTable(navigationView);
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

    public void setUpTable() {
        GridLayout table = (GridLayout) findViewById(R.id.table);
        table.setColumnCount(3);
        table.setRowCount(47);
        table.setOrientation(GridLayout.VERTICAL);

        TextView date = new TextView(this);
        date.setWidth(dpToPx(220));
        addTextViewToTable(date, " DATA: ",0, 3,0,1);

        VerticalTextView temperature = new VerticalTextView(this);
        temperature.setWidth(dpToPx(27*20));
        addTextViewToTable(temperature,"                                                               TEMPERATURA "
                ,0,1,1,27);
        temperature.setHeight(dpToPx(40));

        TextView day = new TextView(this);
        day.setWidth(dpToPx(220));
        addTextViewToTable(day," DZIEŃ CYKLU ",0,3,28,1);

        TextView bleeding = new TextView(this);
        bleeding.setWidth(dpToPx(220));
        addTextViewToTable(bleeding," KRWAWIENIE/PLAMIENIE ",0,3,29,1);

        TextView peekOfMucus = new TextView(this);
        peekOfMucus.setWidth(dpToPx(220));
        addTextViewToTable(peekOfMucus," SZCZYT ŚLUZU ",0,3,30,1);

        VerticalTextView mucus = new VerticalTextView(this);
        mucus.setWidth(dpToPx(7*20));
        addTextViewToTable(mucus,"               ŚLUZ ",0,1,31,7);
        mucus.setHeight(dpToPx(40));

        TextView mucusAnomalous = new TextView(this);
        mucusAnomalous.setWidth(dpToPx(220));
        addTextViewToTable(mucusAnomalous," ŚLUZ/WYDZIELINA NIETYPOWA ",0,3,38,1);

        TextView peekOfCervix = new TextView(this);
        peekOfCervix.setWidth(dpToPx(220));
        addTextViewToTable(peekOfCervix," SZCZYT SZYJKI MACICY ",0,3,39,1);

        VerticalTextView cervix = new VerticalTextView(this);
        cervix.setWidth(dpToPx(60));
        addTextViewToTable(cervix," SZYJKA ",0,1,40,2);
        cervix.setHeight(dpToPx(40));

        VerticalTextView otherSymptoms = new VerticalTextView(this);
        otherSymptoms.setWidth(dpToPx(140));
        addTextViewToTable(otherSymptoms,"   OBJAWY DODATK. " ,0,1,42,3);
        otherSymptoms.setHeight(dpToPx(40));

        TextView prolificDays = new TextView(this);
        prolificDays.setWidth(dpToPx(220));
        addTextViewToTable(prolificDays," DNI PŁODNE ",0,3,45,1);

        TextView intercourse = new TextView(this);
        intercourse.setWidth(dpToPx(220));
        addTextViewToTable(intercourse," WSPÓŁŻYCIE ",0,3,46,1);

        TextView temp01 = new TextView(this);
        temp01.setWidth(dpToPx(180));
        addTextViewToTable(temp01," 37,30 ",1,2,1,1);
        TextView temp02 = new TextView(this);
        temp02.setWidth(dpToPx(180));
        addTextViewToTable(temp02," 37,25 ",1,2,2,1);
        TextView temp03 = new TextView(this);
        temp03.setWidth(dpToPx(180));
        addTextViewToTable(temp03," 37,20 ",1,2,3,1);
        TextView temp04 = new TextView(this);
        temp04.setWidth(dpToPx(180));
        addTextViewToTable(temp04," 37,15 ",1,2,4,1);
        TextView temp05 = new TextView(this);
        temp05.setWidth(dpToPx(180));
        addTextViewToTable(temp05," 37,10 ",1,2,5,1);
        TextView temp06 = new TextView(this);
        temp06.setWidth(dpToPx(180));
        addTextViewToTable(temp06," 37,05 ",1,2,6,1);
        TextView temp07 = new TextView(this);
        temp07.setWidth(dpToPx(180));
        addTextViewToTable(temp07," 37,00 ",1,2,7,1);
        TextView temp08 = new TextView(this);
        temp08.setWidth(dpToPx(180));
        addTextViewToTable(temp08," 36,95 ",1,2,8,1);
        TextView temp09 = new TextView(this);
        temp09.setWidth(dpToPx(180));
        addTextViewToTable(temp09," 36,90 ",1,2,9,1);
        TextView temp10 = new TextView(this);
        temp10.setWidth(dpToPx(180));
        addTextViewToTable(temp10," 36,85 ",1,2,10,1);
        TextView temp11 = new TextView(this);
        temp11.setWidth(dpToPx(180));
        addTextViewToTable(temp11," 36,80 ",1,2,11,1);
        TextView temp12 = new TextView(this);
        temp12.setWidth(dpToPx(180));
        addTextViewToTable(temp12," 36,75 ",1,2,12,1);
        TextView temp13 = new TextView(this);
        temp13.setWidth(dpToPx(180));
        addTextViewToTable(temp13," 36,70 ",1,2,13,1);
        TextView temp14 = new TextView(this);
        temp14.setWidth(dpToPx(180));
        addTextViewToTable(temp14," 36,65 ",1,2,14,1);
        TextView temp15 = new TextView(this);
        temp15.setWidth(dpToPx(180));
        addTextViewToTable(temp15," 36,60 ",1,2,15,1);
        TextView temp16 = new TextView(this);
        temp16.setWidth(dpToPx(180));
        addTextViewToTable(temp16," 36,55 ",1,2,16,1);
        TextView temp17 = new TextView(this);
        temp17.setWidth(dpToPx(180));
        addTextViewToTable(temp17," 36,50 ",1,2,17,1);
        TextView temp18 = new TextView(this);
        temp18.setWidth(dpToPx(180));
        addTextViewToTable(temp18," 36,45 ",1,2,18,1);
        TextView temp19 = new TextView(this);
        temp19.setWidth(dpToPx(180));
        addTextViewToTable(temp19," 36,40 ",1,2,19,1);
        TextView temp20 = new TextView(this);
        temp20.setWidth(dpToPx(180));
        addTextViewToTable(temp20," 36,35 ",1,2,20,1);
        TextView temp21 = new TextView(this);
        temp21.setWidth(dpToPx(180));
        addTextViewToTable(temp21," 36,30 ",1,2,21,1);
        TextView temp22 = new TextView(this);
        temp22.setWidth(dpToPx(180));
        addTextViewToTable(temp22," 36,25 ",1,2,22,1);
        TextView temp23 = new TextView(this);
        temp23.setWidth(dpToPx(180));
        addTextViewToTable(temp23," 36,20 ",1,2,23,1);
        TextView temp24 = new TextView(this);
        temp24.setWidth(dpToPx(180));
        addTextViewToTable(temp24," 36,15 ",1,2,24,1);
        TextView temp25 = new TextView(this);
        temp25.setWidth(dpToPx(180));
        addTextViewToTable(temp25," 36,10 ",1,2,25,1);
        TextView temp26 = new TextView(this);
        temp26.setWidth(dpToPx(180));
        addTextViewToTable(temp26," 36,05 ",1,2,26,1);
        TextView temp27 = new TextView(this);
        temp27.setWidth(dpToPx(180));
        addTextViewToTable(temp27," 36,00 ",1,2,27,1);

        TextView wet = new TextView(this);
        wet.setWidth(dpToPx(180));
        addTextViewToTable(wet," MOKRO/ŚLISKO ",1,2,31,1);
        TextView stretchy = new TextView(this);
        stretchy.setWidth(dpToPx(180));
        addTextViewToTable(stretchy," ROZCIĄGLIWY ",1,2,32,1);
        TextView transparent = new TextView(this);
        transparent.setWidth(dpToPx(180));
        addTextViewToTable(transparent," PRZEJRZYSTY ",1,2,33,1);
        TextView humid = new TextView(this);
        humid.setWidth(dpToPx(180));
        addTextViewToTable(humid," WILGOTNO ",1,2,34,1);
        TextView sticky = new TextView(this);
        sticky.setWidth(dpToPx(180));
        addTextViewToTable(sticky," LEPKI, GĘSTY ",1,2,35,1);
        TextView muzzy = new TextView(this);
        muzzy.setWidth(dpToPx(180));
        addTextViewToTable(muzzy," MĘTNY ",1,2,36,1);
        TextView dry = new TextView(this);
        dry.setWidth(dpToPx(180));
        addTextViewToTable(dry," SUCHO ",1,2,37,1);

        TextView posDil = new TextView(this);
        posDil.setWidth(dpToPx(180));
        addTextViewToTable(posDil," POZYCJA, ROZWARCIE ",1,2,40,1);
        posDil.setHeight(dpToPx(40));
        TextView hardness = new TextView(this);
        hardness.setWidth(dpToPx(180));
        addTextViewToTable(hardness," TWARDOŚĆ ",1,2,41,1);

        TextView pain = new TextView(this);
        pain.setWidth(dpToPx(180));
        addTextViewToTable(pain," BÓL OWULACYJNY ",1,2,42,1);
        TextView tension = new TextView(this);
        tension.setWidth(dpToPx(180));
        addTextViewToTable(tension," NAPIĘCIE W PIERSIACH ",1,2,43,1);
        TextView others = new TextView(this);
        others.setWidth(dpToPx(180));
        addTextViewToTable(others," INNE ",1,2,44,1);
        others.setHeight(dpToPx(100));
    }

    public void addTextViewToTable(TextView tv, String text, int colStart, int colSize, int rowStart, int rowSize) {
        GridLayout table = (GridLayout) findViewById(R.id.table);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setHeight(dpToPx(20));
        tv.setBackgroundResource(R.drawable.cell_shape);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(colStart,colSize);
        params.rowSpec = GridLayout.spec(rowStart,rowSize);
        tv.setLayoutParams(params);
        table.addView(tv);
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
