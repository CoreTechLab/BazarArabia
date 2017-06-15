package bazararabia.com.and.ui.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import bazararabia.com.and.R;
import bazararabia.com.and.adapter.PostAdapter;
import bazararabia.com.and.config.AppConstants;
import bazararabia.com.and.entity.CategoryModel;
import bazararabia.com.and.entity.PostModel;
import bazararabia.com.and.ui.activity.MainActivity;
import bazararabia.com.and.util.FirebaseClass;

public class MainFragment extends BaseFragment {

    CategoryModel category;
    ListView listView;
    private RelativeLayout relativeLayoutHomef;
    private SwipeRefreshLayout swipeRefreshlHomeFragment;

    private View resView;
    private static final int INITIAL_DELAY_MILLIS = 300;

    private PostAdapter mPostAdapter;
    private List<PostModel> postList=new ArrayList<>();
    private FirebaseClass mFirebase = new FirebaseClass();



    public MainFragment(){};

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        try {
            this.category = (CategoryModel) this.getArguments().getSerializable("category");
        }catch(Exception e)
        {
            e.printStackTrace();
        }


        ImageLoader loader = ImageLoader.getInstance();
        if (!loader.isInited()) {
            loader.init(ImageLoaderConfiguration.createDefault((MainActivity)getContainer()));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        resView = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        initAction();
        return resView;
    }

    public void initView(){
        relativeLayoutHomef = (RelativeLayout) this.resView.findViewById(R.id.relative_layout_home_f);
        swipeRefreshlHomeFragment = (SwipeRefreshLayout) this.resView.findViewById(R.id.swipe_refreshl_home_fragment);
        swipeRefreshlHomeFragment.setRefreshing(true);
        listView = (ListView)  this.resView.findViewById(R.id.list_view);
        getPosts();
    }

    public void initAction(){
        this.swipeRefreshlHomeFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                getPosts();
            }
        });
    }

    public void getPosts(){
        swipeRefreshlHomeFragment.setRefreshing(true);
        final Query mPostQueryRef = getQuery(mFirebase.mDatabase);
        mPostQueryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                //mPostAdapter.notifyDataSetChanged();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    GenericTypeIndicator<PostModel> genericTypeIndicator = new GenericTypeIndicator<PostModel>() {};
                    PostModel mPost = postSnapshot.getValue(genericTypeIndicator);
                    postList.add(mPost);
                }
                mPostAdapter = new PostAdapter((MainActivity)getContainer(), postList);
                mPostQueryRef.removeEventListener(this);

                setListViewSettings();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                swipeRefreshlHomeFragment.setRefreshing(false);
            }
        });
    }

    public void setListViewSettings(){

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                mPostAdapter);
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;

        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setClipToPadding(false);
        listView.setDivider(null);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, r.getDisplayMetrics());
        listView.setDividerHeight(px);
        listView.setFadingEdgeLength(0);
        listView.setFitsSystemWindows(true);
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                r.getDisplayMetrics());
        listView.setPadding(px, px, px, px);
        listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);

        swipeRefreshlHomeFragment.setRefreshing(false);
        listView.setAdapter(swingBottomInAnimationAdapter);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query myTopPostsQuery = databaseReference.child(AppConstants.TEST_POSTS_TABLE)
                .orderByChild("starCount");
        // [END my_top_posts_query]

        return myTopPostsQuery;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPosts();
    }
}
