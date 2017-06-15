package bazararabia.com.and.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import bazararabia.com.and.R;
import bazararabia.com.and.adapter.CategoryListAdapter;
import bazararabia.com.and.config.MyApplication;
import bazararabia.com.and.entity.CategoryModel;
import bazararabia.com.and.ui.activity.CategoryListActivity;
import bazararabia.com.and.ui.activity.NewPostActivity;

public class CategoryFragment extends BaseFragment {

    View resView;
    ListView category_list_view;
    CategoryListAdapter categoryList_Adapter;
    private ArrayList<CategoryModel> categoryList = new ArrayList<>();


    public CategoryFragment(){};

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        categoryList = MyApplication.getInstance().getCategoryList().subCategory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        resView = inflater.inflate(R.layout.fragment_category, container, false);
        category_list_view = (ListView) resView.findViewById(R.id.category_list_view);

        categoryList_Adapter = new CategoryListAdapter((CategoryListActivity) getContainer(), categoryList);
        category_list_view.setAdapter(categoryList_Adapter);
        category_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return resView;
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return null;
    }
}
