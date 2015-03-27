package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.perm.kate.api.Api;

/**
 * Created by Жамбыл on 26.03.2015.
 */
public class Vk extends Activity {
    private final int REQUEST_LOGIN=1;

    Button authorizeButton;
    Button logoutButton;
    Button postButton;
    EditText messageEditText;
    Intent app_intent;

    public static Account account=new Account();
    public static Api api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vk_auth);

        String test = "";

        setupVkUI();
       // showButtons();
    }

    private void setupVkUI() {
        authorizeButton=(Button)findViewById(R.id.authorize);
        //logoutButton=(Button)findViewById(R.id.logout);
       // postButton=(Button)findViewById(R.id.post);
        //messageEditText=(EditText)findViewById(R.id.message);
        authorizeButton.setOnClickListener(authorizeClick);
        //logoutButton.setOnClickListener(logoutClick);
        //postButton.setOnClickListener(postClick);
    }

    private View.OnClickListener authorizeClick=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            startLoginActivity();
        }
    };

/*    private View.OnClickListener logoutClick=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            logOut();
        }
    };

    private View.OnClickListener postClick=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            postToWall();
        }
    };
*/
    private void startLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                //авторизовались успешно
                account.access_token=data.getStringExtra("token");
                account.user_id=data.getLongExtra("user_id", 0);
                account.save(Vk.this);
                api=new Api(account.access_token, Constants.API_ID);
                //showButtons();
                app_intent = new Intent();
                app_intent.setClass(this, App.class);
                startActivity(app_intent);

            }
        }
    }

 /*   private void postToWall() {
        //Общение с сервером в отдельном потоке чтобы не блокировать UI поток
        new Thread(){
            @Override
            public void run(){
                try {
                    String text=messageEditText.getText().toString();
                    api.createWallPost(account.user_id, text, null, null, false, false, false, null, null, null, 0L, null, null);
                    //Показать сообщение в UI потоке
                    // api.getFriends();
                    runOnUiThread(successRunnable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }*/

    Runnable successRunnable=new Runnable(){
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), "Запись успешно добавлена", Toast.LENGTH_LONG).show();
        }
    };
/*
    private void logOut() {
        api=null;
        account.access_token=null;
        account.user_id=0;
        account.save(Vk.this);
        showButtons();
    }

    void showButtons(){
        if(api!=null){
            authorizeButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
            postButton.setVisibility(View.VISIBLE);
            messageEditText.setVisibility(View.VISIBLE);
        }else{
            authorizeButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
            postButton.setVisibility(View.GONE);
            messageEditText.setVisibility(View.GONE);
        }
    }*/
}

