package bazararabia.com.and.ui.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

import bazararabia.com.and.R;
import bazararabia.com.and.util.ImageUtil;


public class CustomTabView extends LinearLayout {
    public ImageView tabImage;
    public TextView tabText;
    public CustomTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_tab_view, this, true);

        tabImage = (ImageView) findViewById(R.id.tabImage);
        tabText =(TextView) findViewById(R.id.tabText);


    }

    public CustomTabView(Context context) {
        this(context, null);
    }

    public void setTabImageURl(String url){
        ImageUtil.displayImage(tabImage,url, 0, null);
    }

    public void setTabText(String text){
        tabText.setText(text);
    }
}
