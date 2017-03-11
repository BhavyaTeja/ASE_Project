package com.example.saimohith.homeapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    private EditText email;
    private EditText Password;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth = FirebaseAuth.getInstance();


        if (firebaseAuth.getCurrentUser() != null) {

            finish();

            startActivity(new Intent(getApplicationContext(),Home.class));
        }

        email = (EditText) findViewById(R.id.Lid);
        Password = (EditText) findViewById(R.id.Password);


        PD = new ProgressDialog(this);

    }



    private void loginUser() {

        String mail = email.getText().toString().trim();
        String pass = Password.getText().toString().trim();

        if (TextUtils.isEmpty(mail)) {
            //email is empty
            Toast.makeText(this, "Please Enter the Email id", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            //pass is empty
            Toast.makeText(this, "Please Enter the Password", Toast.LENGTH_SHORT).show();
            return;
        }
        PD.setMessage("Logging in.. ");
        PD.show();


        firebaseAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        PD.dismiss();
                        /*//if the task is successfull
                        if(task.isSuccessful()) {
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), Voice.class));
                        }*/
                        if (task.isSuccessful()) {
                            finish();
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));


                        }
                    }
                });
    }


    public void ButtonClick(View v) {
        loginUser();
    }

    public void RegButtonClick(View v){

        Intent k = new Intent(LoginActivity.this, signupActivity.class);
        startActivity(k);
    }


}




