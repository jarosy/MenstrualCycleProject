package jarosyjarosy.menstrualcycleproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class Pop extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.4));
    }

    public void onYesClick(View view) {
        Intent intent = new Intent(Pop.this, ListActivity.class);
        startActivity(intent);
    }

    public void onNoClick(View view) {
        Intent intent = new Intent(Pop.this, MainActivity.class);
        startActivity(intent);
    }

}