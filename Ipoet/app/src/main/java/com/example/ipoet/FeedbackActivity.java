package com.example.ipoet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FeedbackActivity extends AppCompatActivity {

    EditText name;
    EditText msg;
    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        name=findViewById(R.id.name_edit);
        msg=findViewById(R.id.msg_edit);
        submitButton=findViewById(R.id.feed_submit);
        MobileAds.initialize(this, getString(R.string.MOBILE_AD_APP_IDS));
        AdView mAdView =  findViewById(R.id.adsView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isIt=validateData();
                if(isIt) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Users").push();
                    myRef.child("User").child("Name").setValue(name.getText().toString());
                    myRef.child("User").child("Message").setValue(msg.getText().toString());
                    Toast.makeText(FeedbackActivity.this, getString(R.string.FEEDBACK_SUCCESS_MSG), Toast.LENGTH_SHORT).show();
                }
                else{
                    new AlertDialog.Builder(FeedbackActivity.this)
                            .setTitle(R.string.ALERT_TITLE)
                            .setMessage(R.string.FEED_ERROR_MSG)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();

                }


                }

        });


    }
    public boolean validateData()
    {
        String names=name.getText().toString();
        String msgs=msg.getText().toString();
        if((names.equals(null)||names.equals(""))||(msgs.equals(null)||(msgs.equals(""))))
        {
            return  false;
        }
        else
            return true;
    }
}
