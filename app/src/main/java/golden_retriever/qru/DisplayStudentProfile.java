package golden_retriever.qru;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class DisplayStudentProfile extends AppCompatActivity implements AsyncResponse{
    private PDFView thePDF;
    private String ID;
    private String rID;
    private String profileType;
    private JSONObject hold;
    private Button homeButton;
    private Button submitButton;
    private TextView fNameView;
    private TextView lNameView;
    private TextView classView;
    private TextView majorView;
    private TextView gpaView;
    private TextView specialView;
    private EditText recruiterNotes;
    private LinearLayout pdfLayout;

    public static final String TAG = "DisplayStudentProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_student_profile);
        JSONObject result = null;
        JSONObject result2 = null;
        profileType = getIntent().getStringExtra("profiletype");
        RestAsync rest = new RestAsync(this);
        RestAsync resumeRest = new RestAsync(this);
        final RestAsync sendMail = new RestAsync(this);
        rest.setType("GET");
        resumeRest.setType("GET");
        ID  = getIntent().getStringExtra("sID");
        if(profileType.equals("Recruiter")) {
            rID = getIntent().getStringExtra("rID");
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int total_height = displayMetrics.heightPixels;
        double height_hold = width * 1.35;
        int height = (int) height_hold;

        fNameView = findViewById(R.id.first_name);
        lNameView = findViewById(R.id.last_name);
        classView = findViewById(R.id.classification);
        majorView = findViewById(R.id.major);
        gpaView = findViewById(R.id.gpa);
        specialView = findViewById(R.id.special_notes);
        pdfLayout = findViewById(R.id.pdfLayout);
        submitButton = findViewById(R.id.submit_button);
        recruiterNotes = findViewById(R.id.recruiter_notes_input);
        if(profileType.equals("Recruiter")){
            double calc_hold = total_height * 0.2;
            int textHeight = (int) calc_hold;
            recruiterNotes.setHeight(textHeight);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent theIntent = new Intent(DisplayStudentProfile.this, RecruiterMain.class);
                    theIntent.putExtra("ID", rID);

                    sendMail.setType("EMAIL");

                    JSONObject ids = new JSONObject();
                    try {
                        ids.put("sid", ID)
                            .put("rid", rID)
                            .put("recruitNotes", recruiterNotes.getText().toString());
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }

                    sendMail.execute(ids);

                    startActivity(theIntent);
                }
            });
        } else {
            submitButton.setVisibility(View.GONE);
            recruiterNotes.setVisibility(View.GONE);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        pdfLayout.setLayoutParams(layoutParams);

        JSONObject query = new JSONObject();
        try {
            query.put("_id", ID);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        rest.execute(query);

        String resumeID = null;
        String toBeDecoded = null;
        try{
            result = rest.get();

            if(result.has("firstName")) {
                fNameView.setText(result.getString("firstName"));
            }
            if(result.has("lastName")) {
                lNameView.setText(result.getString("lastName"));
            }
            if(result.has("classification")) {
                classView.setText(result.getString("classification"));
            }
            if(result.has("major")) {
                majorView.setText(result.getString("major"));
            }
            if(result.has("gpa")) {
                gpaView.setText(result.getString("gpa"));
            }
            if(result.has("specNotes")) {
                specialView.setText(result.getString("specNotes"));
            }

            if(result.has("resumeid")) {
                resumeID = result.getString("resumeid");

                JSONObject resumeQuery = new JSONObject();
                resumeQuery.put("_id", resumeID);

                resumeRest.execute(resumeQuery);

                result2 = resumeRest.get();

                toBeDecoded = result2.getString("resumeData");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e ) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        //toBeDecoded = getIntent().getStringExtra("pdf");

        //Log.d(TAG, "Index of: " + toBeDecoded.contains("MNJSVFT0YN"));

        if(toBeDecoded != null) {
            byte[] toPDF = Base64.decode(toBeDecoded, Base64.DEFAULT);

            thePDF = (PDFView) findViewById(R.id.pdfView);
            thePDF.fromBytes(toPDF)
                    .enableSwipe(true)
                    .enableDoubletap(true)
                    .swipeHorizontal(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false)
                    .load();
            thePDF.setVisibility(View.VISIBLE);

            homeButton = (Button) findViewById(R.id.home_button);
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent theIntent = new Intent(DisplayStudentProfile.this, StudentMain.class);
                    if(profileType.equals("Recruiter"))
                        theIntent = new Intent(DisplayStudentProfile.this, RecruiterMain.class);
                    theIntent.putExtra("ID", ID);
                    startActivity(theIntent);
                }
            });
        }
    }

    @Override
    public void processFinish(JSONObject output){
        hold = output;
    }
}
