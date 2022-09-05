package com.example.suas_ps;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userID;
    private String facebookUserId = "";
    private String facebookUserName = "";
    private TextView idName;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        idName = view.findViewById(R.id.idName);

        userInfo();

        return view;
    }


    private void userInfo() {
// find the Facebook profile and get the user's id
        for (UserInfo profile : user.getProviderData()) {
            // check if the provider id matches "facebook.com"
            if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                facebookUserId = profile.getUid();
                facebookUserName = profile.getDisplayName();
            } else {
                if (GoogleAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    facebookUserId = profile.getUid();
                    facebookUserName = profile.getDisplayName();
                } else {
                    if (EmailAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                        facebookUserId = profile.getUid();
                        facebookUserName = profile.getDisplayName();
                    } else {
                        if (PhoneAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                            facebookUserId = profile.getUid();
                            facebookUserName = profile.getDisplayName();
                        }
                    }
                }
            }

        }


// construct the URL to the profile picture, with a custom height
// alternatively, use '?type=small|medium|large' instead of ?height=
        String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";

// (optional) use Picasso to download and show to image
//        Picasso.get().load(photoUrl).placeholder(R.drawable.user).into(profilePicture);
        idName.setText(user.getEmail());
    }

}