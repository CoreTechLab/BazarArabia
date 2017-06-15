package bazararabia.com.and.ui.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import bazararabia.com.and.R;
import bazararabia.com.and.config.AppConstants;
import bazararabia.com.and.config.MyApplication;
import bazararabia.com.and.config.MySharedPreferenceManager;
import bazararabia.com.and.entity.CategoryModel;
import bazararabia.com.and.font.MaterialDesignIconsTextView;
import bazararabia.com.and.util.FirebaseClass;

public class SplashActivity extends BaseActivity {

    public static final String SPLASH_SCREEN_OPTION_1 = "Fade in + Ken Burns";
    public static final String SPLASH_SCREEN_OPTION_2 = "Down + Ken Burns";
    public static final String SPLASH_SCREEN_OPTION_3 = "Down + fade in + Ken Burns";

    private KenBurnsView mKenBurns;
    private MaterialDesignIconsTextView mLogo;
    private TextView welcomeText;
    private RelativeLayout rootLayout;

    FirebaseClass mFirebase = new FirebaseClass();

    private ArrayList<CategoryModel> categoryList;


    private final int MY_PERMISSION_REQUEST_EXTERNAL_STORAGE= 2;
    private boolean isWritePermissionEnabled = false;
    private long firstTime;
    private long secondTime;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        checkWritePermission();
    //    initializeCatetory();
        categoryList = new ArrayList<>();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        firstTime = System.currentTimeMillis();

