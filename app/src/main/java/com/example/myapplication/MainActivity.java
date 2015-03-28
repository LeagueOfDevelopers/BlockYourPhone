package com.example.myapplication;

import com.perm.kate.api.Api;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    Intent app_intent;
    Account account = new Account();
    static Api api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vk_auth);

        StartApp();

    }
    void StartApp()
    {
        //Восстановление сохранённой сессии вк
        account.restore(this);

        //Если сессия есть создаём API для обращения к серверу
        if(account.access_token!=null)
            api=new Api(account.access_token, Constants.API_ID);

        if(api == null)
            app_intent = new Intent(this, Vk.class);
        else
            app_intent = new Intent(this, App.class);

        startActivity(app_intent);
    }

}