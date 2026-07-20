package com.mykids.learning.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * يشغّل ملفات mp3 المسجَّلة مسبقًا (نطق عربي/إنجليزي، أصوات حيوانات) من assets/.
 * إن كان الملف غير موجود يفشل بصمت بدل تعطيل التطبيق (بعض الأصول قد تكون ناقصة).
 */
public class AssetAudioPlayer {

    private final Context appContext;
    private MediaPlayer mediaPlayer;

    public AssetAudioPlayer(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public void play(String assetPath) {
        if (assetPath == null) return;
        stop();
        try {
            AssetFileDescriptor afd = appContext.getAssets().openFd(assetPath);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            mediaPlayer.prepareAsync();
        } catch (IOException | IllegalStateException | IllegalArgumentException e) {
            // ملف صوتي مفقود أو تالف — نتجاهل بصمت بدل تعطّل التطبيق
            mediaPlayer = null;
        }
    }

    public void stop() {
        if (mediaPlayer == null) return;
        try {
            mediaPlayer.release();
        } catch (IllegalStateException ignored) {
            // already released
        }
        mediaPlayer = null;
    }
}
