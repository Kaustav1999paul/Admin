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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    FirebaseAuth auth;
    EditText email;
    FirebaseUser firebaseUser;
    TextView login;
    EditText name;
    EditText password;
    DatabaseReference reference;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.emailID);
        password = (EditText) findViewById(R.id.pass);
        register = (Button) findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String e = Register.this.email.getText().toString().trim();
                String p = Register.this.password.getText().toString().trim();

                if (e.isEmpty() || p.isEmpty()) {
                    Toast.makeText(Register.this, "Fields should not be Empty", Toast.LENGTH_SHORT).show();
                } else if (!e.contains("@") || !e.contains(".com")) {
                    Toast.makeText(Register.this, "Not a valid emailID", Toast.LENGTH_SHORT).show();
                } else if (p.length() <= 5) {
                    Toast.makeText(Register.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(e, p);
                }
            }
        });
    }

    private void registerUser(String e, String p) {
        auth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    reference = FirebaseDatabase.getInstance().getReference("Admin");
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", Register.this.firebaseUser.getUid());
                    map.put("Email", e);
                    Register.this.reference.child(Register.this.firebaseUser.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(Register.this, Home.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}