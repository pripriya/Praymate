package com.geval6.praymate.Utlis;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os) {
        try {
            byte[] bytes = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
            while (true) {
                int count = is.read(bytes, 0, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
                if (count != -1) {
                    os.write(bytes, 0, count);
                } else {
                    return;
                }
            }
        } catch (Exception e) {
        }
    }
}
