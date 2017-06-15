package bazararabia.com.and.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import bazararabia.com.and.R;
import bazararabia.com.and.adapter.DetailedPostImageAdapter;
import bazararabia.com.and.entity.PostModel;
import bazararabia.com.and.ui.activity.DetailPostAdsActivity;

public class DetailPostFragment extends BaseFragment {

    private ViewPager mDetailedPostImageViewPager;
    private DetailedPostImageAdapter imageAdapter;
    private PostModel postInfo;
    private View resView;
    private TextView mDetailTitleText;
    private TextView mDetailDescriptionText;

    public DetailPostFragment(PostModel postInfo){
        this.postInfo = postInfo;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resView = inflater.inflate(R.layout.fragment_detail_post, container, false);
        initView();

        return resView;
    }

    public void initView() {

        mDetailedPostImageViewPager = (ViewPager) resView.findViewById(R.id.detailed_post_image_viewpager);
        mDetailTitleText = (TextView) resView.findViewById(R.id.detail_title_text);
        mDetailDescriptionText =(TextView)resView.findViewById(R.id.detail_description_text);

        mDetailTitleText.setText(postInfo.title);
        mDetailDescriptionText.setText(postInfo.description);

        imageAdapter = new DetailedPostImageAdapter((DetailPostAdsActivity) getContainer() , postInfo.imagePath);
        mDetailedPostImageViewPager.setAdapter(imageAdapter);
    }


    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return null;
    }
}
