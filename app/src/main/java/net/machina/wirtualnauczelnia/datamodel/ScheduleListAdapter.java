package net.machina.wirtualnauczelnia.datamodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.machina.wirtualnauczelnia.R;

import java.util.ArrayList;

public class ScheduleListAdapter extends ArrayAdapter<ScheduleDataModel> {
    private ArrayList<ScheduleDataModel> mObjects;
    private Context mContext;

    public ScheduleListAdapter(@NonNull Context context, int resource, ArrayList<ScheduleDataModel> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mObjects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.layout_schedule_view, null);
        }

        ScheduleDataModel scheduleEntry = mObjects.get(position);
        if(scheduleEntry != null) {
            TextView txtHourStart = v.findViewById(R.id.lecture_hourInfo);
            TextView txtDate = v.findViewById(R.id.lecture_date);
            TextView txtLecture = v.findViewById(R.id.lecture_name);
            TextView txtLectureForm = v.findViewById(R.id.lecture_form);
            TextView txtLectureLocation = v.findViewById(R.id.lecture_location);
            TextView txtLecturer = v.findViewById(R.id.lecture_lecturer);

            txtHourStart.setText(scheduleEntry.getHourStart().concat(" - ").concat(scheduleEntry.getHourEnd()));
            txtDate.setText(scheduleEntry.getDate());
            txtLecture.setText(scheduleEntry.getLectureName());
            txtLectureForm.setText(scheduleEntry.getLectureForm());
            txtLectureLocation.setText(scheduleEntry.getLecturePlace());
            txtLecturer.setText(scheduleEntry.getLecturer());
        }


        return v;
    }


}
