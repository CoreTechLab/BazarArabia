package bazararabia.com.and.ui.activity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bazararabia.com.and.R;
import bazararabia.com.and.adapter.ImageAdapter;
import bazararabia.com.and.config.AppConstants;
import bazararabia.com.and.config.MyApplication;
import bazararabia.com.and.entity.CategoryModel;
import bazararabia.com.and.entity.PostModel;
import bazararabia.com.and.util.FirebaseClass;
import bazararabia.com.and.util.ImagePicker;
import bazararabia.com.and.util.UriConverter;


public class NewPostActivity extends BaseActivity {

    RecyclerView imageRecycleView;
    RecyclerView.Adapter imageAdapter;

    ListView categoryListView;

    List<String> imageList = new ArrayList<>();
    List<String> uploadImageList = new ArrayList<>();

    Button btnNewPost;
    EditText postInputTitle;
    TextInputLayout postInputTitleLayout;
    EditText postInputDescription;
    TextInputLayout postInputDescriptionLayout;

    Spinner spinnerPostCategory;

    FirebaseClass mFirebase = new FirebaseClass();

    Toolbar toolbar;

    private final int ACTION_IMAGE_PICK = 990;
    private ArrayAdapter<String> spinnerDataAdapter;
    private ArrayList<CategoryModel> categoryList = new ArrayList<>();
    private CategoryModel selectedCategory = null;

    private final int MY_PERMISSION_REQUEST_CAMERA= 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_post);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        initToolBar(toolbar, "New Post");
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //imageList
        btnNewPost = (Button) findViewById(R.id.post_btn);
        postInputTitle = (EditText) findViewById(R.id.post_input_title);
        postInputTitleLayout = (TextInputLayout)findViewById(R.id.post_input_layout_title);
        postInputDescription = (EditText) findViewById(R.id.post_input_description);
        postInputDescriptionLayout = (TextInputLayout)findViewById(R.id.post_input_layout_description);

        categoryListView = (ListView) findViewById(R.id.category_list_view);
        imageList.clear();
        imageList.add("");
        imageRecycleView = (RecyclerView) findViewById(R.id.post_image_list_view);

        LinearLayoutManager myLayoutManager = new LinearLayoutManager(NewPostActivity.this);
        myLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        imageAdapter = new ImageAdapter(imageList, NewPostActivity.this);
        imageRecycleView.setAdapter(imageAdapter);
        imageRecycleView.setLayoutManager(myLayoutManager);

        btnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });



        //categoryList
        categoryList = MyApplication.getInstance().getCategoryList().subCategory;
        spinnerPostCategory = (Spinner)findViewById(R.id.post_category);
        final ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<categoryList.size();i++){
            list.add(categoryList.get(i).categoryNameEn);
        }
        spinnerDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        if(spinnerDataAdapter != null) {
            spinnerDataAdapter.notifyDataSetChanged();
            //set default category
            spinnerPostCategory.setSelection(0);
            selectedCategory = categoryList.get(0);
        }
        spinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPostCategory.setAdapter(spinnerDataAdapter);
        spinnerPostCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(categoryList.size() > spinnerPostCategory.getSelectedItemPosition()) {
                    selectedCategory = categoryList.get(spinnerPostCategory.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null;
            }

        });

    }

    private void submitForm(){
        if (!validateTitle()) {
            return;
        }
        if (!validateDescription()) {
            return;
        }
        sendImagesToServer();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateTitle() {
        if (postInputTitle.getText().toString().trim().isEmpty() || postInputTitle.getText().length()  < 3 ) {
            postInputTitleLayout.setError(getString(R.string.error_short_value));
            requestFocus(postInputTitle);
            return false;
        } else {
            postInputTitleLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDescription() {
        if (postInputDescription.getText().toString().trim().isEmpty() || postInputDescription.getText().length()  < 3 ) {
            postInputDescriptionLayout.setError(getString(R.string.error_short_value));
            requestFocus(postInputDescription);
            return false;
        } else {
            postInputDescriptionLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void sendImagesToServer(){
        final String key = mFirebase.mDatabase.child(AppConstants.TEST_POSTS_TABLE).push().getKey();
        uploadImageList.clear();
        for(int i=1;i < imageList.size();i++){
            if(imageList.get(i) == null || imageList.get(i).equalsIgnoreCase(""))
                return;

            Uri file = Uri.fromFile(new File(imageList.get(i)));
            SimpleDateFormat dateFormat = new   SimpleDateFormat("yyyyMMdd'T'HHmmss");
            final String timeStamp = dateFormat.format(new Date());
            String imageFileName = "picture_" + timeStamp + String.valueOf(i) + ".jpg";
            StorageReference imagesRef = mFirebase.mStorageRef.child("test").child(key).child(imageFileName);
            imagesRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public synchronized void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        uploadImageList.add(downloadUrl.toString());
                        if (uploadImageList.size() == imageList.size()-1){
                            postAdsToServer(key);
                        }
                    }
                });
        }
    }

    private void postAdsToServer(String key){
        PostModel post = new PostModel("",
                postInputTitle.getText().toString().trim(),
                postInputDescription.getText().toString().trim(),
                uploadImageList,
                selectedCategory.getId(),
                uploadImageList
        );

        post.postId = key;
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+AppConstants.TEST_POSTS_TABLE + "/" + key, postValues);
        childUpdates.put("/"+AppConstants.TEST_CATEGORY_POSTS_TABLE + "/" + selectedCategory.getId() + "/" + key, postValues);
        mFirebase.mDatabase.updateChildren(childUpdates);
        finish();
    }


    @Override
    public void initView() {

    }

    private boolean checkCameraPermission(){
        //check permission
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(NewPostActivity.this,
                Manifest.permission.CAMERA);
        if(cameraPermissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(NewPostActivity.this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(NewPostActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSION_REQUEST_CAMERA);
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(NewPostActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSION_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else
        {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    ImportPostImages();

                } else {
                    showErrorMessage("Please Allow Camera Permission",
                            "This app requires camera permission for perfect performance.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NewPostActivity.this.finish();
                                }
                            }
                    );
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
        }
    }

    public void onClickImportImages()
    {
        boolean isPermissionAllowed = checkCameraPermission();
        if (isPermissionAllowed){
            ImportPostImages();
        }
    }

    private void ImportPostImages(){
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this , true);
        startActivityForResult(chooseImageIntent, ACTION_IMAGE_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            switch (requestCode) {

                case ACTION_IMAGE_PICK:
                    List<String> loadedImagesFilePaths = ImagePicker.getMultiImagesFullPath(this, resultCode, data);
                    for(int i=0;i<loadedImagesFilePaths.size(); i++) {
                        imageList.add(loadedImagesFilePaths.get(i));
                    }
                    imageAdapter = new ImageAdapter(imageList, NewPostActivity.this);
                    imageRecycleView.setAdapter(imageAdapter);
                    break;
            }
        }
    }


}
