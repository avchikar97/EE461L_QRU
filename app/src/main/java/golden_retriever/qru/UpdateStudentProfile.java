package golden_retriever.qru;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class UpdateStudentProfile extends AppCompatActivity {
    private EditText firstName_edittext;
    private EditText lastName_edittext;
    private EditText gpa_edittext;
    private Spinner major_spinner;
    private Spinner classification_spinner;
    private EditText work_experience_edittext;
    // maybe add a picture

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_profile);

        firstName_edittext = (EditText) findViewById(R.id.student_update_profile_firstname);

        lastName_edittext = (EditText) findViewById(R.id.student_update_profile_lastname);

        gpa_edittext = (EditText) findViewById(R.id.student_update_profile_GPA);

        major_spinner = (Spinner) findViewById(R.id.student_update_profile_major);

        classification_spinner = (Spinner) findViewById(R.id.student_update_profile_classification);

        work_experience_edittext

    }

}
