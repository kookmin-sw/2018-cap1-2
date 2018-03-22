package capstone2018.coway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SuccessResultActivity extends AppCompatActivity {

    private EditText javaTxt;
    private EditText pseudoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_result);

        setView();
        showContents();
    }

    private void setView(){
        javaTxt = findViewById(R.id.javaTxt);
        pseudoTxt = findViewById(R.id.pseudoTxt);
    }

    private void showContents(){
        javaTxt.setText("Google is your friend.", TextView.BufferType.EDITABLE);
    }
}
