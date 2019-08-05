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

package com.adobe.aem.guides.wknd.mobile.android.models;

import android.os.Build;
import android.text.Html;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class Event implements Comparable<Event> {

    private String title;

    private String description;

    private Calendar time;

    private String type;

    private int price;

    private String image;

    private String venueName;

    private String venueCity;

    public String getTitle() {
        return title;
    }

    @JsonProperty("eventTitle")
    public void setTitle(final Map<String, String> json) {
        this.title = json.get("value");
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("eventDescription")
    public void setDescription(final Map<String, String> json) {
        String tmp = json.get("value");

        if (Build.VERSION.SDK_INT >= 24) {
            this.description = Html.fromHtml(tmp, Html.FROM_HTML_MODE_LEGACY).toString(); // for API 24+
        } else {
            this.description = Html.fromHtml(tmp).toString(); // For older APIs
        }
    }

    public Calendar getTime() {
        return time;
    }

    @JsonProperty("eventDateAndTime")
    public void setTime(final Map<String, Object> json) {
        long timestamp = (long) json.get("value");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        this.time = calendar;
    }

    public String getType() {
        return type;
    }

    @JsonProperty("eventType")
    public void setType(final Map<String, String> json) {
        this.type = json.get("value");
    }

    public int getPrice() {
        return price;
    }

    @JsonProperty("eventPrice")
    public void setPrice(final Map<String, Object> json) {
        this.price = (Integer) json.get("value");
    }

    public String getImage() {
        return image;
    }

    @JsonProperty("eventImage")
    public void setImage(final Map<String, String> json) {
        this.image = json.get("value");
    }

    public String getVenueName() {
        return venueName;
    }

    @JsonProperty("venueName")
    public void setVenueName(final Map<String, String> json) {
        this.venueName = json.get("value");
    }

    public String getVenueCity() {
        return venueCity;
    }

    @JsonProperty("venueCity")
    public void setVenueCity(final Map<String, String> json) {
        this.venueCity = json.get("value");
    }

    @Override
    public int compareTo(Event other) {
        return this.getTime().before(other.getTime()) ? -1 : 1;
    }

    // Two employees are equal if their IDs are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Event event = (Event) o;
        return getTitle().equals(event.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }
}
