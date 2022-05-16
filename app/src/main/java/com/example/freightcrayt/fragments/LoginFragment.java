package com.example.freightcrayt.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.example.freightcrayt.R;
import com.example.freightcrayt.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginFragment extends Fragment {

    // DataHelper instance for managing global user data
    DataHelper dataHelper;

    // Firebase authentication instance
    private FirebaseAuth mAuth;

    // Form fields
    private TextInputLayout email;
    private TextInputLayout password;

    // sign-in button
    private Button signInButton;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // View for managing details
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

        // Initialise fields
        email = (TextInputLayout) view.findViewById(R.id.login_textBoxEmail);
        password = (TextInputLayout) view.findViewById(R.id.login_textBoxPassword);

        // Initialise button
        signInButton = view.findViewById(R.id.login_button);

        // Sign in button tap listener
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // remove error messages
                email.setError(null);
                password.setError(null);

                String userEmail = email.getEditText().getText().toString();
                String userPassword = password.getEditText().getText().toString();

                // check if fields are empty
                if(userEmail.isEmpty() || userPassword.isEmpty()) {
                    if(userEmail.isEmpty()) {
                        email.setError("Email cannot be empty");
                    }
                    if(userPassword.isEmpty()) {
                        password.setError("Password cannot be empty");
                    }

                    return;
                }


                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, redirect to collections page
                                    IntentHelper.openIntent(getContext(), "Login", MainActivity.class);

                                } else {
                                    // If sign in fails, display a message
                                    handleException(task.getException());
                                }
                            }
                        });
            }
        });

        return view;
    }

    private void handleException(Exception exception) {
        if(exception instanceof FirebaseAuthException) {
            String code = ((FirebaseAuthException) exception).getErrorCode();

            if(code.equals("ERROR_INVALID_EMAIL")) {
                email.setError("Email is invalid");
            }
            else if(code.equals("ERROR_USER_NOT_FOUND")) {
                email.setError("User not found");
            }
            else if(code.equals("ERROR_WRONG_PASSWORD")) {
                password.setError("Password is invalid");
            }

            return;
        }

        Toast.makeText(getContext(), "Unexpected error occurred, please retry", Toast.LENGTH_SHORT).show();
    }
}
