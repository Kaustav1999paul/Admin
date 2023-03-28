package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView createAccount;
    EditText email;
    Button login;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    EditText password;

    public void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        };


        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        TextView textView = (TextView) findViewById(R.id.register);
        createAccount = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String e = MainActivity.this.email.getText().toString().trim();
                String p = MainActivity.this.password.getText().toString().trim();
                if (e.isEmpty() || p.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields should not be Empty", Toast.LENGTH_SHORT).show();
                } else if (!e.contains("@") || !e.contains(".com")) {
                    Toast.makeText(MainActivity.this, "Not a valid emailID", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(e, p);
                }
            }
        });
    }

    private void loginUser(String e, String p) {
        mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}