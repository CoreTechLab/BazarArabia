package bazararabia.com.and.config;

import android.app.Application;

import bazararabia.com.and.entity.CategoryModel;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    private CategoryModel gCategoryList;

    public static MyApplication getInstance(){return mInstance;}

    @Override
    public void onCreate() {
        super.onCreate();
        this.gCategoryList = new CategoryModel();
        mInstance = this;
    }

    public void setCategoryList(CategoryModel cate){
        this.gCategoryList = cate;
    }

    public CategoryModel getCategoryList(){
        return gCategoryList;
    }
}
