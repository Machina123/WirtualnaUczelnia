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

public class GradeListAdapter extends ArrayAdapter<GradeDataModel> {

    private ArrayList<GradeDataModel> mObjects;
    private Context mContext;

    public GradeListAdapter(@NonNull Context context, int resource, ArrayList<GradeDataModel> objects) {
        super(context, resource, objects);
        this.mObjects = objects;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;


        if(v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.layout_grade_view, null);
        }

        GradeDataModel grade = mObjects.get(position);
        if(grade != null) {
            TextView txtCourseName = (TextView) v.findViewById(R.id.grade_courseName);
            TextView txtCourseForm = (TextView) v.findViewById(R.id.grade_courseForm);
            TextView txtLecturer = (TextView) v.findViewById(R.id.grade_lecturer);
            TextView txtAdditionalInfo = (TextView) v.findViewById(R.id.grade_additionalInfo);
            TextView txtFirstTermGrade = (TextView) v.findViewById(R.id.grade_firstTerm);
            TextView txtSecondTermGrade = (TextView) v.findViewById(R.id.grade_secondTerm);
            TextView txtThirdTermGrade = (TextView) v.findViewById(R.id.grade_thirdTerm);
            if(txtCourseName != null) txtCourseName.setText(grade.getCourseName());
            if(txtCourseForm != null) txtCourseForm.setText(grade.getCourseForm());
            if(txtLecturer != null) txtLecturer.setText(grade.getLecturer());
            if(txtAdditionalInfo != null) txtAdditionalInfo.setText(String.format(mContext.getString(R.string.grade_display_additional_info), grade.getHoursCount(), grade.getEctsCount()));
            if(txtFirstTermGrade != null) txtFirstTermGrade.setText(grade.getFirstTermGrade().length() < 1 ? "---" : grade.getFirstTermGrade());
            if(txtSecondTermGrade != null) txtSecondTermGrade.setText(grade.getSecondTermGrade().length() < 1 ? "---" : grade.getSecondTermGrade());
            if(txtThirdTermGrade != null) txtThirdTermGrade.setText(grade.getThirdTermGrade().length() < 1 ? "---" : grade.getThirdTermGrade());
        }

        return v;
    }
}
