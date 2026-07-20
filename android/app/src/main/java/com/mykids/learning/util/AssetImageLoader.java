package com.mykids.learning.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/** يحمّل صور Solution.png من مجلد assets/ (بدون أي شبكة، الملفات مرفقة داخل التطبيق). */
public final class AssetImageLoader {

    public static Bitmap load(Context context, String assetPath) {
        try (InputStream is = context.getAssets().open(assetPath)) {
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            return null;
        }
    }

    private AssetImageLoader() { }
}
