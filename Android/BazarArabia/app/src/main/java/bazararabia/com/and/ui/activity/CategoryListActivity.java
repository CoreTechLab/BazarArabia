package bazararabia.com.and.ui.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


import java.util.ArrayList;
import java.util.List;

import bazararabia.com.and.R;
import bazararabia.com.and.config.MyApplication;
import bazararabia.com.and.entity.CategoryModel;
import bazararabia.com.and.ui.fragment.CategoryFragment;
import bazararabia.com.and.ui.fragment.PageFragment;

public class CategoryListActivity extends BaseActivity {


    private ViewPager categoryListViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    private ArrayList<CategoryModel> categoryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list_view);

        initView();

        CategoryFragment categoryFragment = new CategoryFragment();
        viewPagerAdapter.addFragment(categoryFragment, "Category");

        viewPagerAdapter.notifyDataSetChanged();
        categoryListViewPager.setAdapter(viewPagerAdapter);
    }




    @Override
    public void initView() {

        categoryListViewPager = (ViewPager)findViewById(R.id.category_list_view_pager);
        viewPagerAdapter = new CategoryListActivity.ViewPagerAdapter(getSupportFragmentManager());

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
