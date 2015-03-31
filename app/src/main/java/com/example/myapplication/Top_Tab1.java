package com.example.myapplication;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.perm.kate.api.Api;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Жамбыл on 29.03.2015.
 */
public class Top_Tab1 extends Fragment {
    ListView VkRowListView1;
    Button PostToWallButton;
    //Данные для вк_адаптера
    final String ATTRIBUTE_NAME_TEXT_NAME = "text_name";
    final String ATTRIBUTE_NAME_TEXT_PLACE = "text_place";
    final String ATTRIBUTE_NAME_IMAGE = "image";

    List<String> Names =  new ArrayList<String>();

    String[] Places = {"1","2","3","4","5","6","7"};
    int img = R.drawable.zhambul;

    // массив имен атрибутов, из которых будут читаться данные
    String[] from = { ATTRIBUTE_NAME_TEXT_NAME, ATTRIBUTE_NAME_TEXT_PLACE,
            ATTRIBUTE_NAME_IMAGE };
    // массив ID View-компонентов, в которые будут вставлять данные
    int[] to = { R.id.vk_name, R.id.vk_raiting, R.id.vk_photo };

    //Дата, куда пакуем
        ArrayList<Map<String, Object>> data;
        Map<String, Object> m;

    Vk vk = new Vk();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.top_tab_1,container,false);
        //Typeface type_thin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
        //TextView mytab1 = (TextView)v.findViewById(R.id.mytab1);
        //mytab1.setTypeface(type_thin);
        StartUI(v);
        Names.add("Жамбыл");

        //проверяем на наличие друзей
        if(Names != null & !Names.isEmpty()){
            data  = new ArrayList<Map<String, Object>>(
                    Names.size());
            PackAndSendData(v);
        }
        else
            PostToWallButton.setVisibility(View.VISIBLE);

        return v;
    }

    //Пакуем и отправляем дату
    private void PackAndSendData(View v)
    {
        for(int i=0;i<Names.size(); i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT_NAME, Names.get(i));
            m.put(ATTRIBUTE_NAME_TEXT_PLACE, Places[i]);
            m.put(ATTRIBUTE_NAME_IMAGE, img);
            data.add(m);

            // создаем адаптер
            SimpleAdapter sAdapter1 = new SimpleAdapter(getActivity(), data, R.layout.vk_row,
                    from, to);
            //привязываем и сетим
            VkRowListView1 = (ListView) v.findViewById(R.id.VkRowListView1);
            VkRowListView1.setAdapter(sAdapter1);
        }
    }
    private void StartUI(View v)
    {
        PostToWallButton = (Button)v.findViewById(R.id.PostToWallButton);
        PostToWallButton.setVisibility(View.GONE);
        PostToWallButton.setOnClickListener(onWallButtonClickListener);
    }
    private View.OnClickListener onWallButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            vk.WallText("testasdas", getActivity());
        }
    };

}