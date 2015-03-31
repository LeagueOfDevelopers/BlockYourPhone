package com.example.myapplication;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.perm.kate.api.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Жамбыл on 29.03.2015.
 */
public class Top_Tab2 extends Fragment {

    ListView VkRowListView2;
    //Данные для вк_адаптера
    final String ATTRIBUTE_NAME_TEXT_NAME = "text_name";
    final String ATTRIBUTE_NAME_TEXT_PLACE = "text_place";
    final String ATTRIBUTE_NAME_IMAGE = "image";

    String[] Names = { "Жамбыл", "Хуйбыл", "Мамбыл",
            "Жуйбыл","Куйбыл", "Намбыл", "Трамбон" };
    String[] Places = {"1","2","3","4","5","6","7"};
    int img = R.drawable.zhambul;


    // массив имен атрибутов, из которых будут читаться данные
    String[] from = { ATTRIBUTE_NAME_TEXT_NAME, ATTRIBUTE_NAME_TEXT_PLACE,
            ATTRIBUTE_NAME_IMAGE };
    // массив ID View-компонентов, в которые будут вставлять данные
    int[] to = { R.id.vk_name, R.id.vk_raiting, R.id.vk_photo };

    //Дата, куда пакуем
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
            Names.length);
    Map<String, Object> m;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.top_tab_2,container,false);
        //Typeface type_thin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
        //TextView mytab2 = (TextView)v.findViewById(R.id.mytab2);
        //mytab2.setTypeface(type_thin);

        //Пакуем и отправляем
        PackAndSendData(v);

        return v;
    }
    private void PackAndSendData(View v)
    {
        for(int i=0;i<Names.length; i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT_NAME, Names[i]);
            m.put(ATTRIBUTE_NAME_TEXT_PLACE, Places[i]);
            m.put(ATTRIBUTE_NAME_IMAGE, img);
            data.add(m);

            // создаем адаптер
            SimpleAdapter sAdapter2 = new SimpleAdapter(getActivity(), data, R.layout.vk_row,
                    from, to);
            //привязываем и сетим
            VkRowListView2 = (ListView) v.findViewById(R.id.VkRowListView2);
            VkRowListView2.setAdapter(sAdapter2);
        }
    }

}