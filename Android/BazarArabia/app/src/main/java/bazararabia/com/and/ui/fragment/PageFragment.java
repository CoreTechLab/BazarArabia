package bazararabia.com.and.ui.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import bazararabia.com.and.config.AppConstants;

public class PageFragment extends MainFragment {

    public PageFragment()
    {
        super();
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query myTopPostsQuery = databaseReference.child(AppConstants.TEST_CATEGORY_POSTS_TABLE).child(String.valueOf(category.id))
                .orderByChild("startCount");
        // [END my_top_posts_query]

        return myTopPostsQuery;
    }
}