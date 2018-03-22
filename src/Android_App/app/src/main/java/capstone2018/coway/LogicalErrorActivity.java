package capstone2018.coway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

public class LogicalErrorActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private Button backBtn;
    private TextView pseudoTxt;
    private String [] pseudoLines;
    private int [] errorLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logical_error);

        setView();
        getIntentExtras();
        setTextView();
    }

    private void setView(){
        pseudoTxt   = findViewById(R.id.pseudoTxt);
        backBtn     = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getIntentExtras(){
        Intent intent = getIntent();
        pseudoLines = intent.getStringArrayExtra("psuedo_txt");
        errorLines = intent.getIntArrayExtra("error_lines");

        Log.d(TAG, "pseudoLines : " + Arrays.toString(pseudoLines));
        Log.d(TAG,"errorLines : " + Arrays.toString(errorLines));
    }

    private void setTextView(){
        for(int i =0; i < pseudoLines.length; i++) {
            pseudoTxt.append(pseudoLines[i].replaceAll("\\,","") + "\n");
        }
    }
}
