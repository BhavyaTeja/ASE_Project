package com.example.saimohith.homeapplication;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.BuildCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class signupActivity extends AppCompatActivity implements View.OnClickListener {


    private Button Register;

    private EditText Username;
    private EditText email;
    private EditText Password;
    private EditText password;

    private Button login;

    ImageView imageView;
    Button Gallery;
    Button Photo;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    public Object bitmap;

    private Button getAddress;
    private TextView Address;
    private RequestQueue requestQueue;

    public static final int REQUEST_CAPTURE = 1;


    private ProgressDialog PD;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        imageView = (ImageView) findViewById(R.id.profile);
        imageView.setImageBitmap((Bitmap) bitmap);

        Gallery = (Button) findViewById(R.id.Gallery);
        Photo = (Button) findViewById(R.id.picture);
        getAddress = (Button) findViewById(R.id.GetAddress);
        Address = (TextView) findViewById(R.id.Address);
        requestQueue = Volley.newRequestQueue(this);

        firebaseAuth = FirebaseAuth.getInstance();
        PD = new ProgressDialog(this);


        Register = (Button) findViewById(R.id.Register);

        Username = (EditText) findViewById(R.id.RUsername);
        email = (EditText) findViewById(R.id.REmail);
        password = (EditText) findViewById(R.id.RPassword);
        Password = (EditText) findViewById(R.id.RPassword2);

        login = (Button) findViewById(R.id.login);

        Register.setOnClickListener(this);
        login.setOnClickListener(this);
        getAddress.setOnClickListener(this);


        if (!hasCamera()) {
            Photo.setEnabled(false);
        }

        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });
    }

    public boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void onPicClick(View v){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUEST_CAPTURE);
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        if (req == REQUEST_CAPTURE && res == RESULT_OK) {
            Bundle ext = data.getExtras();
            Bitmap pic = (Bitmap) ext.get("data");
            imageView.setImageBitmap(pic);


        } else {
            super.onActivityResult(req, res, data);
            if (res == RESULT_OK && req == PICK_IMAGE) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }

    private void regUser(){
        String uname = Username.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String pass = Password.getText().toString().trim();
        String pass1 = password.getText().toString().trim();

        if (!pass.equals(pass1))
        {
            Toast.makeText(signupActivity.this, "Password Doesn't Match!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(uname)){
            //Username is empty
            Toast.makeText(this,"Please enter a Username", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(mail)){
            //email is empty
            Toast.makeText(this, "Please Enter the Email id", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass1)){
            //Username is empty
            Toast.makeText(this,"Please enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            //pass is empty
            Toast.makeText(this, "Please retype the Password", Toast.LENGTH_SHORT).show();
            return;
        }

        PD.setMessage("Registering User..");
        PD.show();

        firebaseAuth.createUserWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            Toast.makeText(signupActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(signupActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                        PD.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View p) {

        if (p == Register) {
            regUser();
        }

        if (p == login) {

            //login
            Intent k = new Intent(signupActivity.this, LoginActivity.class);
            startActivity(k);
        }
        if(p == getAddress){
            JsonObjectRequest request;
            request = new JsonObjectRequest("https://maps.googleapis.com/maps/api/geocode/json?latlng=39.0337471,-94.5786191&key=AIzaSyCbpmouMwoyOS3fE4eUVdbY_Za0wx4pv8A", new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String address = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                        Address.setText(address);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(request);

        }

    }


}
