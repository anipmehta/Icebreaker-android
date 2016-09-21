package in.icebreakerapp.icebreaker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

/**
 * Created by anip on 20/09/16.
 */
public class Profile extends AppCompatActivity {
    private static String TAG =null;
    private static String enroll;
    private static final String URL_UPLOAD_IMAGE = "http://anip.xyz:8080/upload/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enroll = getSharedPreferences("user", 0).getString("enroll","");

        showImagePopup();
//        clearPreferences();
    }
    private void clearPreferences() {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear com.example.saladdays");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject uploadImage(String memberId, String sourceImageFile) {

        try {
            File sourceFile = new File(sourceImageFile);

            Log.d(TAG, "File...::::" + sourceFile + " : " + sourceFile.exists());

            final MediaType MEDIA_TYPE = sourceImageFile.endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");


            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("picture",enroll+".png", RequestBody.create(MEDIA_TYPE, sourceFile))
                    .build();

            Request request = new Request.Builder()
                    .url(URL_UPLOAD_IMAGE+enroll+"/")
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }
    private void showImagePopup() {
        // File System.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        // Chooser of file system options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Please select");
        startActivityForResult(chooserIntent, 1010);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {
            if (data == null) {
//                Snackbar.make(findViewById(R.id.parentView), R.string.string_unable_to_pick_image, Snackbar.LENGTH_INDEFINITE).show();
                return;
            }
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                Log.i("hell","enterd");
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                final String imagePath = cursor.getString(columnIndex);
//                uploadImage("abc",imagePath);
                    new AsyncTask<Void, Integer, Boolean>() {

//                        ProgressDialog progressDialog;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
//                            progressDialog = new ProgressDialog(MainActivity.this);
//                            progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
//                            progressDialog.show();
                        }

                        @Override
                        protected Boolean doInBackground(Void... params) {

                            try {
                                JSONObject jsonObject = uploadImage("abc",imagePath);
                                if (jsonObject != null)
                                    return jsonObject.getString("result").equals("success");

                            } catch (JSONException e) {
                                Log.i("TAG", "Error : " + e.getLocalizedMessage());
                            }
                            return false;
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            super.onPostExecute(aBoolean);
//                            if (progressDialog != null)
//                                progressDialog.dismiss();

//                            if (aBoolean)
//                                Toast.makeText(getApplicationContext(), R.string.string_upload_success, Toast.LENGTH_LONG).show();
//                            else
//                                Toast.makeText(getApplicationContext(), R.string.string_upload_fail, Toast.LENGTH_LONG).show();

//                            imagePath = "";
//                            textView.setVisibility(View.VISIBLE);
//                            imageView.setVisibility(View.INVISIBLE);
                        }
                    }.execute();
                } else {
//                    Snackbar.make(findViewById(R.id.parentView), R.string.string_internet_connection_warning, Snackbar.LENGTH_INDEFINITE).show();
                }
//                Picasso.with(mContext).load(new File(imagePath))
//                        .into(imageView);
//                cursor.close();

            } else {
//                Snackbar.make(findViewById(R.id.parentView), R.string.string_unable_to_load_image, Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showImagePopup();
//                    }
//                }).show();
            }

//            textView.setVisibility(View.GONE);
//            imageView.setVisibility(View.VISIBLE);
        }
    }

