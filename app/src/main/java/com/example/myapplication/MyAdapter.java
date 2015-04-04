package com.example.myapplication;

import android.content.Context;
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
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    // Declaring Variable to Understand which View is being worked on
    // If the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[]; // String Array to store the passed titles Value from App.java or Top.java
    private int mIcons[];       // Int Array to store the passed icons resource value from App.java or Top.java

    private String name;        //String Resource for header View Name
    private int profile;        //int Resource for header view profile picture
    private int points;       //String Resource for header view email
    Context context;
    LinearLayout layoutFromRecycler;

    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holder_id;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView points;
        Context context_2;
        LinearLayout layoutFromRecycler1;



        public ViewHolder(View itemView,int ViewType,Context c) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);
            context_2 = c;
            itemView.setClickable(true);
            //itemView.setOnClickListener(Click);

            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holder_id = 1;                                               // setting holder id as 1 as the object being populated are of type item row

            }
            if(ViewType == TYPE_HEADER) {
                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                points = (TextView) itemView.findViewById(R.id.points);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holder_id = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }

/*
        private View.OnClickListener Click=new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
               // drlay.closeDrawers();
                switch(getPosition())
                {
                    case 1:
                        i.setComponent(new ComponentName(contxt,Top.class));
                        v.getContext().startActivity(i);
                        break;

                    case 3:
                        MainActivity.api = null;
                        Vk.api = null;
                        Vk.account.access_token=null;
                        Vk.account.user_id=0;
                        Vk.account.save(contxt);

                        i.setComponent(new ComponentName(contxt,MainActivity.class));
                        v.getContext().startActivity(i);
                        break;
                }

            }
        };
*/

    }
    MyAdapter(String Titles[],int Icons[],String Name,int _points, int Profile,Context passedContext){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        name = Name;
        points = _points;
        profile = Profile;                     //here we assign those passed values to the values we declared here
        this.context = passedContext;
        //in adapter
    }
    //Below first we override the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false); //Inflating the layout\

            ViewHolder vhItem = new ViewHolder(v,viewType,context); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object
            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType,context); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created
        }
        return null;

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        if(holder.Holder_id ==1) {
            // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
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
            holder.profile.setImageResource(profile);           // Similarly we set the resources for header view
            holder.Name.setText(name);
            holder.points.setText(points + " очков");
            holder.points.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf"));
        }
    }
    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length+1;                               // the number of items in the list will be +1 the titles including the header view.
    }



    // With the following method we check what type of view is being passed
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