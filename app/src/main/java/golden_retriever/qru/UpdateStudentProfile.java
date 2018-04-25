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
import java.util.concurrent.ExecutionException;

public class UpdateStudentProfile extends AppCompatActivity implements AsyncResponse{
    private static final String[] mongoFields = {"firstName", "lastName", "gpa", "classification", "major", "specNotes"};
    private static final int NUM_FIELDS = 6;

    private String[] fieldResults = new String[NUM_FIELDS];
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

        JSONObject result = new JSONObject();
        final RestAsync rest = new RestAsync(this);
        final RestAsync rest2 = new RestAsync(this);
        rest2.setType("GET");

        ID = getIntent().getStringExtra("ID");
        rest.setId(ID);

        JSONObject idCheck = new JSONObject();
        try {
            idCheck.put("_id", ID);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        rest2.execute(idCheck);

        try{
            result = rest2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e ) {
            e.printStackTrace();
        }

        rest.setType("UPDATE");

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

        try {
            if (result.has("major")) {
                String checkMajor = result.getString("major");
                int index = majors.indexOf(checkMajor);
                major_spinner.setSelection(index);
            } else {
                major_spinner.setSelection(0);
            }

            if (result.has("classification")) {
                String checkClass = result.getString("classification");
                int index1 = classifications.indexOf(checkClass);
                classification_spinner.setSelection(index1);
            } else {
                classification_spinner.setSelection(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        special_notes_edittext = (EditText) findViewById(R.id.student_update_profile_special_notes);

        update_profile_btn = (Button) findViewById(R.id.submit_student_profile_btn);

        update_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // delete old student from database, add new one
                fieldResults[0] = firstName_edittext.getText().toString();
                fieldResults[1] = lastName_edittext.getText().toString();
                fieldResults[2] = gpa_edittext.getText().toString();
                fieldResults[3] = classification_spinner.getSelectedItem().toString();
                fieldResults[4] = major_spinner.getSelectedItem().toString();
                fieldResults[5] = special_notes_edittext.getText().toString();

                JSONObject updateUser = new JSONObject();
                try {
                    for(int i  = 0; i < NUM_FIELDS; i++) {
                        if(!fieldResults[i].equals("")) {
                            updateUser.put(mongoFields[i], fieldResults[i]);
                        }
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "Type is: " + rest.getType());
                rest.execute(updateUser);

                Candidate_Profile new_candidate = new Candidate_Profile("0", fieldResults[0], fieldResults[1],
                        "dummyemail", "dummypass", fieldResults[2], fieldResults[3], fieldResults[4], fieldResults[5]);

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
