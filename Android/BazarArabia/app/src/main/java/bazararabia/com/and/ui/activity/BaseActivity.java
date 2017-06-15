package bazararabia.com.and.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import bazararabia.com.and.R;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public abstract void initView();


    public void showErrorMessage(String title, String message , DialogInterface.OnClickListener doneClickListener) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton( "Done", doneClickListener)
                .show();
    }
    public void showToastMessage(String message ,int toast_length) {
        Toast.makeText(this , message , toast_length).show();
    }

    public Toolbar initToolBar(Toolbar toolbar,String title){
        toolbar.setTitle(title);

        return toolbar;
    }
}
