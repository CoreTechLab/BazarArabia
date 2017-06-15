package bazararabia.com.and.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import bazararabia.com.and.R;
import bazararabia.com.and.config.AppConstants;
import bazararabia.com.and.entity.UserModel;
import bazararabia.com.and.util.FirebaseClass;

public class RegisterSetupActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private Button registerEmailBtn;
    private SignInButton mGoogleBtn;
    private LoginButton mFacebookBtn;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseClass mFirebase;
    private String idToken;
    private String userFirstName, userLastName, userEmail;
    private String userId, userPassword;
    private CallbackManager callbackManager;

    private static final int RC_SIGN_IN = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_register_setup);
        mFirebase = new FirebaseClass();

        initView();
        initAction();
    }

    @Override
    public void initView() {
        registerEmailBtn = (Button) findViewById(R.id.register_button_email);
        mGoogleBtn = (SignInButton) findViewById(R.id.register_button_google);
        mFacebookBtn = (LoginButton) findViewById(R.id.register_button_facebook);
    }


    private void initAction(){
        mFirebase.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };

        registerEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterSetupActivity.this, RegisterEmailActivity.class);
                startActivity(intent);
            }
        });

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleInitialSetting();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        mFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookInitialSetting();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            if (requestCode==RC_SIGN_IN){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Google Sign In was successful, save Token and a state then authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    idToken = account.getIdToken();
                    userFirstName = account.getGivenName();
                    userLastName = account.getFamilyName();
                    userEmail = account.getEmail();
                    userPassword = account.getId().toString();
                    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                    firebaseAuthWithGoogle(credential);
                } else{
                    // Google Sign In failed, update UI appropriately
                    Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }else{
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    //After a successful sign into Google, this method now authenticates the user with Firebase
    private void firebaseAuthWithGoogle(AuthCredential credential) {
        mFirebase.mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "signInWithCredential" + task.getException().getMessage());
                        task.getException().printStackTrace();
                        Toast.makeText(RegisterSetupActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }else {
                        Toast.makeText(RegisterSetupActivity.this, "Login successful",
                                Toast.LENGTH_SHORT).show();
                        userId = mFirebase.getCurrentUserId();
                        UserModel user = new UserModel(userId, userFirstName, userLastName, userEmail, userPassword, "Gmail" );
                        mFirebase.mDatabase.child(AppConstants.USERS_TABLE).child(userId).setValue(user);
                        Intent intent = new Intent(RegisterSetupActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            });
    }

    private void googleInitialSetting(){
        // Configure sign-in to request the user's basic profile like name and email
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebase.mAuthListener != null){
            FirebaseAuth.getInstance().signOut();
        }
        mFirebase.mAuth.addAuthStateListener(mFirebase.mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFirebase.mAuthListener != null){
            mFirebase.mAuth.removeAuthStateListener(mFirebase.mAuthListener);
        }
    }






    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void facebookInitialSetting(){
        mFacebookBtn.setReadPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();
        mFacebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
            }
            @Override
            public void onError(FacebookException error) {Log.d("TAG", "facebook:onError", error);}
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebase.mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser account = mFirebase.mAuth.getCurrentUser();
                        userId = mFirebase.getCurrentUserId();
                        userEmail = account.getEmail();
                        userFirstName = account.getDisplayName();
                        userLastName = "";
                        UserModel user = new UserModel(userId, userFirstName, userLastName, userEmail, userPassword, "fb" );
                        mFirebase.mDatabase.child(AppConstants.USERS_TABLE).child(userId).setValue(user);
                    } else{
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                    }
                }
            });
    }

}
