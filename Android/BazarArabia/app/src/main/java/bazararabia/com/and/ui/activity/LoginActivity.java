package bazararabia.com.and.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import bazararabia.com.and.R;
import bazararabia.com.and.util.FirebaseClass;

public class LoginActivity extends BaseActivity{

    Button login_btn;

    EditText login_input_email;
    TextInputLayout login_input_layout_email;
    EditText login_input_password;
    TextInputLayout login_input_layout_password;
    FirebaseClass mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebase = new FirebaseClass();
        initView();
        mFirebase.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());// User is signed in
                } else {
                    Log.d("TAG", "onAuthStateChanged:signed_out");// User is signed out
                }
            }
        };
    }

    @Override
    public void initView() {
        login_input_email = (EditText)findViewById(R.id.login_input_email);
        login_input_layout_email = (TextInputLayout)findViewById(R.id.login_input_layout_email);
        login_input_password = (EditText)findViewById(R.id.login_input_password);
        login_input_layout_password =(TextInputLayout)findViewById(R.id.login_input_layout_password);
        login_btn = (Button)findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        mFirebase.mAuth.addAuthStateListener(mFirebase.mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFirebase.mAuthListener != null) {
            mFirebase.mAuth.removeAuthStateListener(mFirebase.mAuthListener);
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private static  boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean  validateEmail() {
        String email = login_input_email.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            login_input_layout_email.setError(getString(R.string.error_mail_valide));
            requestFocus(login_input_email);
            return false;
        } else {
            login_input_layout_email.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validatePassword() {
        if (login_input_password.getText().toString().trim().isEmpty() || login_input_password.getText().length()  < 6 ) {
            login_input_layout_password.setError(getString(R.string.error_password_short));
            requestFocus(login_input_password);
            return false;
        } else {
            login_input_layout_password.setErrorEnabled(false);
        }
        return true;
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        mFirebase.mAuth.signInWithEmailAndPassword(login_input_email.getText().toString(), login_input_password.getText().toString())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "signInWithEmail:failed", task.getException());
                        Toast.makeText(LoginActivity.this, "LogIn Failed",
                                Toast.LENGTH_SHORT).show();
                    } else{
                        Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }
            });
    }
}
