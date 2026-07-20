package com.mykids.learning.data;

import com.mykids.learning.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** كل محتوى اللعبة: الحروف، الأرقام، الأشكال، الألوان (أصول حقيقية)، والحيوانات (إيموجي). */
public final class GameData {

    public static final List<AnimalItem> ANIMALS = Arrays.asList(
            new AnimalItem("أسد", "🦁", "ملك الغابة، وصوته زئير قوي."),
            new AnimalItem("بقرة", "🐄", "تعطينا الحليب اللذيذ كل يوم."),
            new AnimalItem("خروف", "🐑", "صوفه الناعم يُنسج لنا الملابس."),
            new AnimalItem("أرنب", "🐰", "يقفز بسرعة ويحب أكل الجزر."),
            new AnimalItem("حصان", "🐴", "يجري بسرعة كبيرة جدًا."),
            new AnimalItem("فيل", "🐘", "أكبر حيوان بري، وله خرطوم طويل."),
            new AnimalItem("زرافة", "🦒", "أطول حيوان في العالم كله."),
            new AnimalItem("دب", "🐻", "ينام طوال فصل الشتاء البارد."),
            new AnimalItem("قطة", "🐱", "تحب اللعب وشرب اللبن."),
            new AnimalItem("كلب", "🐶", "صديق وفي يحب اللعب معنا."),
            new AnimalItem("دجاجة", "🐔", "تعطينا بيضًا طازجًا كل يوم."),
            new AnimalItem("بطة", "🦆", "تحب السباحة في الماء كثيرًا."),
            new AnimalItem("ضفدع", "🐸", "يقفز عاليًا ويعيش قرب الماء."),
            new AnimalItem("سلحفاة", "🐢", "تمشي ببطء لكنها تعيش طويلًا."),
            new AnimalItem("بطريق", "🐧", "يعيش في الأماكن الباردة جدًا."),
            new AnimalItem("ثعلب", "🦊", "ذكي جدًا وفروه جميل الألوان.")
    );

    public static final String CAT_LETTERS_AR = "alphabet";
    public static final String CAT_LETTERS_EN = "alphabet-e";
    public static final String CAT_NUMBERS = "numbers";
    public static final String CAT_SHAPES = "shapes";
    public static final String CAT_COLORS = "colors";
    public static final String CAT_ANIMALS = "animals";

    public static final Map<String, Category> CATEGORIES = new LinkedHashMap<>();

    static {
        CATEGORIES.put(CAT_LETTERS_AR, new Category(
                CAT_LETTERS_AR, "الحروف العربية", "أ",
                R.color.blue, R.color.blue_soft,
                new ArrayList<>(WordCatalog.ARABIC_LETTERS), chunk(WordCatalog.ARABIC_LETTERS, 4)));

        CATEGORIES.put(CAT_LETTERS_EN, new Category(
                CAT_LETTERS_EN, "الحروف الإنجليزية", "A",
                R.color.orange, R.color.orange_soft,
                new ArrayList<>(WordCatalog.ENGLISH_LETTERS), chunk(WordCatalog.ENGLISH_LETTERS, 4)));

        CATEGORIES.put(CAT_NUMBERS, new Category(
                CAT_NUMBERS, "الأرقام", "١٢٣",
                R.color.teal, R.color.teal_soft,
                new ArrayList<>(WordCatalog.NUMBERS), chunk(WordCatalog.NUMBERS, 2)));

        CATEGORIES.put(CAT_SHAPES, new Category(
                CAT_SHAPES, "الأشكال", "🔺",
                R.color.purple, R.color.purple_soft,
                new ArrayList<>(WordCatalog.SHAPES), chunk(WordCatalog.SHAPES, 2)));

        CATEGORIES.put(CAT_COLORS, new Category(
                CAT_COLORS, "الألوان", "●",
                R.color.pink, R.color.pink_soft,
                new ArrayList<>(WordCatalog.COLORS), chunk(WordCatalog.COLORS, 3)));

        CATEGORIES.put(CAT_ANIMALS, new Category(
                CAT_ANIMALS, "الحيوانات", "🦁",
                R.color.green, R.color.green_soft,
                new ArrayList<>(ANIMALS), chunk(new ArrayList<>(ANIMALS), 4)));
    }

    private static List<List<Object>> chunk(List<?> source, int size) {
        List<List<Object>> out = new ArrayList<>();
        for (int i = 0; i < source.size(); i += size) {
            List<Object> group = new ArrayList<>();
            for (int j = i; j < Math.min(i + size, source.size()); j++) {
                group.add(source.get(j));
            }
            out.add(group);
        }
        return out;
    }

    public static int maxStars(Category cat) {
        return cat.stages.size() * 3;
    }

    private GameData() { }
}
