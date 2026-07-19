package com.mykids.learning;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mykids.learning.progress.ProgressManager;
import com.mykids.learning.ui.HomeFragment;
import com.mykids.learning.ui.WelcomeFragment;
import com.mykids.learning.util.AssetAudioPlayer;
import com.mykids.learning.util.LocaleHelper;
import com.mykids.learning.util.SoundUtil;
import com.mykids.learning.util.SpeechUtil;

public class MainActivity extends AppCompatActivity {

    private ProgressManager progressManager;
    private SpeechUtil speechUtil;
    private SoundUtil soundUtil;
    private AssetAudioPlayer assetAudioPlayer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressManager = new ProgressManager(this);
        speechUtil = new SpeechUtil(this);
        soundUtil = new SoundUtil();
        assetAudioPlayer = new AssetAudioPlayer(this);

        if (savedInstanceState == null) {
            Fragment start = progressManager.getName().isEmpty()
                    ? new WelcomeFragment()
                    : new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, start)
                    .commit();
        }
    }

    public ProgressManager getProgressManager() {
        return progressManager;
    }

    public SpeechUtil getSpeechUtil() {
        return speechUtil;
    }

    public SoundUtil getSoundUtil() {
        return soundUtil;
    }

    public AssetAudioPlayer getAssetAudioPlayer() {
        return assetAudioPlayer;
    }

    /** يعرض شاشة جديدة فوق المكدس (يمكن الرجوع عنها بزر الرجوع). */
    public void pushFragment(@NonNull Fragment fragment) {
        pushFragment(fragment, null);
    }

    /** يعرض شاشة جديدة ويضع علامة (اسم) على مكانها في المكدس للعودة إليها لاحقًا مباشرة. */
    public void pushFragment(@NonNull Fragment fragment, @Nullable String backStackName) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        tx.replace(R.id.fragment_container, fragment);
        tx.addToBackStack(backStackName);
        tx.commit();
    }

    /** يعود مباشرة إلى الشاشة التي تحمل هذا الاسم في المكدس (يزيل كل ما بعدها). */
    public void popToNamed(@NonNull String backStackName) {
        getSupportFragmentManager().popBackStack(backStackName, 0);
    }

    /** يمسح المكدس بالكامل ويعرض الشاشة الرئيسية (يُستخدم بعد بدء المغامرة). */
    public void resetToFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechUtil != null) speechUtil.shutdown();
        if (soundUtil != null) soundUtil.release();
        if (assetAudioPlayer != null) assetAudioPlayer.stop();
    }
}
