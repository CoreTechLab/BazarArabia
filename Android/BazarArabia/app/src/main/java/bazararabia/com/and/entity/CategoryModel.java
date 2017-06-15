package bazararabia.com.and.entity;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CategoryModel implements Serializable {
    public int id;
    public String categoryNameEn;
    public String categoryNameAr;
    public String categoryImage;
    public ArrayList<CategoryModel> subCategory = null;
    public CategoryModel() {

    }

    public CategoryModel(int id, String categoryNameEn , String categoryNameAr) {
        this.id = id;
        this.categoryNameEn = categoryNameEn;
        this.categoryNameAr = categoryNameAr;
    }

    public void setAll(int id, String categoryNameEn , String categoryNameAr, String categoryImage) {
        this.id = id;
        this.categoryNameEn = categoryNameEn;
        this.categoryNameAr = categoryNameAr;
        this.categoryImage = categoryImage;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }


    public void setCategoryNameEn(String categoryNameEn){
        this.categoryNameEn = categoryNameEn;
    }
    public String getCategoryNameEn(){return this.categoryNameEn;}

    public void setCategoryNameAr(String categoryNameAr){
        this.categoryNameAr = categoryNameAr;
    }
    public String getCategoryNameAr(){return this.categoryNameAr;}

    public void setCategoryImage(String categoryImage){this.categoryImage = categoryImage;}
    public String getCategoryImage(){return this.categoryImage;}

//    public void setSubCategoryId(int id){
//        this.subCategory.id = id;
//    }
//
//    public void setSubCategoryNameEn(String categoryNameEn){
//        this.subCategory.categoryNameEn = categoryNameEn;
//    }
//
//    public void setSubCategoryNameAr(String categoryNameAr){
//        this.subCategory.categoryNameEn = categoryNameEn;
//    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("categoryNameEn", categoryNameEn);
        result.put("categoryNameAr", categoryNameAr);
        return result;
    }
}