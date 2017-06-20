package in.icebreakerapp.icebreaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import in.icebreakerapp.icebreaker.helpers.InternetCheck;
import in.icebreakerapp.icebreaker.preferences.SliderPref;

/**
 * Created by anip on 14/09/16.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);

                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    SharedPreferences sharedPreferences =getSharedPreferences("user",0);
                    Intent intent;
                    if(sharedPreferences.getString("enroll","").equalsIgnoreCase("") ){
                        intent = new Intent(SplashActivity.this,IntroSlider.class);
                    }
                    else if(sharedPreferences.getString("signup","").equalsIgnoreCase("")){
                        intent = new Intent(SplashActivity.this,Signup.class);
                    }
                    else
                        intent = new Intent(SplashActivity.this,Home.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
