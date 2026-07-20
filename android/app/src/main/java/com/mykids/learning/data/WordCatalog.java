package com.mykids.learning.data;

import java.util.ArrayList;
import java.util.List;

/**
 * محتوى الحروف العربية/الإنجليزية، الأرقام، الأشكال، والألوان — مبني على أصول
 * حقيقية (صور ونطق مسجَّل) بدل الإيموجي والرسم اليدوي. البيانات مأخوذة من
 * مشروع "أول كلماتي" السابق مع الحفاظ على نفس المسارات داخل assets/.
 */
public final class WordCatalog {

    public static final List<WordItem> ARABIC_LETTERS = buildArabicLetters();
    public static final List<WordItem> ENGLISH_LETTERS = buildEnglishLetters();
    public static final List<WordItem> NUMBERS = buildNumbers();
    public static final List<WordItem> SHAPES = buildShapes();
    public static final List<WordItem> COLORS = buildColors();

    // كل صف: الحرف، اسم الحرف، اسم الحيوان بالعربية، مجلد صورة الحيوان، ملف صوت الحيوان، سطر الأغنية
    private static List<WordItem> buildArabicLetters() {
        List<WordItem> list = new ArrayList<>();
        addLetter(list, "alphabet", 1, "أ", "ألف", "أسد", "lion", "lion", "ألف أسد، زئيره يملأ الغابة");
        addLetter(list, "alphabet", 2, "ب", "باء", "بطة", "duck", "duck", "باء بطة، تعوم في الماء وتغني");
        addLetter(list, "alphabet", 3, "ت", "تاء", "تمساح", "crocodile", "crocodile", "تاء تمساح، يعيش في النيل العظيم");
        addLetter(list, "alphabet", 4, "ث", "ثاء", "ثعلب", "fox", "fox", "ثاء ثعلب، صاد دجاجة، هو مكار وقت الحاجة");
        addLetter(list, "alphabet", 5, "ج", "جيم", "جمل", "camel", "camel", "جيم جمل، يسير في الصحراء ولا يتعب");
        addLetter(list, "alphabet", 6, "ح", "حاء", "حصان", "horse", "horse", "حاء حصان، يجري ويطير كالريح");
        addLetter(list, "alphabet", 7, "خ", "خاء", "خروف", "sheep", "sheep", "خاء خروف، صوفه دافئ وناعم");
        addLetter(list, "alphabet", 8, "د", "دال", "دب", "bear", "bear", "دال دب كبير، يأكل العسل اللذيذ");
        addLetter(list, "alphabet", 9, "ذ", "ذال", "ذئب", "wolf", "wolf", "ذال ذئب، يعوي في الليل بصوت قوي");
        addLetter(list, "alphabet", 10, "ر", "راء", "غزال", "deer", "deer", "راء ريم، الغزال الجميل يقفز بخفة");
        addLetter(list, "alphabet", 11, "ز", "زاي", "زرافة", "giraffe", "giraffe", "زاي زرافة، عنقها يصل للسحاب");
        addLetter(list, "alphabet", 12, "س", "سين", "سمكة", "fish", "fish", "سين سمكة، تسبح في البحر بسرعة");
        addLetter(list, "alphabet", 13, "ش", "شين", "شمبانزي", "chimpanzee", "chimpanzee", "شين شمبانزي، يلعب على الأشجار ويقفز");
        addLetter(list, "alphabet", 14, "ص", "صاد", "نسر", "eagle", "vulture", "صاد صقر، يحلق عالياً في السماء");
        addLetter(list, "alphabet", 15, "ض", "ضاد", "ضفدع", "frog", "frog", "ضاد ضفدع، ينط ويغني بجانب النهر");
        addLetter(list, "alphabet", 16, "ط", "طاء", "طاووس", "peacock", "peacock", "طاء طاووس، يفرد ذيله الجميل الملوّن");
        addLetter(list, "alphabet", 17, "ظ", "ظاء", "نعامة", "ostrich", "ostrich", "ظاء ظليم، النعامة تجري أسرع من الريح");
        addLetter(list, "alphabet", 18, "ع", "عين", "عندليب", "nightingale", "nightingale", "عين عندليب، يغني أجمل أغنية في الحديقة");
        addLetter(list, "alphabet", 19, "غ", "غين", "غزال", "deer", "deer", "غين غزال رشيق، يعدو في السهول الخضراء");
        addLetter(list, "alphabet", 20, "ف", "فاء", "فيل", "elephant", "elephant", "فاء فيل كبير، أطول أنف في الحيوانات");
        addLetter(list, "alphabet", 21, "ق", "قاف", "قطة", "cat", "cat", "قاف قطة صغيرة، تموء وتلعب بالخيط");
        addLetter(list, "alphabet", 22, "ك", "كاف", "كوالا", "koala", "koala", "كاف كوالا، ينام على أشجار الأوكالبتوس");
        addLetter(list, "alphabet", 23, "ل", "لام", "ليمور", "lemur", "monkey", "لام ليمور، عيونه كبيرة ومضيئة في الليل");
        addLetter(list, "alphabet", 24, "م", "ميم", "قرد مرح", "monkey", "monkey", "ميم قرد مرح، يتأرجح على الأشجار بفرح");
        addLetter(list, "alphabet", 25, "ن", "نون", "نمر", "tiger", "tiger", "نون نمر، ملك الأدغال يمشي بكبرياء");
        addLetter(list, "alphabet", 26, "هـ", "هاء", "فرس النهر", "hippopotamus", "hippopotamus", "هاء هيبو، فرس النهر يحب الماء كثيراً");
        addLetter(list, "alphabet", 27, "و", "واو", "وحيد القرن", "rhinoceros", "rhinoceros", "واو وحيد القرن، له قرن قوي على أنفه");
        addLetter(list, "alphabet", 28, "ي", "ياء", "يمامة", "pigeon", "stork", "ياء يمامة، تطير في السماء وتحمل السلام");
        return list;
    }

