package com.example.freightcrayt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterFragment extends Fragment {

    // Firebase authentication instance
    private FirebaseAuth mAuth;

    // Form fields
    private TextInputLayout userName;
    private TextInputLayout email;
    private TextInputLayout password;

    // sign-up button
    private Button signUpButton;


    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

        // Initialise fields
        userName = (TextInputLayout) view.findViewById(R.id.register_textBoxName);
        email = (TextInputLayout) view.findViewById(R.id.register_textBoxEmail);
        password = (TextInputLayout) view.findViewById(R.id.register_textBoxPassword);

        // Initialise button
        signUpButton = (Button) view.findViewById(R.id.register_button);

        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                email.setError(null);
                password.setError(null);
                userName.setError(null);

                String userEmail = email.getEditText().getText().toString();
                String userPassword = password.getEditText().getText().toString();
                String username = userName.getEditText().getText().toString();

                if(username.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
                    if(userEmail.isEmpty()) {
                        email.setError("Email cannot be empty");
                    }
                    if(userPassword.isEmpty()) {
                        password.setError("Password cannot be empty");
                    }
                    if(username.isEmpty()) {
                        userName.setError("Username cannot be empty");
                    }
                    return;
                }

                // Register user with firebase auth
                mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    setUserName(username);
                                    IntentHelper.openIntent(getContext(), "Login", MainActivity.class);
                                } else {
                                    // Handle Exception types
                                    handleException(task.getException());
                                }
                            }
                        });
            }
        });

        return view;
    }

    private void setUserName(String username) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Failed to set username", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleException(@NonNull Exception exception) {
        if(exception instanceof FirebaseAuthException) {
            String code = ((FirebaseAuthException) exception).getErrorCode();

            if(code.equals("ERROR_INVALID_EMAIL")) {
                email.setError("Email is invalid");
            }
            else if(code.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                email.setError("Email is already in use");
            }
            else if(code.equals("ERROR_WEAK_PASSWORD")) {
                password.setError("Password is too weak");
            }

            return;
        }

        Toast.makeText(getContext(), "Unexpected error occurred, please retry", Toast.LENGTH_SHORT).show();
    }
}
