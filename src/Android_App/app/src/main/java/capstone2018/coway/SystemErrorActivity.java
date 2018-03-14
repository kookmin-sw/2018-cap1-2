package capstone2018.coway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SystemErrorActivity extends AppCompatActivity {

    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_error);

        setView();
    }

    private void setView(){
        TextView errorTxt = findViewById(R.id.systemErrTxt);
        TextView errorComment = findViewById(R.id.systemErrComment);
        ImageView errorImage = findViewById(R.id.errorImage);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SystemErrorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