    // كل صف: الحرف، اسم الحيوان بالعربية، بالإنجليزية، مجلد صورة الحيوان، ملف صوت الحيوان، الجملة
    private static List<WordItem> buildEnglishLetters() {
        List<WordItem> list = new ArrayList<>();
        addEnglishLetter(list, 1, "A", "تمساح", "Alligator", "crocodile", "crocodile", "A is for Alligator! A-A-Alligator snaps!");
        addEnglishLetter(list, 2, "B", "دب", "Bear", "bear", "bear", "B is for Bear! B-B-Bear loves honey!");
        addEnglishLetter(list, 3, "C", "قطة", "Cat", "cat", "cat", "C is for Cat! C-C-Cat goes meow!");
        addEnglishLetter(list, 4, "D", "كلب", "Dog", "dog", "dog", "D is for Dog! D-D-Dog goes woof!");
        addEnglishLetter(list, 5, "E", "فيل", "Elephant", "elephant", "elephant", "E is for Elephant! E-E-Elephant is so big!");
        addEnglishLetter(list, 6, "F", "ثعلب", "Fox", "fox", "fox", "F is for Fox! F-F-Fox is very clever!");
        addEnglishLetter(list, 7, "G", "زرافة", "Giraffe", "giraffe", "giraffe", "G is for Giraffe! G-G-Giraffe has a long neck!");
        addEnglishLetter(list, 8, "H", "حصان", "Horse", "horse", "horse", "H is for Horse! H-H-Horse runs fast!");
        addEnglishLetter(list, 9, "I", "إيغوانا", "Iguana", "iguana", "lizard", "I is for Iguana! I-I-Iguana loves the sun!");
        addEnglishLetter(list, 10, "J", "قنديل البحر", "Jellyfish", "jellyfish", "fish", "J is for Jellyfish! J-J-Jellyfish floats!");
        addEnglishLetter(list, 11, "K", "كنغر", "Kangaroo", "kangaroo", "kangaroo", "K is for Kangaroo! K-K-Kangaroo jumps high!");
        addEnglishLetter(list, 12, "L", "أسد", "Lion", "lion", "lion", "L is for Lion! L-L-Lion is the king!");
        addEnglishLetter(list, 13, "M", "قرد", "Monkey", "monkey", "monkey", "M is for Monkey! M-M-Monkey climbs trees!");
        addEnglishLetter(list, 14, "N", "بلبل", "Nightingale", "nightingale", "nightingale", "N is for Nightingale! N-N-Nightingale sings!");
        addEnglishLetter(list, 15, "O", "نعامة", "Ostrich", "ostrich", "ostrich", "O is for Ostrich! O-O-Ostrich runs so fast!");
        addEnglishLetter(list, 16, "P", "ببغاء", "Parrot", "parrot", "parrot", "P is for Parrot! P-P-Parrot can talk!");
        addEnglishLetter(list, 17, "Q", "سمان", "Quail", "quail", "quail", "Q is for Quail! Q-Q-Quail is very small!");
        addEnglishLetter(list, 18, "R", "أرنب", "Rabbit", "rabbit", "rabbit", "R is for Rabbit! R-R-Rabbit hops along!");
        addEnglishLetter(list, 19, "S", "ثعبان", "Snake", "snake", "snake", "S is for Snake! S-S-Snake goes ssss!");
        addEnglishLetter(list, 20, "T", "نمر", "Tiger", "tiger", "tiger", "T is for Tiger! T-T-Tiger has stripes!");
        addEnglishLetter(list, 21, "U", "قنفذ البحر", "Urchin", "urchin", "hedgehog", "U is for Urchin! U-U-Urchin is spiky!");
        addEnglishLetter(list, 22, "V", "نسر", "Vulture", "vulture", "vulture", "V is for Vulture! V-V-Vulture flies high!");
        addEnglishLetter(list, 23, "W", "ذئب", "Wolf", "wolf", "wolf", "W is for Wolf! W-W-Wolf howls at the moon!");
        addEnglishLetter(list, 24, "X", "ثعلب (في كلمة foX)", "Fox (foX)", "fox", "fox", "X is in foX! The sneaky FOX has an X!");
        addEnglishLetter(list, 25, "Y", "ياك", "Yak", "yak", "yak", "Y is for Yak! Y-Y-Yak lives in the mountains!");
        addEnglishLetter(list, 26, "Z", "حمار وحشي", "Zebra", "zebra", "zebra", "Z is for Zebra! Z-Z-Zebra has black and white stripes!");
        return list;
    }

