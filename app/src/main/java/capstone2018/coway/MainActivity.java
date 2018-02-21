package capstone2018.coway;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private Button photoBtn = null;
    private Button galleryBtn = null;
    private Button sendBtn = null;
    private ImageView photoView = null;
    private Uri photoURI = null;
    private byte [] imageByte = null;
    private String imagePath = "";
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                                      Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                      Manifest.permission.CAMERA,
                                      Manifest.permission.INTERNET};
    private static final int MULTIPLE_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        setView();
    }

    /**
     * 레이아웃 초기화
     */
    private void setView(){
        photoBtn = (Button)findViewById(R.id.photoBtn);
        galleryBtn = (Button)findViewById(R.id.galleryBtn);
        sendBtn = (Button)findViewById(R.id.send);
        photoView = (ImageView)findViewById(R.id.photoView);

        photoBtn.setOnClickListener(this);
        galleryBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.photoBtn){
            takePhoto();
        }
        else if(view.getId() == R.id.galleryBtn){
            choosePhoto();
        }
        else if(view.getId() == R.id.send){
            sendPhoto();
        }
    }

    /**
     *  촬영버튼 누르면 사진촬영 & 메인 페이지에 보여주기
     */
    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        /**
         * 6.0 이상 부터는 이런식으로 FileProvider를 사용해야 함
         */
        photoURI = FileProvider.getUriForFile(MainActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                makePictureFile());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, 1);
    }

    /**
     * 찍은 사진을 {@code File} 형식으로 리턴
     * @return 촬영한 사진
     */
    private File makePictureFile(){
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "COWAY_" + timestamp;
        File pictureStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Coway/");

        pictureStorage.mkdirs();
        File file = null;

        try{
            file = File.createTempFile(fileName, ".jpg", pictureStorage);
            System.out.println(file);
            imagePath = file.getAbsolutePath();

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return file;
    }

    /**
     *갤러리버튼 누르면 갤러리에서 사진 선택
     * (crop 기능 미작동으로 추후 requestCode 수정 필요)
     */
    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 2);
        Log.d(TAG," select photo in the gallery");
    }

    /**
     * thread 생성
     */
    private void sendPhoto(){
        ClientThread thread = new ClientThread();
        thread.start();
        Log.d(TAG, "Client Thread Start.");
    }

    /**
     * 이미지 범위 조정 - 수정필요
     */
    private void cropImage(){
        Log.d("MainActivity","photoURI : " + photoURI);
        this.grantUriPermission("com.android.camera", photoURI,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoURI, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, photoURI,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            croppedFileName = makePictureFile();

            File folder = new File(Environment.getExternalStorageDirectory() + "/test/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoURI = FileProvider.getUriForFile(MainActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    makePictureFile());

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            grantUriPermission(res.activityInfo.packageName, photoURI,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, 3);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG,"onActivityResult - requestCode : " +  requestCode + " , data : " + data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        //카메라에서 촬영
        if (requestCode == 1) {
            cropImage();

            // 찍힌 사진을 "갤러리" 앱에 추가
            Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );

            File f = new File( imagePath );
            Uri contentUri = Uri.fromFile( f );
            mediaScanIntent.setData( contentUri );
            this.sendBroadcast( mediaScanIntent );

        }
        // 갤러리에서 불러오기
        else if (requestCode == 2){
            photoURI = data.getData();
            imagePath = getPath(photoURI);
            Log.d(TAG,"after choosing the photo in the gallery , imagePath : " + imagePath);
            cropImage();
        }
        //메인 화면 사진 변경
        else if (requestCode == 3){
            Log.d(TAG,"after crop, imagePath : " + imagePath);
            if(imagePath == "") {
                photoURI = data.getData();
                imagePath = getPath(photoURI);
            }
            Log.d(TAG, "imagePath check : imagePath = " + imagePath);
            BitmapFactory.Options factory = new BitmapFactory.Options();
            factory.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, factory);
            imageByte = getBytesFromBitmap(bitmap);
            photoView.setImageBitmap(bitmap);
        }
    }

    /**
     * 갤러리에서 불러오는 이미지 주소 얻어오기
     * @param uri
     * @return  실제 이미지 주소
     */
    private String getPath(Uri uri){
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;

    }

    /**
     * Camera & 외부저장소 쓰기/읽기 권한 요청
     */
    private void checkPermission(){
        int permissionResult;
        List<String> permissionList =new ArrayList<>();
        for(String pm : permissions){
            Log.d(TAG, "permission check : " + pm);
            permissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),pm);

            //해당 권한을 가지고 있지 않은 경우 리스트에 추가
            if(permissionResult != PackageManager.PERMISSION_GRANTED){
                permissionList.add(pm);
            }
        }

        //사용자가 필요 권한을 가지고 있지 않은 경우, 해당 권한 재요청
        if(!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(MainActivity.this, permissionList.toArray(new String[permissionList.size()]),MULTIPLE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case MULTIPLE_PERMISSION:
                for(int i = 0; i <permissions.length; i++){
                    if(permissions[i].equals(this.permissions[0])){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            showNoPermissionToast();
                        }
                    }
                    else if(permissions[i].equals(this.permissions[1])){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            showNoPermissionToast();
                        }
                    }
                    else if(permissions[i].equals(this.permissions[2])){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            showNoPermissionToast();
                        }
                    }
                }
        }
        return;
    }

    //필요한 권한을 얻지 못한 경우, 사용자에게 안내 후 종료
    private void showNoPermissionToast(){
        Toast.makeText(this, "해당 서비스 이용을 위해 설정에서 권한을 허용해주시기 바랍니다.",Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * socket 통신을 위한 thread class로 byte 형태의 image 전송
     */
    class ClientThread extends Thread {
        public void run(){
            String host = "49.236.144.45";
            int port = 13579;
            Boolean connected = true;
            Log.d("Client Activity","Client Thread is working.");

            try {
                Socket socket = new Socket(host, port);

                while (connected) {
                    try {
                        OutputStream outputStream = socket.getOutputStream();
                        Log.d("Client Activity", "Image Write.");
                        outputStream.write(imageByte);
                        outputStream.close();
                        connected = false;

                        Log.d("Client Activity", "Image sent.");
                    } catch (Exception e) {
                        Log.e("Client Activity", "Error", e);
                    }
                }
                socket.close();
                Log.d("Client Activity","Closed.");
            }
            catch (Exception e){
                Log.e("Client Activity","Error",e);
                e.printStackTrace();
            }
        }
    }

    /**
     * bitmap image를 byte 로 변환
     * @param bitmap bitmap image
     * @return  bytes image
     */
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}