package com.adobe.aem.guides.wknd.mobile.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.adobe.aem.guides.wknd.mobile.R;
import com.adobe.aem.guides.wknd.mobile.android.services.EventsViewBinder;
import com.adobe.aem.guides.wknd.mobile.android.services.LogoViewBinder;
import com.adobe.aem.guides.wknd.mobile.android.services.TagLineViewBinder;
import com.adobe.aem.guides.wknd.mobile.android.services.ViewBinder;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String AEM_DEFAULT_PATH = "/content/wknd-mobile/en/api/events.model.json";
    private String AEM_DEFAULT_HOST = "http://localhost:4503";
    private String AEM_ANDROID_EMULATOR_LOCALHOST = "http://localhost";
    private String AEM_ANDROID_EMULATOR_LOCALHOST_PROXY = "http://10.0.2.2";

    private RequestQueue queue;
    private SwipeRefreshLayout swipeContainer;
    private BottomNavigationView navigation;
    private MainActivity that;
    private SharedPreferences sharedPreferences;
    private TextView errorMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_settings:
                    Intent intent = new Intent(that, SettingsActivity.class);
                    startActivity(intent);
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = this;
        queue = Volley.newRequestQueue(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        swipeContainer = findViewById(R.id.container);
        errorMessage = findViewById(R.id.errorMessage);
        errorMessage.setVisibility(View.GONE);

        // Create the custom classes that will map the JSON -> POJO -> View elements
        final List<ViewBinder> viewBinders = new ArrayList<>();
        viewBinders.add(new LogoViewBinder(this, getAemHost(), (ImageView) findViewById(R.id.logo)));
        viewBinders.add(new TagLineViewBinder(this, (TextView) findViewById(R.id.tagLine)));
        viewBinders.add(new EventsViewBinder(this, getAemHost(), (RecyclerView) findViewById(R.id.eventsList)));

        // Init the application when newly opened
        initApp(viewBinders);

        // Register the listeners to handle app refresh
        onRefreshApp(viewBinders);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void onRefreshApp(final List<ViewBinder> viewBinders) {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initApp(viewBinders);
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorPrimaryDark);
    }

    public void initApp(final List<ViewBinder> viewBinders) {
        Log.d("WKND_MOBILE", "Init content from: " + getAemUrl());

        final String aemUrl = getAemUrl(); // http://localhost:4503/content/wknd-mobile/en/api/events.mobile.json
        JsonObjectRequest request = new JsonObjectRequest(aemUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WKND_MOBILE", "Successfully connected to AEM Content Services endpoint!");

                        findViewById(R.id.container).requestFocus();
                        ((NestedScrollView) findViewById(R.id.nestedScrollView)).scrollTo(0, 0);

                        for (final ViewBinder viewBinder : viewBinders) {
                            try {
                                viewBinder.bind(response);
                            } catch (JSONException | IOException e) {
                                Log.e("WKND_MOBILE", "Could not bind for ViewBinder: " + viewBinder.getClass().getName(), e);
                            }
                        }

                        swipeContainer.setRefreshing(false);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("WKND_MOBILE", "Response error: " + error.getMessage());
                        swipeContainer.setRefreshing(false);
                        errorMessage.setVisibility(View.VISIBLE);
                    }
                }
        );

        queue.add(request);
    }

    private String getAemUrl() {
        return getAemHost() + getAemPath();
    }

    private String getAemHost() {
        String host = sharedPreferences.getString("host", AEM_DEFAULT_HOST);

        if (StringUtils.startsWith(host, AEM_ANDROID_EMULATOR_LOCALHOST)) {
            host = StringUtils.replaceOnce(host, AEM_ANDROID_EMULATOR_LOCALHOST, AEM_ANDROID_EMULATOR_LOCALHOST_PROXY);
        }

        return host;
    }

    private String getAemPath() {
        return sharedPreferences.getString("path", AEM_DEFAULT_PATH);
    }
}
