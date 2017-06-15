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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import bazararabia.com.and.R;
import bazararabia.com.and.config.AppConstants;
import bazararabia.com.and.entity.CategoryModel;
import bazararabia.com.and.entity.UserModel;
import bazararabia.com.and.util.FirebaseClass;

public class RegisterEmailActivity extends BaseActivity {

    private FirebaseClass mFirebase;
    private TextInputLayout register_input_layout_firstname;
    private EditText register_input_firstname;
    private TextInputLayout register_input_layout_lastname;
    private EditText register_input_lastname;
    private TextInputLayout register_input_layout_email;
    private EditText register_input_email;
    private TextInputLayout register_input_layout_password;
    private EditText register_input_password;
    private Button registerButton;

    private UserModel userModel;
    private CategoryModel categoryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        mFirebase = new FirebaseClass();
        initView();
        initAction();
    }

    @Override
    public void initView() {
        register_input_layout_firstname = (TextInputLayout) findViewById(R.id.register_input_layout_firstname);
        register_input_firstname = (EditText) findViewById(R.id.register_input_firstname);
        register_input_layout_lastname = (TextInputLayout) findViewById(R.id.register_input_layout_lastname);
        register_input_lastname = (EditText) findViewById(R.id.register_input_lastname);
        register_input_layout_email = (TextInputLayout) findViewById(R.id.register_input_layout_email);
        register_input_email = (EditText) findViewById(R.id.register_input_email);
        register_input_layout_password = (TextInputLayout) findViewById(R.id.register_input_layout_password);
        register_input_password = (EditText) findViewById(R.id.register_input_password);

        registerButton = (Button) findViewById(R.id.register_btn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

    }


    private void initAction(){
        mFirebase.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());  // User is signed in
                } else {
                    Log.d("TAG", "onAuthStateChanged:signed_out");  // User is signed out
                }
            }
        };

    }


    private void submitForm(){
        if (!validateFirstName()) {return;}
        if (!validateLastName()) {return;}
        if (!validateEmail()) {return;}
        if (!validatePassword()) {return;}
        userModel = new UserModel();
        userModel.setFirstName(register_input_firstname.getText().toString());
        userModel.setLastName(register_input_lastname.getText().toString());
        userModel.setEmail(register_input_email.getText().toString());
        userModel.setPassword(register_input_password.getText().toString());
        mFirebase.mAuth.createUserWithEmailAndPassword(this.register_input_email.getText().toString(),this.register_input_password.getText().toString())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(RegisterEmailActivity.this, "The account exists. Sign Up failed",
                                Toast.LENGTH_SHORT).show();
                    } else{
                        onAuthenticationSuccess(task.getResult().getUser());
                    }
                }
            });
    }

    private void onAuthenticationSuccess(FirebaseUser mUser){
        String userId = mUser.getUid();
        UserModel user = new UserModel(userId, userModel.getFirstName() , userModel.getLastName(), userModel.getEmail(), userModel.getPassword(), "Email");
        mFirebase.mDatabase.child(AppConstants.USERS_TABLE).child(userId).setValue(user);

        startActivity(new Intent(RegisterEmailActivity.this, MainActivity.class));
        finish();
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

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean  validateEmail() {
        String email = register_input_email.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            register_input_layout_email.setError(getString(R.string.error_mail_valide));
            requestFocus(register_input_email);
            return false;
        } else {
            register_input_layout_email.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (register_input_password.getText().toString().trim().isEmpty() || register_input_password.getText().length()  < 6 ) {
            register_input_layout_password.setError(getString(R.string.error_password_short));
            requestFocus(register_input_password);
            return false;
        } else {
            register_input_layout_password.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateFirstName() {
        if (register_input_firstname.getText().toString().trim().isEmpty() || register_input_firstname.getText().length()  < 3 ) {
            register_input_layout_firstname.setError(getString(R.string.error_short_value));
            requestFocus(register_input_firstname);
            return false;
        } else {
            register_input_layout_firstname.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLastName() {
        if (register_input_lastname.getText().toString().trim().isEmpty() || register_input_lastname.getText().length()  < 3 ) {
            register_input_layout_firstname.setError(getString(R.string.error_short_value));
            requestFocus(register_input_lastname);
            return false;
        } else {
            register_input_layout_lastname.setErrorEnabled(false);
        }
        return true;
    }
}
