package com.knowledgerealm.helpers;

import android.text.Html;
import android.util.Log;

/**
 * HtmlHelper is a general helper class that contains methods related to HTML.
 */
public class HtmlHelper {
    /**
     * Encodes a string to be HTML-safe.
     *
     * @param decoded the string to encode
     * @return the HTML-safe string
     */
    public static String encodeHtml(String decoded) {
        try {
            return Html.escapeHtml(decoded);
        } catch (Exception e) {
            Log.i("HtmlHelper", "Could not encode HTML, using decoded string\nReason:" + e.getMessage());
            return decoded;
        }
    }

    /**
     * Decodes an HTML-encoded string.
     *
     * @param encoded the HTML-encoded string to decode
     * @return the decoded string
     */
    public static String decodeHtml(String encoded) {
        try {
            // Legacy flag to avoid deprecated warnings
            return Html.fromHtml(encoded, Html.FROM_HTML_MODE_LEGACY).toString();
        } catch (Exception e) {
            Log.i("HtmlHelper", "Could not decode HTML, using encoded string\nReason:" + e.getMessage());
            return encoded;
        }
    }
}
