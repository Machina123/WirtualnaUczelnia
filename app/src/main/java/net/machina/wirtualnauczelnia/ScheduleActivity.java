package net.machina.wirtualnauczelnia;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.machina.wirtualnauczelnia.common.Constants;
import net.machina.wirtualnauczelnia.common.DatasetFields;
import net.machina.wirtualnauczelnia.datamodel.ScheduleDataModel;
import net.machina.wirtualnauczelnia.datamodel.ScheduleListAdapter;
import net.machina.wirtualnauczelnia.network.WUDataHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    private WUDataHelper dataHelper;
    protected Button btnPrevious, btnNext;
    protected ListView listSchedule;
    protected TextView txtWeekInfo;

    protected ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        dataHelper = WUDataHelper.getInstance(getApplicationContext());

        btnPrevious = findViewById(R.id.schedule_btnPrev);
        btnNext = findViewById(R.id.schedule_btnNext);
        listSchedule = findViewById(R.id.schedule_list);
        txtWeekInfo = findViewById(R.id.schedule_txtWeek);

        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        if(savedInstanceState == null) {
            dialog = ProgressDialog.show(this, getString(R.string.label_please_wait), getString(R.string.message_downloading_data), true, false);
            dataHelper.getCurrentWeekSchedule(((isSuccessful, dataSet) -> {
                Log.d(Constants.LOGGER_TAG, "ScheduleActivity - getCurrentWeekSchedule - isSuccessful? " + isSuccessful);
                if(isSuccessful) {
                    handleSchedule(dataSet);
                } else {
                    Toast.makeText(ScheduleActivity.this, "Nie udało się pobrać planu zajęć", Toast.LENGTH_LONG).show();
                }
            }));
        }

    }

    @Override
    public void onClick(View v) {
        dialog = ProgressDialog.show(this, getString(R.string.label_please_wait), getString(R.string.message_downloading_data), true, false);
        switch(v.getId()) {
            case R.id.schedule_btnPrev:
                dataHelper.getPreviousWeekSchedule(((isSuccessful, dataSet) -> {
                    if(isSuccessful) handleSchedule(dataSet);
                }));
                break;
            case R.id.schedule_btnNext:
                dataHelper.getNextWeekSchedule(((isSuccessful, dataSet) -> {
                    if(isSuccessful) handleSchedule(dataSet);
                }));
        }
    }

    public void handleSchedule(ArrayList<HashMap<String, String>> dataSet) {

        ArrayList<ScheduleDataModel> scheduleList = new ArrayList<>();

        runOnUiThread(() -> {
            dialog.cancel();

            if(dataHelper.isPreviousWeekScheduleAvailable()) btnPrevious.setVisibility(View.VISIBLE);
            else btnPrevious.setVisibility(View.INVISIBLE);

            if(dataHelper.isNextWeekScheduleAvailable()) btnNext.setVisibility(View.VISIBLE);
            else btnNext.setVisibility(View.INVISIBLE);
        });

        for(HashMap<String, String> data : dataSet) {
            if(data.containsKey(DatasetFields.DS_SCHEDULE_CURR_WEEK)) {
                runOnUiThread(() -> {
                    txtWeekInfo.setText(data.get(DatasetFields.DS_SCHEDULE_CURR_WEEK));
                });
            } else if(data.containsKey(DatasetFields.DS_SCHEDULE_DETAILS[0])) {
                ScheduleDataModel scheduleEntry = new ScheduleDataModel(
                        data.get(DatasetFields.DS_SCHEDULE_DETAILS[0]),
                        data.get(DatasetFields.DS_SCHEDULE_DETAILS[1]),
                        data.get(DatasetFields.DS_SCHEDULE_DETAILS[2]),
                        data.get(DatasetFields.DS_SCHEDULE_DETAILS[3]),
                        data.get(DatasetFields.DS_SCHEDULE_DETAILS[4]),
                        data.get(DatasetFields.DS_SCHEDULE_DETAILS[5]),
                        data.get(DatasetFields.DS_SCHEDULE_DETAILS[6]),
                        data.get(DatasetFields.DS_SCHEDULE_DETAILS[7]),
                        data.get(DatasetFields.DS_SCHEDULE_DETAILS[8])
                );
                scheduleList.add(scheduleEntry);
            }
        }

        final ScheduleListAdapter adapter = new ScheduleListAdapter(ScheduleActivity.this, R.layout.layout_schedule_view, scheduleList);

        runOnUiThread(()-> listSchedule.setAdapter(adapter));
    }
}
