package com.publicTA.template.src.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.publicTA.template.R;
import com.publicTA.template.src.main.MainActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;

public class SplashActivity extends AppCompatActivity {

    TextView mTopCurtain,mBottomCurtain;


    Animation mAnimateMoveUp;
    Animation mAnimateMoveDown;
    ConstraintLayout mParent;
    ImageView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getKeyHash(getApplicationContext());

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int curtainHeight = metrics.heightPixels / 2;

        mTopCurtain = findViewById(R.id.splash_top_curtain);
        mBottomCurtain = findViewById(R.id.splash_bottom_curtain);
        mParent = findViewById(R.id.splash_parent);
        mLogo = findViewById(R.id.splash_logo);

//        mAnimateMoveDown = new TranslateAnimation(0, 0, 0, curtainHeight);
//        mAnimateMoveUp = new TranslateAnimation(0, 0, 0, -curtainHeight);
//        mAnimateMoveDown.setDuration(1200);
//        mAnimateMoveDown.setFillAfter(true);
//        mAnimateMoveUp.setDuration(1200);
//        mAnimateMoveUp.setFillAfter(true);
//
//        mBottomCurtain.startAnimation(mAnimateMoveDown);
//        mTopCurtain.startAnimation(mAnimateMoveUp);
        splash();
    }

    private void splash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 700);
    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null) return null;
        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                System.out.println("hash key : " + Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }



}
