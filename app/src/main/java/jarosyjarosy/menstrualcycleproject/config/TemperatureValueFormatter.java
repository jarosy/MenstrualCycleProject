package jarosyjarosy.menstrualcycleproject.config;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class TemperatureValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public TemperatureValueFormatter() {
        mFormat = new DecimalFormat("#0.00");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format(value);
    }
}