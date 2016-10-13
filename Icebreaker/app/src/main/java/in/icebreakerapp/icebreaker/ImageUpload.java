package in.icebreakerapp.icebreaker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

/**
 * Created by anip on 20/09/16.
 */
public class ImageUpload extends AppCompatActivity {
    private static String TAG = null;
    private static String enroll;
    private ImageView imageView;
    private CropImageView mCropView;
    private FloatingActionButton button;
    private Uri finalImage;
    private static final String URL_UPLOAD_IMAGE = "http://anip.xyz:8080/upload/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        imageView = (ImageView) findViewById(R.id.image);
        mCropView = (CropImageView) findViewById(R.id.cropImageView);
        mCropView.setInitialFrameScale(0.75f);
        mCropView.setCompressQuality(90);
        mCropView.setCompressFormat(Bitmap.CompressFormat.JPEG);
        mCropView.setOutputMaxSize(1080,1080);

        enroll = getSharedPreferences("user", 0).getString("enroll", "");
        button = (FloatingActionButton) findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCropView.startCrop(

                        createSaveUri(),

                        new CropCallback() {
                            @Override
                            public void onSuccess(Bitmap cropped) {
                                Log.i("hell_final_bitmap", String.valueOf(cropped));
                                mCropView.setCompressFormat(Bitmap.CompressFormat.JPEG);
                            }

                            @Override
                            public void onError() {
                            }
                        },

                        new SaveCallback() {
                            @Override
                            public void onSuccess(Uri outputUri) {
                                Log.i("hell_final_image", String.valueOf(outputUri));
                              finalImage=outputUri;
                                prepareImage(finalImage);

                            }

                            @Override
                            public void onError() {
                            }
                        }
                );

            }
        });


        showImagePopup();
    }

    private void prepareImage(final Uri finalImage) {
        Log.i("hell", String.valueOf(finalImage));
            Log.i("hell", "enterd");
            final String imagePath = String.valueOf(finalImage);
            new AsyncTask<Void, Integer, Boolean>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Boolean doInBackground(Void... params) {

                    try {
                        JSONObject jsonObject = uploadImage("abc",imagePath);
                        if (jsonObject != null)
                            return jsonObject.getString("status").equals("true");

                    } catch (JSONException e) {
                        Log.i("TAG", "Error : " + e.getLocalizedMessage());
                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if(aBoolean) {
                        Intent intent = getIntent();
                        intent.putExtra("file",finalImage.toString());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }
            }.execute();
    }

    public static JSONObject uploadImage(String memberId, String sourceImageFile) {

        try {
            File sourceFile = new File(sourceImageFile.replace("file://",""));

            Log.d(TAG, "File...::::" + sourceFile + " : " + sourceFile.exists());

            final MediaType MEDIA_TYPE = sourceImageFile.endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            Bitmap bmp = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);


            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("picture", enroll + ".png", RequestBody.create(MEDIA_TYPE, bos.toByteArray()))
                    .build();

            Request request = new Request.Builder()
                    .url(URL_UPLOAD_IMAGE + enroll + "/")
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
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Please select");
        startActivityForResult(chooserIntent, 1010);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {

            Uri selectedImageUri = data.getData();
            mCropView.startLoad(selectedImageUri, new LoadCallback() {
                @Override
                public void onSuccess() {
                    Log.i("hell", "loaded");
                }

                @Override
                public void onError() {
                }
            });
        }
    }
    public Uri createSaveUri() {
        return Uri.fromFile(new File(this.getCacheDir(), "cropped"));
    }
}