        initView();
        initAction();


    }

    private void checkWritePermission(){
        int writePermissionCheck = ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(writePermissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("SPLASH_SCREEN" , "Camera Permission denied");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_EXTERNAL_STORAGE);
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else
        {
            isWritePermissionEnabled = true;
            getCategories();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    isWritePermissionEnabled = true;
                    getCategories();
                } else {
                    showErrorMessage("Please Allow Write External Storage Permission",
                            "This app requires write external storage permission for perfect performance." ,
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SplashActivity.this.finish();
                                }
                            }
                    );
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void getCategories(){
        final DatabaseReference dbCategory = mFirebase.mDatabase.child(AppConstants.TEST_CATEGORY).child("subCategory");
        dbCategory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryList.clear();
                for(DataSnapshot categorySnapshot: dataSnapshot.getChildren()){
                    GenericTypeIndicator<CategoryModel> genericTypeIndicator = new GenericTypeIndicator<CategoryModel>() {};
                    CategoryModel subCategory = categorySnapshot.getValue(genericTypeIndicator);
                    categoryList.add(subCategory);
                }
                dbCategory.removeEventListener(this);
                CategoryModel catList = new CategoryModel();
                catList.subCategory = categoryList;
                MyApplication.getInstance().setCategoryList(catList);

                secondTime = System.currentTimeMillis();
                long diffTime = secondTime - firstTime;
                if (diffTime <= 3000){
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            go2NextScreen();
                        }
                    } , 3000-diffTime + 2000);
                }else{
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            go2NextScreen();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void go2NextScreen()
    {
        MySharedPreferenceManager sf = new MySharedPreferenceManager(this);
        boolean isFirstLaunch = sf.getValueBoolean("isFirstLaunch" , true);

        if(isFirstLaunch) {
            Intent mainIntent = new Intent(SplashActivity.this, WizardActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }
        else{
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }
    }

    @Override
    public void initView() {
        mKenBurns = (KenBurnsView) findViewById(R.id.ken_burns_images);
        mLogo = (MaterialDesignIconsTextView) findViewById(R.id.logo);
        welcomeText = (TextView) findViewById(R.id.welcome_text);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
    }

    public void initAction(){
//        rootLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SplashActivity.this, WizardActivity.class);
//                SplashActivity.this.startActivity(intent);
//                SplashActivity.this.finish();
//            }
//        });
        String category = SPLASH_SCREEN_OPTION_3;
        setAnimation(category);
    }

    private void setAnimation(String category) {
        if (category.equals(SPLASH_SCREEN_OPTION_3)) {
            mKenBurns.setImageResource(R.drawable.utl);
            animation2();
            animation3();
        }
    }



    private void animation1() {
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(mLogo, "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(1200);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(mLogo, "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(1200);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mLogo, "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(1200);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.setStartDelay(500);
        animatorSet.start();
    }

    private void animation2() {
        mLogo.setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
        mLogo.startAnimation(anim);
    }

    private void animation3() {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(welcomeText, "alpha", 0.0F, 1.0F);
        alphaAnimation.setStartDelay(1700);
        alphaAnimation.setDuration(500);
        alphaAnimation.start();
    }


    public void initializeCatetory(){

        CategoryModel category = new CategoryModel();
        CategoryModel subCate = new CategoryModel();
        CategoryModel sSubcate = new CategoryModel();
        DatabaseReference mChildDatabase = mFirebase.mDatabase.child(AppConstants.TEST_CATEGORY).child("subCategory");

        category.setAll(0,"Cars","سيارة", "https://firebasestorage.googleapis.com/v0/b/bazararabia-e072a.appspot.com/o/category-image%2Fmain%2F0.png?alt=media&token=c081c0b0-6bfc-406d-88a2-57a9d2e3016b");
        category.subCategory = new ArrayList<>();

            subCate.setAll(0,"Any","سيارة" ,"");
            category.subCategory.add(0, subCate);

            subCate = new CategoryModel();
            subCate.setAll(1,"Cars For Sale","سيارة","");
            category.subCategory.add(1, subCate);
            category.subCategory.get(1).subCategory = new ArrayList<>();

                sSubcate.setAll(0,"Any","سيارة","");
                category.subCategory.get(1).subCategory.add(0, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(1,"Audi","سيارة","");
                category.subCategory.get(1).subCategory.add(1, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(2,"Lamborghini","سيارة","");
                category.subCategory.get(1).subCategory.add(2, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(3,"Jeep","سيارة","");
                category.subCategory.get(1).subCategory.add(3, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(4,"Benz","سيارة","");
                category.subCategory.get(1).subCategory.add(3, sSubcate);

            subCate = new CategoryModel();
            subCate.setAll(2,"Car Rental","سيارة" ,"");
            category.subCategory.add(2, subCate);
            category.subCategory.get(2).subCategory = new ArrayList<>();

                sSubcate = new CategoryModel();
                sSubcate.setAll(0,"Any","سيارة","");
                category.subCategory.get(2).subCategory.add(0, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(1,"Audi","سيارة","");
                category.subCategory.get(2).subCategory.add(1, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(2,"Lamborghini","سيارة","");
                category.subCategory.get(2).subCategory.add(2, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(3,"Jeep","سيارة","");
                category.subCategory.get(2).subCategory.add(3, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(4,"Benz","سيارة","");
                category.subCategory.get(2).subCategory.add(4, sSubcate);


            subCate = new CategoryModel();
            subCate.setAll(3,"Motorcycle", "","");
            category.subCategory.add(3, subCate);
            category.subCategory.get(3).subCategory = new ArrayList<>();

                sSubcate = new CategoryModel();
                sSubcate.setAll(0,"Any","سيارة","");
                category.subCategory.get(3).subCategory.add(0, sSubcate);

                sSubcate.setAll(1,"Honda","سيارة","");
                category.subCategory.get(3).subCategory.add(1, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(2,"Toyota","سيارة","");
                category.subCategory.get(3).subCategory.add(2, sSubcate);

            subCate = new CategoryModel();
            subCate.setAll(4,"Car Accessories", "","");
            category.subCategory.add(4, subCate);
            category.subCategory.get(4).subCategory = new ArrayList<>();



            subCate = new CategoryModel();
            subCate.setAll(5,"Wheels and Rims", "","");
            category.subCategory.add(5, subCate);
            category.subCategory.get(5).subCategory = new ArrayList<>();

                sSubcate = new CategoryModel();
                sSubcate.setAll(0,"Any","سيارة","");
                category.subCategory.get(5).subCategory.add(0, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(1,"14","سيارة","");
                category.subCategory.get(5).subCategory.add(1, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(2,"15","سيارة","");
                category.subCategory.get(5).subCategory.add(2, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(3,"16","سيارة","");
                category.subCategory.get(5).subCategory.add(3, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(4,"17","سيارة","");
                category.subCategory.get(5).subCategory.add(4, sSubcate);

        mChildDatabase.child("0").setValue(category);

        category = new CategoryModel();
        category.setAll(1,"Arts","سيارة", "https://firebasestorage.googleapis.com/v0/b/bazararabia-e072a.appspot.com/o/category-image%2Fmain%2F1.png?alt=media&token=1026521d-8f20-4d55-9711-c97c39f16cd4");
        mChildDatabase.child("1").setValue(category);

        category = new CategoryModel();
        category.setAll(2,"Electronics","سيارة", "https://firebasestorage.googleapis.com/v0/b/bazararabia-e072a.appspot.com/o/category-image%2Fmain%2F2.png?alt=media&token=da31b804-86c9-4dbe-9f88-e98e759b9d2b");
        category.subCategory = new ArrayList<>();


            subCate = new CategoryModel();
            subCate.setAll(0,"Any", "","");
            category.subCategory.add(0, subCate);
            category.subCategory.get(0).subCategory = new ArrayList<>();

            subCate = new CategoryModel();
            subCate.setAll(1,"Computers", "","");
            category.subCategory.add(1, subCate);
            category.subCategory.get(1).subCategory = new ArrayList<>();

                sSubcate = new CategoryModel();
                sSubcate.setAll(0,"Any","سيارة","");
                category.subCategory.get(1).subCategory.add(0, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(1,"Dell","سيارة","");
                category.subCategory.get(1).subCategory.add(1, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(2,"Sony","سيارة","");
                category.subCategory.get(1).subCategory.add(2, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(3,"Mac","سيارة","");
                category.subCategory.get(1).subCategory.add(3, sSubcate);


            subCate = new CategoryModel();
            subCate.setAll(2,"TVs", "","");
            category.subCategory.add(2, subCate);
            category.subCategory.get(2).subCategory = new ArrayList<>();

                sSubcate = new CategoryModel();
                sSubcate.setAll(0,"Any","سيارة","");
                category.subCategory.get(2).subCategory.add(0, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(1,"Panasonic","سيارة","");
                category.subCategory.get(2).subCategory.add(1, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(2,"Shivaki","سيارة","");
                category.subCategory.get(2).subCategory.add(2, sSubcate);

                sSubcate = new CategoryModel();
                sSubcate.setAll(3,"Haier","سيارة","");
                category.subCategory.get(2).subCategory.add(3, sSubcate);



        mChildDatabase.child("2").setValue(category);

        category = new CategoryModel();
        category.setAll(3,"Food","سيارة", "https://firebasestorage.googleapis.com/v0/b/bazararabia-e072a.appspot.com/o/category-image%2Fmain%2F3.png?alt=media&token=2f65a57b-2fff-4eb0-bb1f-05f7b29bf7c2");
        category.subCategory = new ArrayList<>();

            subCate = new CategoryModel();
            subCate.setAll(0,"Any", "","");
            category.subCategory.add(0, subCate);

            subCate = new CategoryModel();
            subCate.setAll(1,"Oil", "","");
            category.subCategory.add(1, subCate);

            subCate = new CategoryModel();
            subCate.setAll(2,"Dates", "","");
            category.subCategory.add(2, subCate);

            subCate = new CategoryModel();
            subCate.setAll(3,"Honey", "","");
            category.subCategory.add(3, subCate);

            subCate = new CategoryModel();
            subCate.setAll(4,"Ready Cooked Meals","","");
            category.subCategory.add(4, subCate);

            subCate = new CategoryModel();
            subCate.setAll(5,"Desserts","","");
            category.subCategory.add(5, subCate);

        mChildDatabase.child("3").setValue(category);

        category = new CategoryModel();
        category.setAll(4,"Health","سيارة", "https://firebasestorage.googleapis.com/v0/b/bazararabia-e072a.appspot.com/o/category-image%2Fmain%2F4.png?alt=media&token=baac2bb7-8744-4bfe-b95c-211f5888359f");
        mChildDatabase.child("4").setValue(category);

        category = new CategoryModel();
        category.setAll(5,"Travel","سيارة", "https://firebasestorage.googleapis.com/v0/b/bazararabia-e072a.appspot.com/o/category-image%2Fmain%2F5.png?alt=media&token=f7946577-5941-4fb9-abce-615d68dec65b");
        mChildDatabase.child("5").setValue(category);


    }
}
