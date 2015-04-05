package com.example.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
    int resource;
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

        TextView tt = (TextView) v.findViewById(R.id.vk_name);
        tt.setText((CharSequence) results.get(position).get(ATTRIBUTE_NAME_TEXT_NAME));

        TextView bt = (TextView) v.findViewById(R.id.vk_raiting);
        bt.setText((CharSequence) results.get(position).get(ATTRIBUTE_NAME_TEXT_RAITING));

        ImageView vt = (ImageView)v.findViewById(R.id.vk_photo);
        vt.setImageBitmap((android.graphics.Bitmap) results.get(position).get(ATTRIBUTE_NAME_IMAGE));

        return v;
    }
}
