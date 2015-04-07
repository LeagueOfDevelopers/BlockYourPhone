package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Жамбыл on 05.04.2015.
 */
public class Vk_row_adapter extends SimpleAdapter {
    final String ATTRIBUTE_NAME_TEXT_NAME = "text_name";
    final String ATTRIBUTE_NAME_TEXT_RAITING = "text_place";
    final String ATTRIBUTE_NAME_IMAGE = "image";
    private List<? extends Map<String, ?>> results;
    Context context;
    String Name=null;
    int resource;
    TextView tt;
    LinearLayout LL;
    LinearLayout LL2;
    TextView currentText;
    public Vk_row_adapter(Context _context, List<? extends Map<String, ?>> data, int _resource, String[] from, int[] to) {
        super(_context, data, _resource, from, to);
        this.results = data;
        context = _context;
        resource = _resource;


    }
    @Override
    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource, parent, false);
        LL= (LinearLayout)v.getRootView();
        LL.setOnClickListener(onButtonClickListener);
        tt = (TextView) v.findViewById(R.id.vk_name);
        tt.setText((CharSequence) results.get(position).get(ATTRIBUTE_NAME_TEXT_NAME));

        TextView bt = (TextView) v.findViewById(R.id.vk_raiting);
        bt.setText((CharSequence) results.get(position).get(ATTRIBUTE_NAME_TEXT_RAITING));

        ImageView vt = (ImageView)v.findViewById(R.id.vk_photo);
        byte[] image = (byte[]) results.get(position).get(ATTRIBUTE_NAME_IMAGE);
        vt.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

        return v;
    }
    private View.OnClickListener onButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            LinearLayout asd = (LinearLayout)view;
            LL2 = (LinearLayout)asd.getChildAt(1);
            currentText = (TextView)LL2.getChildAt(0);
            String currentName = (String) currentText.getText();
            new AlertDialog.Builder(context)
                    .setTitle(currentName)
                    .setMessage("Перейти на страницу Вконтакте?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //TODO: Intent на страничку
                                }
                            }).create().show();
        }
    };


}
