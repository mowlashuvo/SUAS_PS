package com.example.suas_ps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class GiveawaysActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;

    private EditText roomEditText, contactEditText, descriptionEditText, titleEditText, dormEditText;
    private ImageButton addImageOrVideo;
    private Button submitButton;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userID;


    String key;
    DatabaseReference mDatabaseForProductUploading;

    Uri resultUri;
    int picSelected = 0;
    String imageUrl;

    ProgressDialog progressDialog;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giveaways);
        initViews();

        submitButton.setOnClickListener(view1 -> {
            if (dormEditText.getText().toString().isEmpty() || titleEditText.getText().toString().isEmpty() || roomEditText.getText().toString().isEmpty() || contactEditText.getText().toString().isEmpty() || descriptionEditText.getText().toString().isEmpty()) {
                Toasty.error(this, "Please Fill All THe fields above First.", Toast.LENGTH_LONG, true).show();
                return;
            }

            if (picSelected == 0) {
                Toasty.error(this, "Please Select A Image First.", Toast.LENGTH_LONG, true).show();
                return;
            }

            mDatabaseForProductUploading = FirebaseDatabase.getInstance().getReference().child("giveaways");
            key = mDatabaseForProductUploading.push().getKey();

            uploadImageFirst();
        });

        addImageOrVideo.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });
    }

    private void initViews() {
        titleEditText = findViewById(R.id.titleEditText);
        dormEditText = findViewById(R.id.dormEditText);
        roomEditText = findViewById(R.id.roomEditText);
        contactEditText = findViewById(R.id.contactEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        addImageOrVideo = findViewById(R.id.addImageOrVideo);
        submitButton = findViewById(R.id.submitButton);
        image = findViewById(R.id.image);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);


    }

    private void uploadImageFirst() {

        progressDialog.setTitle("Uploading Image");
        progressDialog.setMessage("Please Wait while we Upload your Request");
        progressDialog.show();

        final StorageReference Submit_Datareference = FirebaseStorage.getInstance().getReference(roomEditText.getText().toString()).child(key);
        Submit_Datareference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Submit_Datareference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //imagesUrl.add(uri.toString());
                        progressDialog.dismiss();
                        imageUrl = uri.toString();

                        uploadDataToDatabase();

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //Dialog.Set_Percetage((int) progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });

    }

    private void uploadDataToDatabase() {
        Date currentTime = Calendar.getInstance().getTime();
        HashMap Data = new HashMap();
        Data.put("title", titleEditText.getText().toString());
        Data.put("dorm_no", dormEditText.getText().toString());
        Data.put("room_no", roomEditText.getText().toString());
        Data.put("contact_no", contactEditText.getText().toString());
        Data.put("service_image", imageUrl);
        Data.put("description", descriptionEditText.getText().toString());
        Data.put("user_id", user.getUid());
        Data.put("current_time", currentTime.toString());
        Data.put("status", Constants.PENDING_STATUS);

        mDatabaseForProductUploading.child(roomEditText.getText().toString()).updateChildren(Data).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toasty.success(GiveawaysActivity.this, "Giveaways Submitted Successfully", Toast.LENGTH_LONG, true).show();
                roomEditText.clearFocus();
                roomEditText.setText("");
                titleEditText.clearFocus();
                titleEditText.setText("");
                dormEditText.clearFocus();
                dormEditText.setText("");
                contactEditText.clearFocus();
                contactEditText.setText("");
                descriptionEditText.clearFocus();
                descriptionEditText.setText("");
                image.setImageBitmap(null);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            resultUri = getImageUri(this, photo);
            image.setImageBitmap(photo);
            picSelected = 1;

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}