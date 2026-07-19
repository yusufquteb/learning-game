package com.mykids.learning.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/** ينطق الكلمات بالعربية باستخدام محرك تحويل النص إلى كلام المدمج في أندرويد (مجاني، بدون ملفات صوت). */
public class SpeechUtil {

    private TextToSpeech tts;
    private boolean ready = false;

    public SpeechUtil(Context context) {
        tts = new TextToSpeech(context.getApplicationContext(), status -> {
            if (status == TextToSpeech.SUCCESS && tts != null) {
                int result = tts.setLanguage(new Locale("ar"));
                ready = result != TextToSpeech.LANG_MISSING_DATA
                        && result != TextToSpeech.LANG_NOT_SUPPORTED;
                tts.setSpeechRate(0.85f);
            }
        });
    }

    public void speak(String text) {
        if (tts == null || !ready || text == null || text.isEmpty()) return;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utterance");
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
