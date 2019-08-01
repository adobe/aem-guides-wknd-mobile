package com.adobe.aem.guides.wknd.mobile.android.services;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import com.adobe.aem.guides.wknd.mobile.android.models.Image;
import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LogoViewBinder implements ViewBinder {

    private final Context context;
    private final String aemHost;
    private ObjectMapper objectMapper = new ObjectMapper();
    private ImageView logo;

    public LogoViewBinder(final Context applicationContext, final String aemHost, final ImageView imageView) {
        this.context = applicationContext;
        this.logo = imageView;
        this.aemHost = aemHost;
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public void bind(final JSONObject jsonResponse) throws JSONException, IOException {
        final JSONObject components = jsonResponse.getJSONObject(":items").getJSONObject("root").getJSONObject(":items");

        if (components.has("image")) {
            final Image image = objectMapper.readValue(components.getJSONObject("image").toString(), Image.class);
            final String imageUrl = aemHost + image.getSrc();
            Glide.with(context).load(imageUrl).into(logo);
        } else {
            Log.d("WKND_MOBILE", "Missing Logo");
        }
    }
}

