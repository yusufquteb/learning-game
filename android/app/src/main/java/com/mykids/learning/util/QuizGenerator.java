package com.mykids.learning.util;

import com.mykids.learning.data.AnimalItem;
import com.mykids.learning.data.Category;
import com.mykids.learning.data.GameData;
import com.mykids.learning.data.Letter;
import com.mykids.learning.data.ShapeItem;
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
        switch (cat.id) {
            case GameData.CAT_LETTERS: {
                Letter target = (Letter) item;
                List<Letter> distractors = sample(GameData.LETTERS, 2, l -> l.letter.equals(target.letter));
                List<Letter> shuffledLetters = new ArrayList<>();
                shuffledLetters.add(target);
                shuffledLetters.addAll(distractors);
                Collections.shuffle(shuffledLetters, RANDOM);
                List<Option> options = new ArrayList<>();
                for (Letter l : shuffledLetters) {
                    options.add(new Option(l.word, l.emoji, l.letter.equals(target.letter)));
                }
                return new Question(cat.id, "اختر الكلمة التي تبدأ بهذا الحرف", target, options);
            }
            case GameData.CAT_SHAPES: {
                ShapeItem target = (ShapeItem) item;
                List<ShapeItem> distractors = sample(GameData.SHAPES, 2, s -> s.id.equals(target.id));
                List<ShapeItem> shuffledShapes = new ArrayList<>();
                shuffledShapes.add(target);
                shuffledShapes.addAll(distractors);
                Collections.shuffle(shuffledShapes, RANDOM);
                List<Option> options = new ArrayList<>();
                for (ShapeItem s : shuffledShapes) {
                    options.add(new Option(s.name, null, s.id.equals(target.id)));
                }
                return new Question(cat.id, "ما اسم هذا الشكل؟", target, options);
            }
            default: {
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
        }
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
