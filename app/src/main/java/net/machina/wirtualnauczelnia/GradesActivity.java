package net.machina.wirtualnauczelnia;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import net.machina.wirtualnauczelnia.common.Constants;
import net.machina.wirtualnauczelnia.common.DatasetFields;
import net.machina.wirtualnauczelnia.datamodel.GradeDataModel;
import net.machina.wirtualnauczelnia.datamodel.GradeListAdapter;
import net.machina.wirtualnauczelnia.network.WUDataHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class GradesActivity extends AppCompatActivity implements View.OnClickListener {

    protected WUDataHelper dataHelper;
    protected Button btnPrevious, btnNext;
    protected TextView txtRow1, txtRow2;
    protected ListView listGrades;

    protected ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        dataHelper = WUDataHelper.getInstance(getApplicationContext());
        btnPrevious = (Button) findViewById(R.id.grades_btnPrevious);
        btnNext = (Button) findViewById(R.id.grades_btnNext);

        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        txtRow1 = (TextView) findViewById(R.id.grades_txtRow1);
        txtRow2 = (TextView) findViewById(R.id.grades_txtRow2);

        listGrades = (ListView) findViewById(R.id.grades_listGrades);
        if(savedInstanceState == null) {
            dialog = ProgressDialog.show(this, getString(R.string.label_please_wait), getString(R.string.message_downloading_data), true, false);
            dataHelper.getCurrentTermGrades(((isSuccessful, dataSet) -> {
                Log.d(Constants.LOGGER_TAG, "MainActivity - getCurrentTermGrades - isSuccessful? " + isSuccessful);
                if(isSuccessful) {
                    handleGrades(dataSet);
                }
            }));
        }
    }

    @Override
    public void onClick(View v) {
        dialog = ProgressDialog.show(this, getString(R.string.label_please_wait), getString(R.string.message_downloading_data), true, false);
        switch(v.getId()) {
            case R.id.grades_btnPrevious:
                dataHelper.getPreviousTermGrades(((isSuccessful, dataSet) -> {
                    if(isSuccessful) handleGrades(dataSet);
                }));
                break;
            case R.id.grades_btnNext:
                dataHelper.getNextTermGrades(((isSuccessful, dataSet) -> {
                    if(isSuccessful) handleGrades(dataSet);
                }));
        }
    }

    public void handleGrades(ArrayList<HashMap<String, String>> dataSet) {

        ArrayList<GradeDataModel> gradeList = new ArrayList<>();

        runOnUiThread(() -> {
            dialog.cancel();

            if(dataHelper.isPreviousTermGradesAvailable()) btnPrevious.setVisibility(View.VISIBLE);
            else btnPrevious.setVisibility(View.INVISIBLE);

            if(dataHelper.isNextTermGradesAvailable()) btnNext.setVisibility(View.VISIBLE);
            else btnNext.setVisibility(View.INVISIBLE);
        });

        for(HashMap<String, String> data : dataSet) {
            if(data.containsKey(DatasetFields.DS_TERMINFO_YEAR)) {
                runOnUiThread(() -> {
                    txtRow1.setText(data.get(DatasetFields.DS_TERMINFO_TERM));
                    txtRow2.setText(data.get(DatasetFields.DS_TERMINFO_YEAR));
                });
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
                gradeList.add(grade);
            }
        }

        final GradeListAdapter adapter = new GradeListAdapter(GradesActivity.this, R.layout.layout_grade_view, gradeList);

        runOnUiThread(()-> listGrades.setAdapter(adapter));
    }
}
