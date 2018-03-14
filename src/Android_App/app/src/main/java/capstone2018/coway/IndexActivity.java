package capstone2018.coway;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeFormatException;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.Socket;

public class IndexActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private Socket socket;
    private ReceiveAsync receiveAsync;
    private DataInputStream input;
    private String data = null;
    private boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        setView();

        receiveAsync = new ReceiveAsync();
        receiveAsync.execute();
//        if ( receiveAsync.getStatus() != AsyncTask.Status.RUNNING ) {
//            Log.d(TAG, "AsycnTask is stopped");
//        }
    }

    private void setView(){
        progressBar = findViewById(R.id.progressBar);
        TextView loadingTxt = findViewById(R.id.loadingTxt);
    }

    class ReceiveAsync extends AsyncTask<Void, Void,Void > {

        String ClientTAG = "ReceiveAsync";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Log.d(ClientTAG, "onPreExecute().");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int cancel;

            Log.d(ClientTAG,"doInBackground()");
            setSocket();

            while (isCancelled() == false && socket.isConnected()) {
                Log.d(ClientTAG, "try to receive data");
                try {
                    socket.setSoTimeout(2000);
                    InputStream in = socket.getInputStream();
                    while ((cancel = in.read()) != -1) {
                        Log.d(ClientTAG, "Client Thread start");
                        input = new DataInputStream(socket.getInputStream());
                        data = input.readUTF();
                    }
                    if (cancel == -1) {
                        Log.d(ClientTAG, "I/O Interrupt.");
                    }
                    Log.d(ClientTAG, "Input data : " + data);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            Log.d(ClientTAG, "socket is disconnected.");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.INVISIBLE);
            if (TextUtils.isEmpty(data)) {
                Log.d(TAG, "Nothing to be received");
                Intent failIntent = new Intent(IndexActivity.this, SystemErrorActivity.class);
                startActivity(failIntent);
            } else {
                Intent successIntent = new Intent(IndexActivity.this, SuccessResultActivity.class);
                successIntent.putExtra("received_Data", data);
                startActivity(successIntent);
            }
        }

        private void setSocket(){
            String ip = "49.236.144.45";
            int port = 13579;

            try{
                socket = new Socket(ip, port);
                connected = socket.isConnected();
                Log.d(ClientTAG,"socket open");

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
