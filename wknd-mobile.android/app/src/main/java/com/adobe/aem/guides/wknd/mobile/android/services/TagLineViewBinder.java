/*
 * Copyright 2019 Adobe. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 * OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

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

