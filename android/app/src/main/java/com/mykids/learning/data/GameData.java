package com.mykids.learning.data;

import com.mykids.learning.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** كل محتوى اللعبة: الحروف، الأشكال، الحيوانات (يطابق النسخة الإلكترونية). */
public final class GameData {

    public static final List<Letter> LETTERS = Arrays.asList(
            new Letter("أ", "أَلِف", "أسد", "🦁"),
            new Letter("ب", "باء", "بيت", "🏠"),
            new Letter("ت", "تاء", "تفاحة", "🍎"),
            new Letter("ث", "ثاء", "ثعلب", "🦊"),
            new Letter("ج", "جيم", "جزرة", "🥕"),
            new Letter("ح", "حاء", "حصان", "🐴"),
            new Letter("خ", "خاء", "خروف", "🐑"),
            new Letter("د", "دال", "دجاجة", "🐔"),
            new Letter("ذ", "ذال", "ذرة", "🌽"),
            new Letter("ر", "راء", "ريشة", "🪶"),
            new Letter("ز", "زاي", "زرافة", "🦒"),
            new Letter("س", "سين", "سمكة", "🐟"),
            new Letter("ش", "شين", "شمس", "☀️"),
            new Letter("ص", "صاد", "صابون", "🧼"),
            new Letter("ض", "ضاد", "ضفدع", "🐸"),
            new Letter("ط", "طاء", "طائرة", "✈️"),
            new Letter("ظ", "ظاء", "ظرف", "✉️"),
            new Letter("ع", "عين", "عصفور", "🐦"),
            new Letter("غ", "غين", "غزال", "🦌"),
            new Letter("ف", "فاء", "فيل", "🐘"),
            new Letter("ق", "قاف", "قطة", "🐱"),
            new Letter("ك", "كاف", "كلب", "🐶"),
            new Letter("ل", "لام", "ليمون", "🍋"),
            new Letter("م", "ميم", "موز", "🍌"),
            new Letter("ن", "نون", "نمر", "🐯"),
            new Letter("هـ", "هاء", "هدية", "🎁"),
            new Letter("و", "واو", "وردة", "🌹"),
            new Letter("ي", "ياء", "يد", "✋")
    );

    public static final List<ShapeItem> SHAPES = Arrays.asList(
            new ShapeItem("circle", "دائرة", R.drawable.shape_circle),
            new ShapeItem("square", "مربع", R.drawable.shape_square),
            new ShapeItem("triangle", "مثلث", R.drawable.shape_triangle),
            new ShapeItem("rectangle", "مستطيل", R.drawable.shape_rectangle),
            new ShapeItem("star", "نجمة", R.drawable.shape_star),
            new ShapeItem("heart", "قلب", R.drawable.shape_heart),
            new ShapeItem("oval", "بيضاوي", R.drawable.shape_oval),
            new ShapeItem("diamond", "معين", R.drawable.shape_diamond)
    );

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

    public static final String CAT_LETTERS = "letters";
    public static final String CAT_SHAPES = "shapes";
    public static final String CAT_ANIMALS = "animals";

    public static final Map<String, Category> CATEGORIES = new LinkedHashMap<>();

    static {
        CATEGORIES.put(CAT_LETTERS, new Category(
                CAT_LETTERS, "الحروف", "أ",
                R.color.blue, R.color.blue_soft,
                new ArrayList<>(LETTERS), chunk(new ArrayList<>(LETTERS), 4)));

        CATEGORIES.put(CAT_SHAPES, new Category(
                CAT_SHAPES, "الأشكال", "🔺",
                R.color.purple, R.color.purple_soft,
                new ArrayList<>(SHAPES), chunk(new ArrayList<>(SHAPES), 2)));

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
