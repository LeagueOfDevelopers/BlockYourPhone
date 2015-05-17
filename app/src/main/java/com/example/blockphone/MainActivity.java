package com.example.blockphone;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;


public class MainActivity  extends ActionBarActivity implements LocalUI {

    /**
     * Scope is set of required permissions for your application
     * @see <a href="https://vk.com/dev/permissions">vk.com api permissions documentation</a>
     */
    int NumbOfTabs = 2;
    ViewPager pager;
    TabViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"asd","Все"};
    TextView[] dots;
    int dotsCount;


    private static final String[] sMyScope = new String[] {
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation);
        VKUIHelper.onCreate(this);
        VKSdk.initialize(sdkListener, MainActivity.this.getResources().getString(R.string.API_ID));
        if (VKSdk.wakeUpSession()) {
            Log.i("Login", "Starting app");
            startApp();
            finish();
        }
        startLocalUI();
        //String[] fingerprint = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //Log.d("Fingerprint", fingerprint[0]);
    }
    public void showLogout() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.vk, new LogoutFragment())
                .commit();
    }
    public void showLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.vk, new LoginFragment())
                .commit();
    }

    @Override
    public void onResume(){
        super.onResume(); //продолжает работу приложения
        VKUIHelper.onResume(this);
        if (VKSdk.isLoggedIn()) {
            startApp();
            //showLogout();
        } else {
            //startActivity(new Intent(this, PresentationActivity.class));
            //finish();
            //showLogin(); //TODO ERROR когда включаешь интрентер в мэйн активити
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    private final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(sMyScope);
        }

        @Override
        public void onAccessDenied(final VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            startApp();
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            startApp();
        }
    };
    private void startApp() {
        startActivity(new Intent(this, App.class));
    }

    @Override
    public void startLocalUI() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.setStatusBarColor(getResources().getColor(R.color.ColorPrimary));
            window.setStatusBarColor(Color.BLACK);
        }

        adapter =  new TabViewPagerAdapter(getSupportFragmentManager(),Titles, NumbOfTabs,2);
        pager = (ViewPager) findViewById(R.id.pager1);
        pager.setAdapter(adapter);

        dotsCount = adapter.getCount();
        dots = new TextView[dotsCount];
        dots[0] = (TextView)findViewById(R.id.dot1);
        dots[1] = (TextView)findViewById(R.id.dot2);

        for(int i = 0; i< dotsCount;i++) {
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
        }

        dots[0].setTextColor(getResources().getColor(R.color.ColorPrimary));

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //setDot(position);
            }

            @Override
            public void onPageSelected(int position) {
                setDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // setDot(position);
            }
        });
    }
    void setDot(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
        dots[position].setTextColor(getResources().getColor(R.color.ColorPrimary));
    }

    public static class LoginFragment extends android.support.v4.app.Fragment {
        public LoginFragment() {
            super();
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_login, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getView().findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.authorize(sMyScope, true, false);
                }
            });

            getView().findViewById(R.id.force_oauth_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.authorize(sMyScope, true, true);
                }
            });
        }
    }
    public static class LogoutFragment extends android.support.v4.app.Fragment {
        public LogoutFragment() {
            super();
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_logout, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getView().findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)getActivity()).startApp();
                }
            });

            getView().findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.logout();
                    if (!VKSdk.isLoggedIn()) {
                        ((MainActivity)getActivity()).showLogin();
                    }
                }
            });
        }
    }
}