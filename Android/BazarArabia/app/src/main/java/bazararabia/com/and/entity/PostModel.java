package bazararabia.com.and.entity;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PostModel implements Serializable {

    public String uid;
    public String postId;
    public String title;
    public String description;
    public List<String> imagePath;
    public int categoryId;
    public List<String> thumbPath;
    @Exclude
    public String categoryNameEn;
    @Exclude
    public String categoryNameAr;


    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public PostModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        this.imagePath = new ArrayList<>();
        this.thumbPath = new ArrayList<>();
    }

    public PostModel(String uid, String title, String description, List<String> imagePath, int categoryId, List<String> thumbPath) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.categoryId = categoryId;
        this.thumbPath = thumbPath;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("postId" , postId);
        result.put("title", title);
        result.put("description", description);
        result.put("imagePath", imagePath );
        result.put("categoryId",categoryId);
        result.put("thumbPath", thumbPath );
        result.put("starCount", starCount);
        result.put("stars", stars);
        return result;
    }
}

//public class PostModel {
//    public String userId;
//    public String postId;
//    public String postTitle;
//    public String postDescription;
//    public String postImagePath;
//    public int categoryId;
//    public String postThumbPath;
//    @Exclude
//    public String categoryNameEn;
//    @Exclude
//    public String categoryNameAr;
//    public int starCount = 0;
//    public Map<String, Boolean> stars = new HashMap<>();
//
//    public PostModel(){}
//    public PostModel(String userId, String postTitle, String postDescription, String postImagePath, int categoryId) {
//        this.userId = userId;
//        this.postTitle = postTitle;
//        this.postDescription = postDescription;
//        this.postImagePath = postImagePath;
//        this.categoryId = categoryId;
//    }
//
//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> res = new HashMap<>();
//        res.put("userId", userId);
//        res.put("postId" , postId);
//        res.put("postTitle", postTitle);
//        res.put("postDescription", postDescription);
//        res.put("postImagePath", postImagePath );
//        res.put("categoryId",categoryId);
//        res.put("starCount", starCount);
//        res.put("stars", stars);
//        return res;
//    }
//}
