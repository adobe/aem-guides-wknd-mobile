package com.adobe.aem.guides.wknd.mobile.android.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.adobe.aem.guides.wknd.mobile.android.models.Text;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TagLineViewBinder implements ViewBinder {

    private final Context context;
    private ObjectMapper objectMapper;
    private TextView tagLine;

    public TagLineViewBinder(final Context applicationContext, final TextView textView) {
        this.context = applicationContext;
        this.tagLine = textView;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public void bind(final JSONObject jsonResponse) throws JSONException, IOException {
        final JSONObject components = jsonResponse.getJSONObject(":items").getJSONObject("root").getJSONObject(":items");

        if (components.has("text")) {
            Log.d("WKND_MOBILE", "Binding TagLine");

            final Text text = objectMapper.readValue(components.getJSONObject("text").toString(), Text.class);

            tagLine.setText(text.getText());
        } else {
            Log.d("WKND_MOBILE", "Missing Tagline");
            tagLine.setVisibility(View.GONE);
        }
    }
}

