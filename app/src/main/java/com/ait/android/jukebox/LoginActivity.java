package com.ait.android.jukebox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.ait.android.jukebox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.onClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.btnRegister)
    void registerClick() {
        if (!isFormValid()) {
            return;
        }

        showProgressDialog();

        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),
                etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    FirebaseUser fbUser = task.getResult().getUser();

                    fbUser.updateProfile(
                            new UserProfileChangeRequest.Builder().
                                    setDisplayName(usernameFromEmail(fbUser.getEmail())).build());

                    Toast.makeText(LoginActivity.this, "Registration ok", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "ERROR: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Wait for it...");
        }

        progressDialog.show();
    }

    @OnClick(R.id.btnLogin)
    void loginClick() {
        if (!isFormValid()) {
            return;
        }

        showProgressDialog();

        firebaseAuth.signInWithEmailAndPassword(
                etEmail.getText().toString(),
                etPassword.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Error: "+task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean isFormValid() {
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("The email cannot be empty");
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText())){
            etPassword.setError("The password cannot be empty");
            return false;
        }
        return true;
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}
