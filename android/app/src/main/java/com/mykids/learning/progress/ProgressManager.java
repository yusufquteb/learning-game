package com.mykids.learning.progress;

import android.content.Context;
import android.content.SharedPreferences;

import com.mykids.learning.data.Category;
import com.mykids.learning.data.GameData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/** يحفظ اسم الطفل، النجوم، الجواهر، والمراحل المفتوحة محليًا على الجهاز. */
public class ProgressManager {

    private static final String PREFS = "learning_game_prefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_GEMS = "gems";
    private static final String KEY_STREAK = "streak";
    private static final String KEY_LAST_PLAYED = "last_played";
    private static final String KEY_DAILY_DONE = "daily_done";
    private static final String KEY_PROGRESS_PREFIX = "progress_";

    private static final SimpleDateFormat DAY_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private final SharedPreferences prefs;

    public ProgressManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public String getName() {
        return prefs.getString(KEY_NAME, "");
    }

    public void setName(String name) {
        prefs.edit().putString(KEY_NAME, name).apply();
    }

    public int getGems() {
        return prefs.getInt(KEY_GEMS, 0);
    }

    public void addGems(int amount) {
        prefs.edit().putInt(KEY_GEMS, getGems() + amount).apply();
    }

    public int getStreak() {
        return prefs.getInt(KEY_STREAK, 0);
    }

    /** يستدعى عند دخول الطفل للتطبيق؛ يحدّث سلسلة الأيام المتتالية. */
    public void touchStreak() {
        String today = todayStr();
        String last = prefs.getString(KEY_LAST_PLAYED, null);
        if (today.equals(last)) return;

        String yesterday = shiftedDayStr(-1);
        int newStreak = yesterday.equals(last) ? getStreak() + 1 : 1;
        prefs.edit()
                .putInt(KEY_STREAK, newStreak)
                .putString(KEY_LAST_PLAYED, today)
                .apply();
    }

    public boolean isDailyDone() {
        return todayStr().equals(prefs.getString(KEY_DAILY_DONE, ""));
    }

    public void markDailyDone() {
        prefs.edit().putString(KEY_DAILY_DONE, todayStr()).apply();
    }

    public int getUnlockedStage(String categoryId) {
        return readProgress(categoryId).unlockedStage;
    }

    public int getStars(String categoryId, int stageIndex) {
        Integer s = readProgress(categoryId).stars.get(stageIndex);
        return s == null ? 0 : s;
    }

    public int totalStars(String categoryId) {
        int sum = 0;
        for (int v : readProgress(categoryId).stars.values()) sum += v;
        return sum;
    }

    public int maxStars(String categoryId) {
        Category cat = GameData.CATEGORIES.get(categoryId);
        return cat == null ? 0 : GameData.maxStars(cat);
    }

    public int overallStars() {
        int sum = 0;
        for (String id : GameData.CATEGORIES.keySet()) sum += totalStars(id);
        return sum;
    }

    public int overallMaxStars() {
        int sum = 0;
        for (Category cat : GameData.CATEGORIES.values()) sum += GameData.maxStars(cat);
        return sum;
    }

    /** يسجّل نتيجة مرحلة، يحدّث أفضل عدد نجوم، ويفتح المرحلة التالية إن كانت هذه هي الحدّ الأقصى. */
    public void recordStageResult(String categoryId, int stageIndex, int stars, int totalStagesInCategory) {
        CategoryProgress p = readProgress(categoryId);
        int prevBest = p.stars.containsKey(stageIndex) ? p.stars.get(stageIndex) : 0;
        p.stars.put(stageIndex, Math.max(prevBest, stars));
        if (p.unlockedStage == stageIndex && p.unlockedStage < totalStagesInCategory - 1) {
            p.unlockedStage++;
        }
        writeProgress(categoryId, p);
    }

    private CategoryProgress readProgress(String categoryId) {
        CategoryProgress p = new CategoryProgress();
        String raw = prefs.getString(KEY_PROGRESS_PREFIX + categoryId, null);
        if (raw == null) return p;
        try {
            JSONObject obj = new JSONObject(raw);
            p.unlockedStage = obj.optInt("unlocked", 0);
            JSONObject stars = obj.optJSONObject("stars");
            if (stars != null) {
                java.util.Iterator<String> keys = stars.keys();
                while (keys.hasNext()) {
                    String k = keys.next();
                    p.stars.put(Integer.parseInt(k), stars.getInt(k));
                }
            }
        } catch (JSONException | NumberFormatException ignored) {
            // بيانات تالفة، نبدأ من جديد لهذا القسم فقط
        }
        return p;
    }

    private void writeProgress(String categoryId, CategoryProgress p) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("unlocked", p.unlockedStage);
            JSONObject stars = new JSONObject();
            for (Integer key : p.stars.keySet()) {
                stars.put(String.valueOf(key), p.stars.get(key));
            }
            obj.put("stars", stars);
            prefs.edit().putString(KEY_PROGRESS_PREFIX + categoryId, obj.toString()).apply();
        } catch (JSONException ignored) {
            // لن يحدث عمليًا مع هذا الهيكل البسيط
        }
    }

    private static String todayStr() {
        return DAY_FORMAT.format(Calendar.getInstance().getTime());
    }

    private static String shiftedDayStr(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, days);
        return DAY_FORMAT.format(cal.getTime());
    }
}
