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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import capstone.kookmin.commons.protocol.Packet;

public class IndexActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private Socket socket;
    private ReceiveAsync receiveAsync;
    private ObjectInputStream input;
    private Packet packet = null;
    private int statusCode = -1;

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

            Log.d(ClientTAG,"doInBackground()");
            setSocket();

            Log.d(TAG, "isCancelled() : " + isCancelled() + ", isConnected() : " + socket.isConnected());
            while (!isCancelled() && socket.isConnected()) {
                Log.d(ClientTAG, "try to receive data");
                try {
                    int cancel;
                    Log.d(ClientTAG, "Client Thread start");

                    input = new ObjectInputStream(socket.getInputStream());

                    while((cancel = input.read()) != -1) {
                        packet = (Packet) input.readObject();
                        statusCode = packet.getStatusCode();
                    }

                    Log.d(ClientTAG,"requestCode : " + packet.getStatusCode());
                    socket.close();
                }
                catch(SocketTimeoutException e1){
                    e1.printStackTrace();
                    cancel(true);
                }catch (Exception e) {
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
            //system error
            if (statusCode == 300) {
                Log.d(TAG, "Nothing to be received");
                Intent failIntent = new Intent(IndexActivity.this, SystemErrorActivity.class);
                startActivity(failIntent);
            //success
            } else if (statusCode == 100) {
                Log.d(TAG, "Success.");
                Intent successIntent = new Intent(IndexActivity.this, SuccessResultActivity.class);
                successIntent.putExtra("pseudo_txt", packet.getPseudoLines());
                successIntent.putExtra("java_txt", packet.getJavaLines());
                startActivity(successIntent);
            //logical error
            } else if (statusCode == 200){
                Log.d(TAG,"Logical Error occur");
                Intent logicalErrorIntent = new Intent(IndexActivity.this, LogicalErrorActivity.class);
                logicalErrorIntent.putExtra("psuedo_txt",packet.getPseudoLines());
                logicalErrorIntent.putExtra("error_lines", packet.getErrorLines());
                startActivity(logicalErrorIntent);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            progressBar.setVisibility(View.INVISIBLE);

            Log.d(TAG, "Nothing to be received");
            Intent failIntent = new Intent(IndexActivity.this, SystemErrorActivity.class);
            startActivity(failIntent);

        }

        private void setSocket(){
            String ip = "49.236.144.45";
            int port = 13579;

            try{
                SocketAddress socketAddress = new InetSocketAddress(ip,port);
                int timeout = 3000;
                socket = new Socket();
                socket.setSoTimeout(timeout);

                socket.connect(socketAddress,timeout);
                Log.d(ClientTAG,"socket open");

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
