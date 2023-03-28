package com.example.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class AddProduct extends AppCompatActivity {


    private final int PICK_IMAGE_REQUEST = 22;
    FloatingActionButton add;
    String categ;
    EditText category;
    EditText description;
    private Uri filePath;
    ImageView image;
    String imgLink = "";
    EditText name;
    EditText price;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        category = findViewById(R.id.category);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, 17367048);
//        adapter.setDropDownViewResource(17367049);
//        this.category.setAdapter(adapter);
//        this.category.setOnItemSelectedListener(this);
        FirebaseStorage instance = FirebaseStorage.getInstance();
        this.storage = instance;
        this.storageReference = instance.getReference();
        this.image = (ImageView) findViewById(R.id.image);
        this.name = (EditText) findViewById(R.id.name);
        this.price = (EditText) findViewById(R.id.price);
        this.description = (EditText) findViewById(R.id.description);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add);
        this.add = floatingActionButton;
        floatingActionButton.setVisibility(View.VISIBLE);
        this.reference = FirebaseDatabase.getInstance().getReference("Product");
        this.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SelectImage();
            }
        });

        categ = "Bed";

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String n = AddProduct.this.name.getText().toString().trim();
                String p = AddProduct.this.price.getText().toString().trim();
                String d = AddProduct.this.description.getText().toString().trim();
                if (n.isEmpty() || p.isEmpty() || d.isEmpty()) {
                    Toast.makeText(AddProduct.this, "Error: Empty Fields !!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String cat = category.getText().toString().trim();
                AddProduct addProduct = AddProduct.this;
                addProduct.addProduct(n, p, d, cat);
            }
        });
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 22);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == -1 && data != null && data.getData() != null) {
            this.filePath = data.getData();
            try {
                this.image.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), this.filePath));
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                final StorageReference ref = this.storageReference.child("images/" + UUID.randomUUID().toString());
                ref.putFile(this.filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                        UploadTask.TaskSnapshot taskSnapshot = task.getResult();
                        progressDialog.setMessage("Uploaded " + ((int) ((((double) taskSnapshot.getBytesTransferred()) * 100.0d) / ((double) taskSnapshot.getTotalByteCount()))) + "%");
                        if (task.isSuccessful()) {
                            return ref.getDownloadUrl();
                        }
                        throw task.getException();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    public void onComplete(Task<Uri> task) {
                        if (task.isSuccessful()) {
                            AddProduct.this.imgLink = task.getResult().toString();
                            Toast.makeText(AddProduct.this, "Link: " + AddProduct.this.imgLink, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            AddProduct.this.add.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddProduct.this, "Failed:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                if (this.imgLink != null) {
                    this.add.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addProduct(String n, String p, String d, String categ2) {
        if (this.filePath != null) {
            int cost = Integer.parseInt(p);
            String id = generateID();
            HashMap<String, Object> map = new HashMap<>();
            map.put("ID", id);
            map.put("name", n);
            map.put("price", Integer.valueOf(cost));
            map.put("description", d);
            map.put("category", categ2);
            map.put("image", this.imgLink);
            this.reference.child(categ2).child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddProduct.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return;
        }
        Toast.makeText(this, "Select image at first", Toast.LENGTH_SHORT).show();
    }

    private String generateID() {
        return "" + new SimpleDateFormat("dd-MM-yyyyHH:mm:ssz").format(new Date());
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.categ = adapterView.getItemAtPosition(i).toString();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

}