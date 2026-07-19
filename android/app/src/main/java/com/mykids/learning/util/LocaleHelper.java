package com.mykids.learning.util;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

/** يفرض اللغة العربية (RTL) على التطبيق بغض النظر عن لغة الجهاز، لأن محتوى اللعبة عربي بالكامل. */
public final class LocaleHelper {

    public static Context wrap(Context context) {
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);

        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        config.setLayoutDirection(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.createConfigurationContext(config);
        } else {
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            return context;
        }
    }

    private LocaleHelper() { }
}
