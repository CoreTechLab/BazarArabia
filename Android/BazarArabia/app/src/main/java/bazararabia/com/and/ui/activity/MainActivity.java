package bazararabia.com.and.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import bazararabia.com.and.R;
import bazararabia.com.and.config.AppConstants;
import bazararabia.com.and.config.MyApplication;
import bazararabia.com.and.entity.CategoryModel;
import bazararabia.com.and.entity.UserModel;
import bazararabia.com.and.ui.fragment.MainFragment;
import bazararabia.com.and.ui.fragment.PageFragment;
import bazararabia.com.and.util.FirebaseClass;
import bazararabia.com.and.util.ImageUtil;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager main_view_pager;
    private ViewPagerAdapter view_pager_adapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    private ArrayList<CategoryModel> categoryList = new ArrayList<>();
    private MainFragment mainFragment;
    private Toolbar toolbar;
    private ImageView navigationProfilePhoto;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private Button mRegisterButton;
    private Button mSigninButton;
    private FirebaseClass mFirebase;
    private TextView headerTextViewName;
    private TextView headerTextViewEmail;
    private LinearLayout headerSignInUpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebase = new FirebaseClass();
        initAuthSetting();

        categoryList = MyApplication.getInstance().getCategoryList().subCategory;

        ImageLoader loader = ImageLoader.getInstance();
        if (!loader.isInited()) {
            loader.init(ImageLoaderConfiguration.createDefault(this));
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        initToolBar(toolbar,"BazarArabia");

        setSupportActionBar(toolbar);

        initView();

        for(int i=0;i<categoryList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("category", categoryList.get(i));
            PageFragment pageFragment = new PageFragment();
            pageFragment.setArguments(bundle);

            view_pager_adapter.addFragment(pageFragment, categoryList.get(i).categoryNameEn);
        }
        view_pager_adapter.notifyDataSetChanged();
        main_view_pager.setAdapter(view_pager_adapter);

        tabLayout.setupWithViewPager(main_view_pager);

        for(int i=0;i<categoryList.size(); i++) {
            CustomTabView tabView = new CustomTabView(MainActivity.this);
            tabView.setTabText(categoryList.get(i).categoryNameEn);
            tabView.setTabImageURl(categoryList.get(i).categoryImage);
            tabLayout.getTabAt(i).setCustomView(tabView);
        }
        tabLayout.refreshDrawableState();

        if(mFirebase.mAuth.getCurrentUser() != null){

        }else{
            headerSignInUpLayout.setVisibility(View.VISIBLE);}
    }

    private void initAuthSetting(){
        mFirebase.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    MainActivity.this.finish();
                }else{}
            }
        };


        if(mFirebase.mAuth.getCurrentUser() != null)
        {
            String userId = mFirebase.getCurrentUserId();
            final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(AppConstants.USERS_TABLE).child(userId);
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get user information
                    UserModel userdata = dataSnapshot.getValue(UserModel.class);
                    if (userdata!=null){
                        headerTextViewName.setText(userdata.getFirstName()+" "+userdata.getLastName());
                        headerTextViewEmail.setText(userdata.getEmail());
                        showOrHideItem();
                    }
                    db.removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
        }
    }


    @Override
    public void initView() {
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        main_view_pager = (ViewPager)findViewById(R.id.main_view_pager);
        view_pager_adapter = new ViewPagerAdapter(getSupportFragmentManager());


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        //Get profile_photo widget
        mNavigationView = (NavigationView) findViewById(R.id.main_navigation_view);

        mNavigationView.setNavigationItemSelectedListener(this);

        View headerView = mNavigationView.getHeaderView(0);
        navigationProfilePhoto = (ImageView)headerView.findViewById(R.id.navigation_profile_photo);
        mRegisterButton = (Button)headerView.findViewById(R.id.btnSignUp);
        mSigninButton = (Button)headerView.findViewById(R.id.btnSignInOut);
        headerTextViewName = (TextView)headerView.findViewById(R.id.navigation_profile_name);
        headerTextViewEmail = (TextView)headerView.findViewById(R.id.navigation_profile_email);


        headerSignInUpLayout = (LinearLayout) headerView.findViewById(R.id.header_button_layout);

        ImageUtil.displayRoundImage(navigationProfilePhoto,"http://pengaja.com/uiapptemplate/newphotos/profileimages/0.jpg", null);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);



        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterSetupActivity.class);
                startActivity(intent);
            }
        });

        mSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        showOrHideItem();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_right_top_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }






    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.home_action:
                //main_view_pager.setCurrentItem(0);
                break;
            case R.id.post_action:
                startActivity(new Intent(MainActivity.this, NewPostActivity.class));
                break;
            case R.id.logout_action:
                if(mFirebase.mAuth.getCurrentUser() != null) {
                    mFirebase.mAuth.signOut();
                }
                showOrHideItem();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showOrHideItem()
    {
        Menu nav_Menu = mNavigationView.getMenu();
        if (mFirebase.mAuth.getCurrentUser() == null)
        {
            nav_Menu.findItem(R.id.post_action).setVisible(false);
            nav_Menu.findItem(R.id.logout_action).setVisible(false);
            headerSignInUpLayout.setVisibility(View.VISIBLE);
            headerTextViewName.setText("No subscriber");
            headerTextViewEmail.setText("xxx@xx.xxx");}
        else{
            headerSignInUpLayout.setVisibility(View.GONE);
            nav_Menu.findItem(R.id.post_action).setVisible(true);
            nav_Menu.findItem(R.id.logout_action).setVisible(true);
        }
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
