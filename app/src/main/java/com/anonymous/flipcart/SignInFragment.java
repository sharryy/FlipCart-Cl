package com.anonymous.flipcart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignInFragment extends Fragment {

    public SignInFragment() {
        // Required empty public constructor
    }

    private TextView dontHaveAnAccount;
    private TextView forgotPassword;
    private FrameLayout parentFrameLayout;
    private EditText emailSignIn;
    private EditText passwordSignIn;
    private ImageButton closeBtnSignIn;
    private Button signInBtn;
    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        dontHaveAnAccount = view.findViewById(R.id.tv_dont_have_an_account);
        forgotPassword = view.findViewById(R.id.sign_in_forgot_password);
        parentFrameLayout = getActivity().findViewById(R.id.register_frame_layout);
        emailSignIn = view.findViewById(R.id.sign_in_email);
        passwordSignIn = view.findViewById(R.id.sign_in_password);
        closeBtnSignIn = view.findViewById(R.id.sign_in_close_button);
        signInBtn = view.findViewById(R.id.sign_in_button);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.signin_progress_bar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dontHaveAnAccount.setOnClickListener(v -> setFragment(new SignUpFragment()));
        forgotPassword.setOnClickListener(v -> {
            RegisterActivity.onResetPasswordFragment = true;
            setFragment(new ResetPasswordFragment());
        });
        emailSignIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordSignIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        closeBtnSignIn.setOnClickListener(v -> mainIntent());
        signInBtn.setOnClickListener(v -> checkEmailAndPassword());
    }

    private void checkEmailAndPassword() {
        if (emailSignIn.getText().toString().matches(emailPattern)) {
            if (passwordSignIn.getText().length() >= 4) {
                progressBar.setVisibility(View.VISIBLE);
                signInBtn.setEnabled(false);
                signInBtn.setTextColor(Color.argb(50, 255, 255, 255));
                firebaseAuth.signInWithEmailAndPassword(emailSignIn.getText().toString(), passwordSignIn.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                mainIntent();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                signInBtn.setEnabled(true);
                                signInBtn.setTextColor(Color.rgb(255, 255, 255));
                                Toast.makeText(getActivity(), "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getActivity(), "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(emailSignIn.getText().toString())) {
            if (!TextUtils.isEmpty(passwordSignIn.getText().toString())) {
                signInBtn.setEnabled(true);
                signInBtn.setTextColor(Color.rgb(255, 255, 255));
            } else {
                signInBtn.setEnabled(false);
                signInBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            signInBtn.setEnabled(false);
            signInBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void mainIntent() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }
}