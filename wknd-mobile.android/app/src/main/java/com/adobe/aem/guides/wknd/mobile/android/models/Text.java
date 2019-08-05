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
