package golden_retriever.qru;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class DisplayStudentProfile extends AppCompatActivity implements AsyncResponse{
    PDFView thePDF;
    String ID;
    JSONObject hold;
    Button homeButton;
    TextView fNameView;
    TextView lNameView;
    TextView classView;
    TextView majorView;
    TextView gpaView;
    TextView specialView;

    public static final String TAG = "DisplayStudentProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_student_profile);
        JSONObject result = null;
        JSONObject result2 = null;
        RestAsync rest = new RestAsync(this);
        RestAsync resumeRest = new RestAsync(this);
        rest.setType("GET");
        resumeRest.setType("GET");
        ID  = getIntent().getStringExtra("ID");

        fNameView = findViewById(R.id.first_name);
        lNameView = findViewById(R.id.last_name);
        classView = findViewById(R.id.classification);
        majorView = findViewById(R.id.major);
        gpaView = findViewById(R.id.gpa);
        specialView = findViewById(R.id.special_notes);

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
                    .defaultPage(1)
                    .enableAnnotationRendering(false)
                    .load();
            thePDF.setVisibility(View.VISIBLE);

            homeButton = (Button) findViewById(R.id.home_button);
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent theIntent = new Intent(DisplayStudentProfile.this, StudentMain.class);
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
