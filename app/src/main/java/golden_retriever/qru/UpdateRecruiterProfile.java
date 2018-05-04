package golden_retriever.qru;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UpdateRecruiterProfile extends AppCompatActivity implements AsyncResponse {
    private static final String[] mongoFields = {"firstName", "lastName", "company", "email", "about", "position"};
    private static final String[] fieldDefaults =
            {"First Name",
            "Last Name",
            "Company Name",
            "Email",
            "About your company",
            "Positions"};
    private static final int NUM_FIELDS = 6;
    private static EditText[] fields = new EditText[NUM_FIELDS];

    private String[] fieldResults = new String[6];
    private EditText profile_firstname;
    private EditText profile_lastname;
    private EditText profile_company;
    private EditText email;
    private EditText profile_company_about;
    private EditText profile_positions;
    private Button submit_profile_btn;
    private Button cancel_btn;

    private String ID;
    private JSONObject hold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recruiter_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ID = getIntent().getStringExtra("ID");
        final RestAsync rest = new RestAsync(this);
        RestAsync rest2 = new RestAsync(this);
        rest2.setType("GET");
        JSONObject idCheck = new JSONObject();
        JSONObject result = new JSONObject();
        try{
            idCheck.put("_id", ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rest2.execute(idCheck);
        try{
            result = rest2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        rest.setId(ID);
        rest.setType("UPDATE");

        profile_firstname = (EditText) findViewById(R.id.recruiter_update_profile_firstname);
        fields[0] = profile_firstname;
        profile_lastname = (EditText) findViewById(R.id.recruiter_update_profile_lastname);
        fields[1] = profile_lastname;
        profile_company = (EditText) findViewById(R.id.recruiter_update_profile_company);
        fields[2] = profile_company;
        email = (EditText) findViewById(R.id.recruiter_update_email);
        fields[3] = email;
        profile_company_about = (EditText) findViewById(R.id.recruiter_update_profile_company_about);
        fields[4] = profile_company_about;
        profile_positions = (EditText) findViewById(R.id.recruiter_update_profile_positions);
        fields[5] = profile_positions;
        submit_profile_btn = (Button) findViewById(R.id.submit_recruiter_profile_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);

        for(int i = 0; i < NUM_FIELDS; i++){
            String label = mongoFields[i];
            if(result.has(label)){
                String current_result = "";
                try {
                    current_result = result.getString(label);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fields[i].setHint(current_result);
            }
            else
                fields[i].setHint(fieldDefaults[i]);
        }

        submit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete old student from database, add new one
                fieldResults[0] = profile_firstname.getText().toString();
                fieldResults[1] = profile_lastname.getText().toString();
                fieldResults[2] = profile_company.getText().toString();
                fieldResults[3] = email.getText().toString();
                fieldResults[4] = profile_company_about.getText().toString();
                fieldResults[5] = profile_positions.getText().toString();

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

                rest.execute(updateUser);

                Recruiter_Profile new_recruiter = new Recruiter_Profile("0", "dummySalt", "dummyPass",
                        fieldResults[0], fieldResults[1], "dummyEmail", fieldResults[2], fieldResults[4], fieldResults[5]);

                Intent myIntent = new Intent(UpdateRecruiterProfile.this, RecruiterMain.class);
                myIntent.putExtra("profiletype", "Recruiter");
                myIntent.putExtra("ID", ID);
                UpdateRecruiterProfile.this.startActivity(myIntent);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(UpdateRecruiterProfile.this, RecruiterMain.class);
                myIntent.putExtra("profiletype", "Recruiter");
                myIntent.putExtra("ID", ID);
                UpdateRecruiterProfile.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public void processFinish(JSONObject output){
        hold = output;
    }

}
