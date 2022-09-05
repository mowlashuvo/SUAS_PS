package com.example.suas_ps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class RequestFragment extends Fragment {
    private static final int CAMERA_REQUEST = 1888;

    private EditText roomEditText, contactEditText, descriptionEditText;
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


    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        roomEditText = view.findViewById(R.id.roomEditText);
        contactEditText = view.findViewById(R.id.contactEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        addImageOrVideo = view.findViewById(R.id.addImageOrVideo);
        submitButton = view.findViewById(R.id.submitButton);
        image = view.findViewById(R.id.image);
        initViews();
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();


        submitButton.setOnClickListener(view1 -> {
//            if (user != null) {
//                userID = mAuth.getCurrentUser().getUid();
//
//                final DocumentReference documentReference = firebaseFirestore.collection("review").document(userID);
//
//                Date currentTime = Calendar.getInstance().getTime();
//
//                final ServiceRequestModel serviceRequestModel = new ServiceRequestModel(roomEditText.getText().toString(), contactEditText.getText().toString(), descriptionEditText.getText().toString(), currentTime);
//
//
//
//                documentReference.set(serviceRequestModel).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toasty.success(getContext(), "Submitted Successfully", Toast.LENGTH_LONG, true).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toasty.error(getContext(), "Try Again", Toast.LENGTH_LONG, true).show();
//                    }
//                });
//
//            }


            if (roomEditText.getText().toString().isEmpty() || contactEditText.getText().toString().isEmpty() || descriptionEditText.getText().toString().isEmpty()) {
//                MDToast.makeText(AddProduct.this, "Please Fill All THe fields above FIrst.", MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                Toasty.error(getContext(), "Please Fill All THe fields above First.", Toast.LENGTH_LONG, true).show();
                return;
            }

            if (picSelected == 0) {
                Toasty.error(getContext(), "Please Select A Image First.", Toast.LENGTH_LONG, true).show();
//                    MDToast.makeText(AddProduct.this, "Please Select A Image First.", MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                return;
            }

            mDatabaseForProductUploading = FirebaseDatabase.getInstance().getReference().child("service_request");
            key = mDatabaseForProductUploading.push().getKey();

            uploadImageFirst();
        });

        return view;
    }

    private void initViews() {

        progressDialog = new ProgressDialog(getContext());

        addImageOrVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: ");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

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
//                Toast.makeText(AddProduct.this, "Error!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }

    private void uploadDataToDatabase() {
        Date currentTime = Calendar.getInstance().getTime();
        HashMap Data = new HashMap();
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
                Toasty.success(getContext(), "Request Submitted Successfully", Toast.LENGTH_LONG, true).show();
                roomEditText.clearFocus();
                roomEditText.setText("");
                contactEditText.clearFocus();
                contactEditText.setText("");
                descriptionEditText.clearFocus();
                descriptionEditText.setText("");
                image.setImageBitmap(null);
//                MDToast.makeText(AddProduct.this, "Food Added", MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();
//                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            resultUri = getImageUri(getContext(), photo);
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