package golden_retriever.qru;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateStudentProfile extends AppCompatActivity implements AsyncResponse{
    private EditText firstName_edittext;
    private EditText lastName_edittext;
    private EditText gpa_edittext;
    private Spinner major_spinner;
    private Spinner classification_spinner;
    private EditText special_notes_edittext;
    private Button update_profile_btn;

    private String ID;
    private JSONObject hold;

    public static final String TAG = "UpdateStudentProfile";
    // maybe add a picture

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_profile);

        final RestAsync rest = new RestAsync(this);
        rest.setType("UPDATE");

        ID = getIntent().getStringExtra("ID");
        rest.setId(ID);

        firstName_edittext = (EditText) findViewById(R.id.student_update_profile_firstname);

        lastName_edittext = (EditText) findViewById(R.id.student_update_profile_lastname);

        gpa_edittext = (EditText) findViewById(R.id.student_update_profile_GPA);

        major_spinner = (Spinner) findViewById(R.id.student_update_profile_major);

        major_spinner.setPrompt("Select your major");
        List<String> majors = new ArrayList<String>();
        majors.add("Aerospace Engineering");
        majors.add("Biomedical Engineering");
        majors.add("Chemical Engineering");
        majors.add("Civil Engineering");
        majors.add("Electrical and Computer Engineering");
        majors.add("Mechanical Engineering");
        majors.add("Other");
        ArrayAdapter<String> madapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, majors);
        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        major_spinner.setAdapter(madapter);


        classification_spinner = (Spinner) findViewById(R.id.student_update_profile_classification);

        classification_spinner.setPrompt("Select your classification");
        List<String> classifications = new ArrayList<String>();
        classifications.add("Freshman");
        classifications.add("Sophomore");
        classifications.add("Junior");
        classifications.add("Senior");
        classifications.add("Alum");
        classifications.add("Master's");
        classifications.add("Doctorate");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, classifications);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classification_spinner.setAdapter(adapter);

        special_notes_edittext = (EditText) findViewById(R.id.student_update_profile_special_notes);

        update_profile_btn = (Button) findViewById(R.id.submit_student_profile_btn);

        update_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // delete old student from database, add new one
                String new_firstName = firstName_edittext.getText().toString();
                String new_lastName = lastName_edittext.getText().toString();
                String new_GPA = gpa_edittext.getText().toString();
                String classification = classification_spinner.getSelectedItem().toString();
                String major = major_spinner.getSelectedItem().toString();
                String special_notes = special_notes_edittext.getText().toString();

                JSONObject updateUser = new JSONObject();
                try {
                    updateUser.put("firstName", new_firstName)
                            .put("lastName", new_lastName)
                            .put("gpa", new_GPA)
                            .put("classification", classification)
                            .put("major", major)
                            .put("specNotes", special_notes);
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "Type is: " + rest.getType());
                rest.execute(updateUser);

                Candidate_Profile new_candidate = new Candidate_Profile("0", new_firstName, new_lastName,
                        "dummyemail", "dummypass", new_GPA, classification, major, special_notes);

                Intent myIntent = new Intent(UpdateStudentProfile.this, StudentMain.class);
                myIntent.putExtra("profiletype", "Student");
                myIntent.putExtra("ID", ID);
                UpdateStudentProfile.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public void processFinish(JSONObject output){
        hold = output;
    }

}
