package in.icebreakerapp.icebreaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import in.icebreakerapp.icebreaker.util.CircleTransform;

/**
 * Created by anip on 29/09/16.
 */
public class Profile extends AppCompatActivity {
    FloatingActionButton button;
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        button = (FloatingActionButton) findViewById(R.id.edit_image);
        imageView = (ImageView) findViewById(R.id.image);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,ImageUpload.class);
                startActivityForResult(intent,1010);
            }
        });
        Picasso.with(this).setLoggingEnabled(true);
        Picasso.with(this).setIndicatorsEnabled(true);
        Picasso.with(this)
//                .load(data.getSt)
                    .load("http://anip.xyz:8080/image/"+getSharedPreferences("user",0).getString("enroll","")+"/")
//                .resize(50, 50)
//                .centerCrop()
                .fit()
//                .memoryPolicy(MemoryPolicy.NO_CACHE)

                .centerCrop()
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .transform(new CircleTransform())
                .placeholder(R.drawable.icebreaker)
                .error(R.drawable.icebreaker)
                .into(imageView);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("hell","http://anip.xyz:8080/image/"+getSharedPreferences("user",0).getString("enroll","")+"/");
        if(requestCode == 1010 && resultCode==RESULT_OK){
            Picasso.with(this)
                    .load(data.getStringExtra("file"))
//                    .load("http://anip.xyz:8080/image/"+getSharedPreferences("user",0).getString("enroll","")+"/")
//                .resize(50, 50)
//                .centerCrop()
                    .fit()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)

                    .centerCrop()
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.icebreaker)
                    .error(R.drawable.icebreaker)
                    .into(imageView);
        }
    }
}
