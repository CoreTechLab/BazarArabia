package bazararabia.com.and.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import bazararabia.com.and.config.AppConstants;

public class FirebaseClass {
    public FirebaseAuth mAuth;
    public DatabaseReference mDatabase;
    public StorageReference mStorageRef;
    public FirebaseAuth.AuthStateListener mAuthListener;

    public FirebaseClass(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://bazararabia-e072a.appspot.com");
    }

    public Query getQuery(DatabaseReference databaseReference) {
        Query myTopPostsQuery = databaseReference.child(AppConstants.POSTS_TABLE)
                .orderByChild("starCount");
        return myTopPostsQuery;
    }

    public String getCurrentUserId(){
        if(mAuth!=null)
            return mAuth.getCurrentUser().getUid();
        return null;
    }
}
