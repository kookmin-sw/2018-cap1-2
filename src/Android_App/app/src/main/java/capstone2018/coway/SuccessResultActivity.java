package capstone2018.coway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class SuccessResultActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private TextView javaTxt;
    private TextView pseudoTxt;
    private Button backBtn;
    private Button okBtn;
    private String [] javaLines;
    private String [] pseudoLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_result);

        setView();
        getIntentExtras();
        setTextView();
    }

    private void setView(){
        javaTxt     = findViewById(R.id.javaTxt);
        pseudoTxt   = findViewById(R.id.pseudoTxt);
        backBtn     = findViewById(R.id.backBtn);
        okBtn       = findViewById(R.id.okBtn);

        backBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
    }

    private void getIntentExtras(){
        Intent intent    = getIntent();
        javaLines       = intent.getStringArrayExtra("java_txt");
        pseudoLines     = intent.getStringArrayExtra("pseudo_txt");

        Log.d(TAG,"javaLines : " + Arrays.toString(javaLines));
        Log.d(TAG,"pseudoLines : " + Arrays.toString(pseudoLines));
    }

    private void setTextView(){
        setTextView(pseudoTxt, pseudoLines);
        setTextView(javaTxt,javaLines);
    }

    private void setTextView(TextView textView, String [] strings){
        for (int i =0; i < strings.length; i++){
            textView.append(strings[i]);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()       == R.id.backBtn){
            Intent backIntent = new Intent(this,MainActivity.class);
            startActivity(backIntent);

            finish();
        }
        else if(view.getId()  == R.id.okBtn){
//            Intent okIntent = new Intent(this);
//            startActivity(okIntent);
        }
    }

}
