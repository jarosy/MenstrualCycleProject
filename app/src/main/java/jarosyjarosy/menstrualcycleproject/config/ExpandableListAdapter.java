package jarosyjarosy.menstrualcycleproject.config;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.*;
import android.widget.*;
import jarosyjarosy.menstrualcycleproject.R;
import jarosyjarosy.menstrualcycleproject.activities.DayFormActivity;
import jarosyjarosy.menstrualcycleproject.activities.ListActivity;
import jarosyjarosy.menstrualcycleproject.activities.MainActivity;
import jarosyjarosy.menstrualcycleproject.activities.TableActivity;
import jarosyjarosy.menstrualcycleproject.models.Cycle;
import jarosyjarosy.menstrualcycleproject.models.Day;
import jarosyjarosy.menstrualcycleproject.repository.DatabaseAdapter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ExpandableListAdapter extends BaseExpandableListAdapter implements PopupMenu.OnMenuItemClickListener {

    private Context _context;

    private DateTimeFormatter appDateFormat = DateTimeFormat.forPattern("dd.MM.yyyy");

    private List<Cycle> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Cycle, List<Day>> _listDataChild;
    private Cycle chosenCycle;

    public ExpandableListAdapter(Context context, List<Cycle> listDataHeader,
                                 HashMap<Cycle, List<Day>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {

        final Day day = (Day) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.listDay);

        txtListChild.setText(day.getDayOfCycle() + " dzień | " + day.getTemperature() + "℃");

        Button deleteDayBtn = (Button)convertView.findViewById(R.id.deleteDayButton);
        deleteDayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openDayDeleteAlertDialog(day);
            }
        });

        Button editDayBtn = (Button)convertView.findViewById(R.id.editDayButton);
        editDayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context, DayFormActivity.class);
                Bundle b = new Bundle();
                b.putLong("cycleId", day.getCycleId());
                b.putLong("dayId", day.getDayId());
                intent.putExtras(b);
                _context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        final Cycle cycle = (Cycle) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.listCycle);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText("Start: " + appDateFormat.print(cycle.getStartDate()));

        Button optionBtn = (Button)convertView.findViewById(R.id.optionButton);
        optionBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                chosenCycle = cycle;
                PopupMenu cycleMenu = new PopupMenu(_context, view);
                cycleMenu.setOnMenuItemClickListener(ExpandableListAdapter.this);
                MenuInflater inflater = cycleMenu.getMenuInflater();
                inflater.inflate(R.menu.cycle_menu, cycleMenu.getMenu());
                cycleMenu.show();
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Intent intent;
        Bundle b;
        switch (menuItem.getItemId()) {
            case R.id.previewItem:
                intent = new Intent(_context, TableActivity.class);
                b = new Bundle();
                b.putLong("cycleId", chosenCycle.getCycleId());
                intent.putExtras(b);
                _context.startActivity(intent);
                return true;

            case R.id.newDayItem:
                intent = new Intent(_context, DayFormActivity.class);
                b = new Bundle();
                b.putLong("cycleId", chosenCycle.getCycleId());
                intent.putExtras(b);
               _context.startActivity(intent);
               return true;

            case R.id.exportItem:
                exportCycleToXLS();
                return true;

            case R.id.deleteItem:
                openCycleDeleteAlertDialog();

                return true;

            default:
                break;
        }
        return false;
    }

    public void openCycleDeleteAlertDialog() {
        AlertDialog.Builder cycleAlertBuild = new AlertDialog.Builder(_context);
        cycleAlertBuild.setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseAdapter dbAdapter = new DatabaseAdapter(_context);
                        dbAdapter.open();
                        dbAdapter.deleteCycle(chosenCycle.getCycleId());
                        dbAdapter.close();
                        try {
                            writeToSD();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ((ListActivity)_context).refreshActivity();

                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertCycle = cycleAlertBuild.create();
        alertCycle.setTitle("Na pewno chcesz usunąć cykl?");
        alertCycle.show();
    }

    public void openDayDeleteAlertDialog(final Day day) {
        AlertDialog.Builder cycleAlertBuild = new AlertDialog.Builder(_context);
        cycleAlertBuild.setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseAdapter dbAdapter = new DatabaseAdapter(_context);
                        dbAdapter.open();
                        dbAdapter.deleteDay(day.getDayId());
                        dbAdapter.close();
                        try {
                            writeToSD();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ((ListActivity)_context).refreshActivity();
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertCycle = cycleAlertBuild.create();
        alertCycle.setTitle("Na pewno chcesz usunąć dzień?");
        alertCycle.show();
    }

    private void exportCycleToXLS() {
        DateTimeFormatter monthAndYearFormat = DateTimeFormat.forPattern("MM.yyyy");

        DatabaseAdapter dbAdapter = new DatabaseAdapter(_context);
        dbAdapter.open();
        List<Day> days = dbAdapter.getAllDaysFromCycle(chosenCycle.getCycleId());

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Cykl - " + appDateFormat.print(chosenCycle.getStartDate()));
        Row dateRow = sheet.createRow(0);
        sheet.addMergedRegion(CellRangeAddress.valueOf("A1:B1"));
        dateRow.createCell(0).setCellValue("Data: " + monthAndYearFormat.print(chosenCycle.getStartDate()));

        for (Day day : days) {
            dateRow.createCell(day.getDayOfCycle() + 1).setCellValue(day.getCreateDate().getDayOfMonth());
        }

        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            File xlsFile = new File(path + "/Cykl-" + appDateFormat.print(chosenCycle.getStartDate()) + ".xls");
            FileOutputStream out = new FileOutputStream(xlsFile);
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(_context, "Wyeksportowano cykl do pliku", Toast.LENGTH_LONG).show();
    }

    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        String backupDBPath = "menstrualcyclebackup.db";
        File currentDB = new File(_context.getDatabasePath("menstrualcycle.db").toString());
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