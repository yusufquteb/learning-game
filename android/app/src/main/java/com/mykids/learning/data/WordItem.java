package com.mykids.learning.data;

/**
 * عنصر تعليمي مبني على أصول حقيقية (صورة + نطق مسجَّل)، بدل الإيموجي/الرسم اليدوي.
 * يُستخدم في الحروف العربية والإنجليزية، الأرقام، الأشكال، والألوان.
 */
public class WordItem {
    public final String key;
    public final String nameAr;
    public final String nameEn;
    public final String imageAssetPath;
    public final String soundArAssetPath;
    public final String soundEnAssetPath;
    /** صوت الحيوان الحقيقي (onomatopoeia) — للحروف فقط، قد تكون null. */
    public final String soundEffectAssetPath;
    /** سطر من أغنية الحروف — للحروف فقط، قد تكون null. */
    public final String caption;
    /** الكلمة المرافقة للحرف (اسم الحيوان)، قد تكون null. */
    public final String pairedLabel;
    /** صورة الحيوان المرافق للحرف، قد تكون null. */
    public final String pairedImageAssetPath;

    public WordItem(String key, String nameAr, String nameEn, String imageAssetPath,
                     String soundArAssetPath, String soundEnAssetPath,
                     String soundEffectAssetPath, String caption, String pairedLabel,
                     String pairedImageAssetPath) {
        this.key = key;
        this.nameAr = nameAr;
        this.nameEn = nameEn;
        this.imageAssetPath = imageAssetPath;
        this.soundArAssetPath = soundArAssetPath;
        this.soundEnAssetPath = soundEnAssetPath;
        this.soundEffectAssetPath = soundEffectAssetPath;
        this.caption = caption;
        this.pairedLabel = pairedLabel;
        this.pairedImageAssetPath = pairedImageAssetPath;
    }
}
