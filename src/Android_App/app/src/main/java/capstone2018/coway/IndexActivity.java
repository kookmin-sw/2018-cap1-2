package capstone2018.coway;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import capstone.kookmin.commons.protocol.Packet;

public class IndexActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private Socket socket;
    private SocketAsync socketAsync;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Packet packet = null;
    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        setView();
        getImageBytes();

        socketAsync = new SocketAsync();
        socketAsync.execute();

    }

    private void setView() {
        progressBar = findViewById(R.id.progressBar);
    }

    private void getImageBytes() {
        Log.d(TAG, "Get ImageBytes from MainActivity");
        Intent intent = getIntent();
        imageBytes = intent.getByteArrayExtra("imageBytes");
    }


    class SocketAsync extends AsyncTask<Void, Void,Void > {

        String SocketTAG = "SocketAsync";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(SocketTAG, "onPreExecute().");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Log.d(SocketTAG,"doInBackground()");
            setSocket();

//            Log.d(TAG, "isCancelled() : " + isCancelled() + ", isConnected() : " + socket.isConnected());
            try{
                if(isCancelled() || !socket.isConnected() || imageBytes == null) {
                    throw new Exception("socket setting is not complete.");
                }
                while(!isCancelled() && socket.isConnected()) {
                    send();
                    receive();
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e1){
                e1.printStackTrace();
            }

           Log.d(SocketTAG, "socket is disconnected.");
            return null;
        }

        private void send() throws IOException {
            Log.d(SocketTAG, "Send image.");

            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(new Packet(Packet.IMAGE_SEND,imageBytes));
        }

        private void receive() throws IOException, ClassNotFoundException{
            Log.d(SocketTAG,"try to receive packet");
            input = new ObjectInputStream(socket.getInputStream());

            packet = (Packet)input.readObject();
            Log.d(SocketTAG,"received packet code : " + packet.getStatusCode());

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(SocketTAG,"onPostExecute()");
            progressBar.setVisibility(View.INVISIBLE);

            if (packet.getStatusCode() == Packet.SYSTEM_ERROR) {
                goSystemErrorActivity();
            } else if (packet.getStatusCode() == Packet.SUCCESS) {
                goSuccessResultActivity();
            } else if (packet.getStatusCode() == Packet.LOGICAL_ERROR){
               goLogicalErrorActivity();
            } else {
                Log.d(TAG,"Unsupported status code");
                goSystemErrorActivity();
            }
        }

        private void goSystemErrorActivity(){
            Log.d(TAG, "System error occur.");

            Intent failIntent = new Intent(IndexActivity.this, SystemErrorActivity.class);

            startActivity(failIntent);
            finish();
        }

        private void goSuccessResultActivity(){
            Log.d(TAG, "Success.");

            Intent successIntent = new Intent(IndexActivity.this, SuccessResultActivity.class);

            successIntent.putExtra("pseudo_txt", packet.getPseudoLines());
            successIntent.putExtra("java_txt", packet.getJavaLines());

            startActivity(successIntent);
            finish();
        }

        private void goLogicalErrorActivity(){
            Log.d(TAG,"Logical Error occur");

            Intent logicalErrorIntent = new Intent(IndexActivity.this, LogicalErrorActivity.class);

            logicalErrorIntent.putExtra("psuedo_txt",packet.getPseudoLines());
            logicalErrorIntent.putExtra("error_lines", packet.getErrorLines());

            startActivity(logicalErrorIntent);
            finish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "OnCancelled()");

            progressBar.setVisibility(View.INVISIBLE);
            goSystemErrorActivity();

        }

        private void setSocket(){
            String ip = "49.236.144.45";
            int port = 13579;

            try{
                SocketAddress socketAddress = new InetSocketAddress(ip,port);
                int timeout = 30000;
                socket = new Socket();
                socket.setSoTimeout(timeout);

                socket.connect(socketAddress,timeout);
                Log.d(SocketTAG,"socket open");

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
