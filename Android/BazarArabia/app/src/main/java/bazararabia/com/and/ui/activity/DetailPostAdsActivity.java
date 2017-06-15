package bazararabia.com.and.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import bazararabia.com.and.R;
import bazararabia.com.and.adapter.DetailedPostImageAdapter;
import bazararabia.com.and.entity.PostModel;
import bazararabia.com.and.ui.fragment.DetailPostFragment;

public class DetailPostAdsActivity extends BaseActivity implements View.OnClickListener{

    private PostModel postInfo;
    private DetailPostFragment detailPostFragment;
    private Toolbar toolbar;
    private FrameLayout fragmentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getExtras();

        initToolBar(toolbar, postInfo.title);
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


        fragmentLayout = (FrameLayout)findViewById(R.id.fragmentLayout);
        detailPostFragment = new DetailPostFragment(postInfo);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentLayout , detailPostFragment)
       .commit();



        initView();
    }




    @Override
    public void initView() {
    }

    private void getExtras()
    {
        postInfo = (PostModel) this.getIntent().getSerializableExtra("postitem");
    }

    @Override
    public void onClick(View v) {

    }
}
