package bazararabia.com.and.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import bazararabia.com.and.R;
import bazararabia.com.and.config.MySharedPreferenceManager;
import bazararabia.com.and.ui.fragment.WizardFragment;

public class WizardActivity extends BaseActivity {

    private MyPagerAdapter adapter;
    private ViewPager pager;
    private TextView nextButton;
    private TextView skipButton;
    private TextView navigator;
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        currentItem = 0;

        pager = (ViewPager) findViewById(R.id.activity_wizard_pager);
        skipButton = (TextView) findViewById(R.id.activity_wizard_skip);
        nextButton = (TextView) findViewById(R.id.activity_wizard_next);
        navigator = (TextView) findViewById(R.id.activity_wizard_possition);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(currentItem);

        setNavigator();

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                setNavigator();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySharedPreferenceManager sf= new MySharedPreferenceManager(WizardActivity.this);
                sf.setValueBoolean("isFirstLaunch" , false);
                Intent intent = new Intent(WizardActivity.this, MainActivity.class);
                WizardActivity.this.startActivity(intent);
                WizardActivity.this.finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() != (pager.getAdapter().getCount()-1)){
                    pager.setCurrentItem(pager.getCurrentItem()+1);
                }
                else{
                    MySharedPreferenceManager sf= new MySharedPreferenceManager(WizardActivity.this);
                    sf.setValueBoolean("isFirstLaunch" , false);
                    Intent intent = new Intent(WizardActivity.this, MainActivity.class);
                    WizardActivity.this.startActivity(intent);
                    WizardActivity.this.finish();
                }

            }
        });
    }

    public void setNavigator(){
        String navigation = "";
        for (int i = 0; i < adapter.getCount(); i++) {
            if (i == pager.getCurrentItem()) {
                navigation += getString(R.string.material_icon_point_full)
                        + "  ";
            } else {
                navigation += getString(R.string.material_icon_point_empty)
                        + "  ";
            }
        }
        navigator.setText(navigation);
    }

    @Override
    public void initView() {

    }
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return WizardFragment.newInstance(position);
            } else if (position == 1) {
                return WizardFragment.newInstance(position);
            } else {
                return WizardFragment.newInstance(position);
            }
        }
    }
}
