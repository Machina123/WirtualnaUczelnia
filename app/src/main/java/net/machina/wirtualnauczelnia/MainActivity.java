package net.machina.wirtualnauczelnia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import net.machina.wirtualnauczelnia.common.Constants;
import net.machina.wirtualnauczelnia.common.DatasetFields;
import net.machina.wirtualnauczelnia.common.PreferencesFields;
import net.machina.wirtualnauczelnia.datamodel.GradeDataModel;
import net.machina.wirtualnauczelnia.network.WUDataHelper;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    protected TextView txtLoggedIn;
    protected WUDataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLoggedIn = (TextView) findViewById(R.id.main_txtLoggedIn);
        dataHelper = WUDataHelper.getInstance(getApplicationContext());
        if(savedInstanceState == null) {
            dataHelper.getLoggedInUser(((isSuccessful, data) -> {
                if (!isSuccessful) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.label_warning)
                            .setIcon(R.drawable.ic_warning_black)
                            .setMessage(R.string.error_not_logged_in)
                            .setPositiveButton("OK", ((dialog, which) -> {
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                MainActivity.this.startActivity(intent);
                                MainActivity.this.finish();
                            }))
                            .setCancelable(false)
                            .show();
                }
                runOnUiThread(() -> MainActivity.this.txtLoggedIn.setText(data));
            }));
        } else {
            txtLoggedIn.setText(savedInstanceState.getString(PreferencesFields.SHARED_PREFS_KEY_USERNAME));
        }

        dataHelper.getCurrentTermGrades(((isSuccessful, dataSet) -> {
            Log.d(Constants.LOGGER_TAG, "MainActivity - getCurrentTermGrades - isSuccessful? " + isSuccessful);
            if(isSuccessful) {
                for(HashMap<String, String> data : dataSet) {
                    if(data.containsKey(DatasetFields.DS_TERMINFO_YEAR)) {
                        Log.d(Constants.LOGGER_TAG, data.get(DatasetFields.DS_TERMINFO_YEAR));
                        Log.d(Constants.LOGGER_TAG, data.get(DatasetFields.DS_TERMINFO_TERM));
                    } else if(data.containsKey(DatasetFields.DS_GRADE_DETAILS[0])) {
                        GradeDataModel grade = new GradeDataModel(
                                data.get(DatasetFields.DS_GRADE_DETAILS[0]),
                                data.get(DatasetFields.DS_GRADE_DETAILS[1]),
                                data.get(DatasetFields.DS_GRADE_DETAILS[2]),
                                data.get(DatasetFields.DS_GRADE_DETAILS[3]),
                                data.get(DatasetFields.DS_GRADE_DETAILS[4]),
                                data.get(DatasetFields.DS_GRADE_DETAILS[5]),
                                data.get(DatasetFields.DS_GRADE_DETAILS[6]),
                                data.get(DatasetFields.DS_GRADE_DETAILS[7])
                        );
                        Log.d(Constants.LOGGER_TAG, grade.toString());
                    }
                }
            }
        }));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PreferencesFields.SHARED_PREFS_KEY_USERNAME, txtLoggedIn.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null) txtLoggedIn.setText(savedInstanceState.getString(PreferencesFields.SHARED_PREFS_KEY_USERNAME));
        super.onRestoreInstanceState(savedInstanceState);
    }
}
