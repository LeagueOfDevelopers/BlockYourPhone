package com.example.blockphone;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Жамбыл on 27.03.2015.
 */
public class DrawableAdapter extends RecyclerView.Adapter<DrawableAdapter.ViewHolder> {
    Typeface  type;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];
    private int mIcons[];

    private String name;
    private byte[] profile1;
    private int points;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holder_id;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView points;
        Context context_2;
        LinearLayout layoutFromRecycler1;



        public ViewHolder(View itemView,int ViewType,Context c) {
            super(itemView);
            context_2 = c;
            itemView.setClickable(true);

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                Holder_id = 1;

            }
            if(ViewType == TYPE_HEADER) {
                Name = (TextView) itemView.findViewById(R.id.name);
                points = (TextView) itemView.findViewById(R.id.points);
                profile = (ImageView) itemView.findViewById(R.id.circleView);
                Holder_id = 0;
            }
        }

    }
    DrawableAdapter(String Titles[], int Icons[], String Name, int _points, byte[] Profile, Context passedContext){
        mNavTitles = Titles;
        mIcons = Icons;
        name = Name;
        points = _points;
        profile1 = Profile;
        this.context = passedContext;
        type = Typeface.createFromAsset( passedContext.getAssets(), "fonts/RobotoCondensed-Light.ttf");

    }


    @Override
    public DrawableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType,context);
            return vhItem;


        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);
            ViewHolder vhHeader = new ViewHolder(v,viewType,context);
            return vhHeader;
        }
        return null;

    }


    @Override
    public void onBindViewHolder(DrawableAdapter.ViewHolder holder, int position) {
        if(holder.Holder_id ==1) {
            if(context.getClass().getSimpleName().equals("App")) {
                if(holder.getPosition() == 1){
                    holder.textView.setTextColor(Color.WHITE);
                    holder.layoutFromRecycler1 = (LinearLayout)holder.textView.getRootView();
                    holder.layoutFromRecycler1.setBackgroundColor(context.getResources().getColor(R.color.ColorPrimaryDark));
                }
           }
            else if(context.getClass().getSimpleName().equals("Top")) {
                if(holder.getPosition() == 2){
                    holder.textView.setTextColor(Color.WHITE);
                    holder.layoutFromRecycler1 = (LinearLayout)holder.textView.getRootView();
                    holder.layoutFromRecycler1.setBackgroundColor(context.getResources().getColor(R.color.ColorPrimaryDark));
                }
            }
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            //Typeface type_medium = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
            //holder.textView.setTypeface(type_medium);
            holder.imageView.setImageResource(mIcons[position - 1]);// Setting the image with array of our icons
            if(context.getClass().getSimpleName().equals("App")) {
                if(holder.getPosition() == 1){
                    holder.imageView.setImageResource(R.drawable.ic_action1);
                }
            }
            else if(context.getClass().getSimpleName().equals("Top")) {
                if(holder.getPosition() == 2){
                    holder.imageView.setImageResource(R.drawable.ic_raiting1);
                }
            }

        }
        else{
            if(profile1 == null){
               // Toast.makeText(context, "Profile1 null", Toast.LENGTH_LONG).show();
            }
            else {
                holder.profile.setImageBitmap(BitmapFactory.decodeByteArray(profile1, 0, profile1.length));
            }
            holder.Name.setText(name);
            holder.points.setText(points + " очков");
            holder.points.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf"));
        }
    }
    @Override
    public int getItemCount() {
        return mNavTitles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}