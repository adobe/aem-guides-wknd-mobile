package com.adobe.aem.guides.wknd.mobile.android.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public interface ViewBinder {
   void bind(final JSONObject jsonResponse) throws JSONException, IOException;
   default void unbind() { return; }
}
