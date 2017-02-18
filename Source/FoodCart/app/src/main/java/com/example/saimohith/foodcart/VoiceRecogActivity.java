package com.example.saimohith.foodcart;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Locale;


public class VoiceRecogActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebase;
    private Button logout;
    private TextView UNAME;
    private TextView restext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recog);
        firebase = FirebaseAuth.getInstance();

        if(firebase.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        FirebaseUser user = firebase.getCurrentUser();

        UNAME = (TextView) findViewById(R.id.RUsername);
        UNAME.setText("Welcome "+ user.getEmail());
        logout = (Button) findViewById(R.id.Logout);
        logout.setOnClickListener(this);

        restext = (TextView)findViewById(R.id.speechText);
    }

    public void onClick(View vw){

        if (vw == logout){
            firebase.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void imageClick(View V){
        if (V.getId() == R.id.imageButton){
            speechInput();
        }
    }

    public void speechInput(){
        Intent t = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        t.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        t.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        t.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something");

        try{
            startActivityForResult(t, 100);
        }
        catch(ActivityNotFoundException b){
            Toast.makeText(VoiceRecogActivity.this, "Device doesn't support speech language!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int rcode, int resCode, Intent t)
    {
        super.onActivityResult(rcode,resCode, t);
        switch(rcode)
        {
            case 100: if(resCode == RESULT_OK && t!=null);
            {
                ArrayList<String> text = t.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                restext.setText(text.get(0));
            }
            break;

        }
    }

}



