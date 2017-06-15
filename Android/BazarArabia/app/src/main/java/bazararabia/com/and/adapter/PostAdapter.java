package bazararabia.com.and.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import bazararabia.com.and.R;
import bazararabia.com.and.entity.PostModel;
import bazararabia.com.and.ui.activity.DetailPostAdsActivity;
import bazararabia.com.and.ui.activity.MainActivity;
import bazararabia.com.and.util.ImageUtil;

public class PostAdapter extends ArrayAdapter<PostModel> {

    private LayoutInflater mInflater;
    private Context context;

    List<PostModel> postList;
    public PostAdapter(Context context, List<PostModel> postList){
        super(context, 0, postList);
        this.context = context;
        this.postList = postList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.postPersonImage = (ImageView) convertView.findViewById(R.id.item_person_image);
            holder.postImage = (ImageView) convertView.findViewById(R.id.item_post_image);

            holder.postUserName = (TextView) convertView.findViewById(R.id.item_person_name);
            holder.postTitle = (TextView) convertView.findViewById(R.id.item_post_title);
            holder.postDescription = (TextView) convertView.findViewById(R.id.item_post_description);
            holder.like = (TextView) convertView.findViewById(R.id.item_post_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        PostModel post = getItem(position);
        ImageUtil.displayRoundImage(holder.postPersonImage, post.thumbPath.get(0), null);
        ImageUtil.displayImage(holder.postImage, post.thumbPath.get(0), null);
        holder.postUserName.setText("@" + "Jone doe");
        holder.postTitle.setText(post.title);
        holder.postDescription.setText(post.description);
        holder.like.setTag(position);
        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context, DetailPostAdsActivity.class);
                intent.putExtra("postitem" , postList.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder{

        public ImageView postPersonImage;
        public ImageView postImage;
        public TextView postUserName;
        public TextView postTitle;
        public TextView postDescription;
        public TextView like;
    }

}
