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

package com.adobe.aem.guides.wknd.mobile.android.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adobe.aem.guides.wknd.mobile.R;
import com.adobe.aem.guides.wknd.mobile.android.models.Event;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private static final SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    private final String host;
    private List<Event> events;

    public EventsAdapter(final List<Event> events, final String host) {
        this.events = events;
        this.host = host;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event,
                        parent,
                        false);

        return new ViewHolder(view, host);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setEvent(events.get(position), position);
        holder.bindData();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(final List<Event> events) {
        this.events.addAll(events);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final String host;
        private ImageView image;
        private TextView month;
        private TextView day;
        private TextView title;
        private TextView description;
        private TextView typeAndVenue;
        private Button button;
        private GridLayout secondaryDetails;
        private TextView ticketPrice;
        private TextView venueName;
        private TextView time;
        private TextView city;

        private Event event;
        private int position;

        public ViewHolder(@NonNull View itemView, @NonNull String host) {
            super(itemView);
            this.host = host;
            this.view = itemView;
        }

        public void setEvent(Event event, int position) {
            this.event = event;
            this.position = position;
        }

        public Event getEvent() {
            return event;
        }

        public void bindData() {
            image = view.findViewById(R.id.image);
            month = view.findViewById(R.id.month);
            day = view.findViewById(R.id.day);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            typeAndVenue = view.findViewById(R.id.typeAndVenue);
            button = view.findViewById(R.id.moreLessButton);
            secondaryDetails = view.findViewById(R.id.eventSecondaryDetails);
            ticketPrice = view.findViewById(R.id.ticketPrice);
            venueName = view.findViewById(R.id.venue);
            time = view.findViewById(R.id.time);
            city = view.findViewById(R.id.city);

            final String imageUrl = host + event.getImage();
            Glide.with(view).load(imageUrl).into(image);

            Log.d("WKND", String.format("Loading image from [ %s ]", imageUrl));

            month.setText(monthFormat.format(event.getTime().getTime()).toUpperCase());
            day.setText(dayFormat.format(event.getTime().getTime()));

            title.setText(event.getTitle());
            description.setText(event.getDescription());
            typeAndVenue.setText(event.getType() + " @" + event.getVenueName());

            ticketPrice.setText("$" + event.getPrice());
            venueName.setText(event.getVenueName());
            time.setText(timeFormat.format(event.getTime().getTime()));
            city.setText(event.getVenueCity());

            if (position == 0) {
                // Is first element
                view.setBackgroundColor(view.getResources().getColor(R.color.colorPrimary, view.getContext().getTheme()));
                typeAndVenue.setTextColor(view.getResources().getColor(R.color.colorSecondaryGrey, view.getContext().getTheme()));
                description.setTextColor(view.getResources().getColor(R.color.colorSecondaryGrey, view.getContext().getTheme()));
                button.setBackgroundColor(view.getResources().getColor(R.color.colorWhite, view.getContext().getTheme()));
            }

            /** Actions **/
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (secondaryDetails.getVisibility() == View.GONE) {
                        button.setText(R.string.button_less);


                        view.getRootView().findViewById(R.id.eventsList).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                secondaryDetails.getParent().requestChildFocus(secondaryDetails, secondaryDetails);
                            }

                        }, 100);

                        secondaryDetails.setVisibility(View.VISIBLE);
                    } else {
                        button.setText(R.string.button_more);
                        secondaryDetails.setVisibility(View.GONE);
                    }
                }
            });

        }
    }
}
