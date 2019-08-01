package com.adobe.aem.guides.wknd.mobile.android.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adobe.aem.guides.wknd.mobile.R;
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

