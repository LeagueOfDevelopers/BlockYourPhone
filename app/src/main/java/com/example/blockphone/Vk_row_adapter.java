package com.example.blockphone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
    TextView tt;
    LinearLayout LL;
    LinearLayout LL2;
    TextView currentText;
    int Tab;

    public Vk_row_adapter(Context _context, List<? extends Map<String, ?>> data, int _resource, String[] from, int[] to,int _Tab) {
        super(_context, data, _resource, from, to);
        this.results = data;
        context = _context;
        resource = _resource;
        Tab = _Tab;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource, parent, false); //vk_row.xml
        LL = (LinearLayout)v.getRootView();
        LL.setTag(position);
        LL.setOnLongClickListener(onButtonClickListener2);
        tt = (TextView) v.findViewById(R.id.vk_name);
        tt.setText((CharSequence) results.get(position).get(ATTRIBUTE_NAME_TEXT_NAME));
        TextView bt = (TextView) v.findViewById(R.id.vk_raiting);
        bt.setText(String.valueOf(results.get(position).get(ATTRIBUTE_NAME_TEXT_RAITING) +" очков"));
        ImageView vt = (ImageView)v.findViewById(R.id.vk_photo);
        byte[] image = (byte[]) results.get(position).get(ATTRIBUTE_NAME_IMAGE);
        if(image == null)
            Log.e("Vk_row_adapter","image = null");
        vt.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        return v;
    }
    private View.OnLongClickListener onButtonClickListener2= new View.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View view) {

            LinearLayout LL = (LinearLayout)view;
            LL2 = (LinearLayout)LL.getChildAt(1);
            currentText = (TextView)LL2.getChildAt(0);
            String currentName = (String) currentText.getText();

            int position = (Integer) LL.getTag();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String idAsString = null;
            if(prefs != null) {
                switch (Tab) {
                    case 1:
                        idAsString = prefs.getString("FriendVkId" + String.valueOf(position), null);
                        break;
                    case 2:
                        idAsString = prefs.getString("UserVkId" + String.valueOf(position), null);
                        break;
                }
            }
            final long finalId = Long.valueOf(idAsString);
            new AlertDialog.Builder(context)
                    .setTitle(currentName)
                    .setMessage("Перейти на страницу Вконтакте?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if(finalId != 0){
                                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://vk.com/id" + String.valueOf(finalId))));
                                        context.startActivity(i);
                                    }
                                }
                            }).create().show();
            return false;
        }
    };


}
