package bazararabia.com.and.adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

import bazararabia.com.and.R;
import bazararabia.com.and.ui.activity.NewPostActivity;
import bazararabia.com.and.util.ImageUtil;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> imageList;
    private Context context;

    private NewPostActivity parentActivity;

    public ImageAdapter(List<String> imageList, Context context){
        this.imageList = imageList;
        this.context = context;

        parentActivity = (NewPostActivity)context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(context).inflate(R.layout.custom_new_post_image_view, parent, false);
       // viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        ViewHolder view = new ViewHolder(viewHolder);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == 0) {
            holder.rootCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position == 0)
                        parentActivity.onClickImportImages();
                }
            });
            holder.postImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position == 0)
                        parentActivity.onClickImportImages();
                }
            });
            holder.postImageButton.setVisibility(View.VISIBLE);
            holder.postImageClose.setVisibility(View.GONE);
        }
        else{
            ImageUtil.displayImage(holder.postImage, imageList.get(position), null);
            //holder.postImage.setImageURI(Uri.fromFile(new File(imageList.get(position))));
            holder.postImageButton.setVisibility(View.GONE);
            holder.postImageClose.setVisibility(View.VISIBLE);
            holder.postImageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position != 0) {
                        imageList.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });
        }

        holder.setIsRecyclable(false);
    }



    @Override
    public int getItemCount() {
        return imageList==null?0:imageList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CardView rootCardView;
        public ImageView postImageButton;
        public ImageView postImage;
        public ImageButton postImageClose;
        public ViewHolder(View itemView) {
            super(itemView);
            rootCardView = (CardView)itemView.findViewById(R.id.rootCardView);
            postImageButton = (ImageView)itemView.findViewById(R.id.post_image_button);
            postImage = (ImageView)itemView.findViewById(R.id.post_image);
            postImageClose = (ImageButton) itemView.findViewById(R.id.post_image_close);

        }
    }
}
