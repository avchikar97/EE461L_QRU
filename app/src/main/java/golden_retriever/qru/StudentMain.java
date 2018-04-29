package golden_retriever.qru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.PendingIntent.getActivity;

public class StudentMain extends AppCompatActivity {
    private Button updateProfileButton;
    private Button newScanButton;
    private Button viewProfileButton;
    private Button getStudentQRCodeButton;
    private Button uploadPDFButton;
    private Button LogOffButton;
    private TextView welcomeMessage;

    private String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        ID = getIntent().getStringExtra("ID");

        updateProfileButton = (Button) findViewById(R.id.submit_student_profile_btn);
        updateProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(StudentMain.this, UpdateStudentProfile.class);
                myIntent.putExtra("profiletype", "Student");
                myIntent.putExtra("ID", ID);
                StudentMain.this.startActivity(myIntent);
            }
        });

        newScanButton = (Button) findViewById(R.id.student_scan_btn);
        newScanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(StudentMain.this, QRReaderActivity.class);
                myIntent.putExtra("profiletype", "Student");
                myIntent.putExtra("ID", ID);
                StudentMain.this.startActivity(myIntent);
            }
        });

        viewProfileButton = (Button) findViewById(R.id.view_profile_btn);
        viewProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(StudentMain.this, DisplayStudentProfile.class);
                myIntent.putExtra("profiletype", "Student");
                myIntent.putExtra("ID", ID);
                StudentMain.this.startActivity(myIntent);
            }
        });


        getStudentQRCodeButton = (Button) findViewById(R.id.generate_student_QR_btn);
        getStudentQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(StudentMain.this, QRGeneratorActivity.class);
                myIntent.putExtra("profiletype", "Student");
                myIntent.putExtra("ID", ID);
                StudentMain.this.startActivity(myIntent);
            }
        });

        uploadPDFButton = (Button) findViewById(R.id.upload_resume_btn);
        uploadPDFButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(StudentMain.this, UploadResume.class);
                myIntent.putExtra("profiletype", "Student");
                myIntent.putExtra("ID", ID);
                StudentMain.this.startActivity(myIntent);
            }
        });

        LogOffButton = (Button) findViewById(R.id.log_off_student_button);
        LogOffButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(StudentMain.this, LoginActivity.class);
                StudentMain.this.startActivity(myIntent);
            }
        });

    }

}
