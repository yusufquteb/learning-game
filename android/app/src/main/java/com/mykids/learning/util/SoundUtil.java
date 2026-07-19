package com.mykids.learning.util;

import android.media.AudioManager;
import android.media.ToneGenerator;

/** أصوات تفاعلية بسيطة (نغمات) للإجابات الصحيحة والخاطئة، بدون أي ملفات صوتية خارجية. */
public class SoundUtil {

    private final ToneGenerator toneGenerator;

    public SoundUtil() {
        ToneGenerator tg;
        try {
            tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 80);
        } catch (RuntimeException e) {
            tg = null;
        }
        toneGenerator = tg;
    }

    public void playCorrect() {
        if (toneGenerator == null) return;
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2, 180);
    }

    public void playWrong() {
        if (toneGenerator == null) return;
        toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 220);
    }

    public void release() {
        if (toneGenerator != null) toneGenerator.release();
    }
}
