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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adobe.aem.guides.wknd.mobile.android.adapters.EventsAdapter;
import com.adobe.aem.guides.wknd.mobile.android.models.Event;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventsViewBinder implements ViewBinder {

    private final Context context;
    private RecyclerView recyclerView;
    private ObjectMapper objectMapper;
    private String host;

    public EventsViewBinder(final Context applicationContext, final String host, final RecyclerView recyclerView) {
        this.context = applicationContext;
        this.recyclerView = recyclerView;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.host = host;
    }

    public void bind(final JSONObject jsonResponse) throws JSONException {
        Log.d("WKND_MOBILE", "Binding Events");

        final List<Event> events = new ArrayList<>();

        final JSONObject components = jsonResponse.getJSONObject(":items").getJSONObject("root").getJSONObject(":items");
        final JSONArray contentFragments = components.getJSONObject("contentfragmentlist").getJSONArray("items");

        for (int i = 0; i < contentFragments.length(); i++) {
            try {
                final Event event = objectMapper.readValue(contentFragments.getJSONObject(i).getJSONObject("elements").toString(), Event.class);

                Log.d("WKND_MOBILE", "Collected event: " + event.getTitle());

                events.add(event);
            } catch (IOException e) {
                Log.e("WKND_MOBILE", "Unable to parse event from JSON: " + e.getMessage());
            }
        }

        Collections.sort(events);

        final EventsAdapter adapter = new EventsAdapter(events, host);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}

