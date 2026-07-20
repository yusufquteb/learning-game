package com.mykids.learning.util;

import com.mykids.learning.data.AnimalItem;
import com.mykids.learning.data.Category;
import com.mykids.learning.data.GameData;
import com.mykids.learning.data.WordItem;
import com.mykids.learning.model.Option;
import com.mykids.learning.model.Question;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** يبني أسئلة الاختبار (اختيار من متعدد) لكل قسم، بنفس منطق النسخة الإلكترونية. */
public final class QuizGenerator {

    private static final Random RANDOM = new Random();

    public static List<Question> buildQuizForStage(String categoryId, int stageIndex) {
        Category cat = GameData.CATEGORIES.get(categoryId);
        List<Object> stageItems = cat.stages.get(stageIndex);
        List<Question> questions = new ArrayList<>();
        for (Object item : stageItems) {
            questions.add(buildQuestion(cat, item));
        }
        Collections.shuffle(questions, RANDOM);
        return questions;
    }

    public static Question buildQuestion(Category cat, Object item) {
        if (item instanceof WordItem) {
            WordItem target = (WordItem) item;
            List<Object> distractors = sample(cat.items, 2, w -> ((WordItem) w).key.equals(target.key));
            List<WordItem> shuffled = new ArrayList<>();
            shuffled.add(target);
            for (Object d : distractors) shuffled.add((WordItem) d);
            Collections.shuffle(shuffled, RANDOM);
            List<Option> options = new ArrayList<>();
            for (WordItem w : shuffled) {
                options.add(new Option(w.nameAr, null, w.key.equals(target.key)));
            }
            return new Question(cat.id, wordInstruction(cat.id), target, options);
        }

        AnimalItem target = (AnimalItem) item;
        List<AnimalItem> distractors = sample(GameData.ANIMALS, 2, a -> a.name.equals(target.name));
        List<AnimalItem> shuffledAnimals = new ArrayList<>();
        shuffledAnimals.add(target);
        shuffledAnimals.addAll(distractors);
        Collections.shuffle(shuffledAnimals, RANDOM);
        List<Option> options = new ArrayList<>();
        for (AnimalItem a : shuffledAnimals) {
            options.add(new Option(a.name, null, a.name.equals(target.name)));
        }
        return new Question(cat.id, "ما اسم هذا الحيوان؟", target, options);
    }

    private static String wordInstruction(String categoryId) {
        if (categoryId.equals(GameData.CAT_LETTERS_AR) || categoryId.equals(GameData.CAT_LETTERS_EN)) {
            return "ما هذا الحرف؟";
        }
        if (categoryId.equals(GameData.CAT_NUMBERS)) return "ما هذا الرقم؟";
        if (categoryId.equals(GameData.CAT_COLORS)) return "ما هذا اللون؟";
        return "ما اسم هذا الشكل؟";
    }

    /** يختار عنصر اليوم بشكل ثابت طوال اليوم (نفس المنطق في النسخة الإلكترونية). */
    public static DailyPick dailyItem() {
        List<DailyPick> all = new ArrayList<>();
        for (Category cat : GameData.CATEGORIES.values()) {
            for (Object item : cat.items) {
                all.add(new DailyPick(cat, item));
            }
        }
        Calendar cal = Calendar.getInstance();
        int seed = cal.get(Calendar.YEAR) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
        return all.get(Math.floorMod(seed, all.size()));
    }

    private interface ExcludePredicate<T> {
        boolean exclude(T item);
    }

    private static <T> List<T> sample(List<T> source, int n, ExcludePredicate<T> excludeFn) {
        List<T> pool = new ArrayList<>();
        for (T t : source) if (!excludeFn.exclude(t)) pool.add(t);
        Collections.shuffle(pool, RANDOM);
        return pool.subList(0, Math.min(n, pool.size()));
    }

    public static class DailyPick {
        public final Category category;
        public final Object item;

        DailyPick(Category category, Object item) {
            this.category = category;
            this.item = item;
        }
    }

    private QuizGenerator() { }
}
