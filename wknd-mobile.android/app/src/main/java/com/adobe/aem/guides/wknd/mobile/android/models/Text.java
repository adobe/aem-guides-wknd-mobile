package com.adobe.aem.guides.wknd.mobile.android.models;

import android.os.Build;
import android.text.Html;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Text {

    @JsonProperty(value = "text")
    private String text;

    @JsonProperty(value = "richText")
    private boolean richText;

    public String getText() {
        if (!isRichText()) {
            return text;
        } else if (Build.VERSION.SDK_INT >= 24) {
           return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT).toString(); // for 24 api and more
        } else {
           return Html.fromHtml(text).toString(); // or for older api
        }
    }

    public boolean isRichText() {
        return richText;
    }
}
