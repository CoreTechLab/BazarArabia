package bazararabia.com.and.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bazararabia.com.and.R;
import bazararabia.com.and.entity.CategoryModel;
import bazararabia.com.and.util.ImageUtil;

public class CategoryListAdapter extends ArrayAdapter<CategoryModel> {

    private LayoutInflater mInflater;

    public CategoryListAdapter(Context context, List<CategoryModel> subCategoryList){

        super(context, 0 , subCategoryList);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CategoryListAdapter.ViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.category_list_item, parent, false);
            holder = new CategoryListAdapter.ViewHolder();
            holder.categoryListImage = (ImageView) convertView.findViewById(R.id.category_list_image);
            holder.categoryListName = (TextView) convertView.findViewById(R.id.category_list_name);
            convertView.setTag(holder);
        } else {
            holder = (CategoryListAdapter.ViewHolder) convertView.getTag();
        }

        CategoryModel category = getItem(position);
        ImageUtil.displayImage(holder.categoryListImage, category.categoryImage, null);
        holder.categoryListName.setText(category.categoryNameEn);
        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder{

        public ImageView categoryListImage;
        public TextView categoryListName;
    }

}
