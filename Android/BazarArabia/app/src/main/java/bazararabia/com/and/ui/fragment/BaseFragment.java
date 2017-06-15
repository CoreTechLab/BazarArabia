package bazararabia.com.and.ui.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import bazararabia.com.and.ui.activity.BaseActivity;

public abstract class BaseFragment extends Fragment {

    private BaseActivity m_Activity;

    protected BaseActivity getContainer() {
        return m_Activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BaseActivity) {
            m_Activity = (BaseActivity) context;
        }
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

}