    private static void addLetter(List<WordItem> list, String folder, int index, String letter,
                                   String letterName, String animalAr, String animalImageFolder,
                                   String soundFile, String songLyric) {
        String n = pad3(index);
        list.add(new WordItem(
                letter,
                letter,
                letterName,
                folder + "/images/" + n + "/Solution.png",
                folder + "/sounds/speech/Arabic/" + n + ".mp3",
                folder + "/sounds/speech/English/" + n + ".mp3",
                "animals/sounds/onomatopoeia/" + soundFile + ".mp3",
                songLyric,
                animalAr,
                "animals/images/" + animalImageFolder + "/Solution.png"));
    }

    private static void addEnglishLetter(List<WordItem> list, int index, String letter,
                                          String animalAr, String animalEn, String animalImageFolder,
                                          String soundFile, String caption) {
        String n = pad3(index);
        list.add(new WordItem(
                letter,
                letter,
                letter,
                "alphabet-e/images/" + n + "/Solution.png",
                "alphabet-e/sounds/speech/Arabic/" + n + ".mp3",
                "alphabet-e/sounds/speech/English/" + n + ".mp3",
                "animals/sounds/onomatopoeia/" + soundFile + ".mp3",
                caption,
                animalEn,
                "animals/images/" + animalImageFolder + "/Solution.png"));
    }

    private static List<WordItem> buildNumbers() {
        String[] ar = {"واحد", "اثنان", "ثلاثة", "أربعة", "خمسة", "ستة", "سبعة", "ثمانية", "تسعة", "عشرة"};
        String[] en = {"ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN"};
        List<WordItem> list = new ArrayList<>();
        for (int i = 0; i < ar.length; i++) {
            String n = pad3(i + 1);
            list.add(new WordItem(n, ar[i], en[i],
                    "numbers/images/" + n + "/Solution.png",
                    "numbers/sounds/speech/Arabic/" + n + ".mp3",
                    "numbers/sounds/speech/English/" + n + ".mp3",
                    null, null, null, null));
        }
        return list;
    }

    private static List<WordItem> buildShapes() {
        return buildFromMap("shapes", new String[][]{
                {"circle", "دائرة", "CIRCLE"},
                {"square", "مربع", "SQUARE"},
                {"triangle", "مثلث", "TRIANGLE"},
                {"rectangle", "مستطيل", "RECTANGLE"},
                {"star", "نجمة", "STAR"},
                {"heart", "قلب", "HEART"},
                {"oval", "بيضاوى", "OVAL"},
                {"diamond", "معين", "DIAMOND"},
                {"crescent", "هلال", "CRESCENT"},
                {"arrow", "سهم", "ARROW"},
        });
    }

    private static List<WordItem> buildColors() {
        return buildFromMap("colors", new String[][]{
                {"red", "أحمر", "RED"},
                {"blue", "أزرق", "BLUE"},
                {"yellow", "أصفر", "YELLOW"},
                {"green", "أخضر", "GREEN"},
                {"orange", "برتقالي", "ORANGE"},
                {"black", "أسود", "BLACK"},
                {"white", "أبيض", "WHITE"},
                {"brown", "بني", "BROWN"},
                {"pink", "وردى", "PINK"},
                {"grey", "رصاصي", "GREY"},
                {"violet", "بنفسجى", "VIOLET"},
        });
    }

    private static List<WordItem> buildFromMap(String folder, String[][] entries) {
        List<WordItem> list = new ArrayList<>();
        for (String[] e : entries) {
            String key = e[0];
            list.add(new WordItem(key, e[1], e[2],
                    folder + "/images/" + key + "/Solution.png",
                    folder + "/sounds/speech/Arabic/" + key + ".mp3",
                    folder + "/sounds/speech/English/" + key + ".mp3",
                    null, null, null, null));
        }
        return list;
    }

    private static String pad3(int n) {
        return (n < 10 ? "00" : n < 100 ? "0" : "") + n;
    }

    private WordCatalog() { }
}
