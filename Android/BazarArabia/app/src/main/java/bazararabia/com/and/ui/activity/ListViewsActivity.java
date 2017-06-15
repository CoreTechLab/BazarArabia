package bazararabia.com.and.ui.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import bazararabia.com.and.R;
import bazararabia.com.and.adapter.PostAdapter;
import bazararabia.com.and.entity.PostModel;
import bazararabia.com.and.util.FirebaseClass;

public class ListViewsActivity extends BaseActivity implements OnDismissCallback {

    private static final int INITIAL_DELAY_MILLIS = 300;

    private PostAdapter mPostAdapter;
    private List<PostModel> postList=new ArrayList<>();

    private FirebaseClass mFirebase = new FirebaseClass();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        getPosts();

        ImageLoader loader = ImageLoader.getInstance();
        if (!loader.isInited()) {
            loader.init(ImageLoaderConfiguration.createDefault(this));
        }
    }


    public void getPosts(){
        final Query mPostQueryRef = mFirebase.getQuery(mFirebase.mDatabase);
        mPostQueryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                //mPostAdapter.notifyDataSetChanged();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    GenericTypeIndicator<PostModel> genericTypeIndicator = new GenericTypeIndicator<PostModel>() {

                    };
                    PostModel mPost = postSnapshot.getValue(genericTypeIndicator);
                    postList.add(mPost);
                }
                mPostAdapter = new PostAdapter(ListViewsActivity.this, postList);
                mPostQueryRef.removeEventListener(this);
                setListViewSettings();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setListViewSettings(){

        ListView listView = (ListView) findViewById(R.id.list_view);

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
        listView.setAdapter(mPostAdapter);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void initView() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDismiss(@NonNull ViewGroup listView, @NonNull int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mPostAdapter.remove(mPostAdapter.getItem(position));
        }
    }
}
