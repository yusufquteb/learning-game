# مزرعة التعلّم — تطبيق أندرويد (Java)

نسخة أندرويد أصلية (Native, Java, بدون WebView) تدمج مشروعين: تصميم وتفاعل "مزرعة التعلّم" الإلكترونية
(المسارات، النجوم، الجواهر، الاختبارات، التحدي اليومي) + محتوى مشروع "أول كلماتي" السابق (صور ونطق صوتي
حقيقي بالعربية والإنجليزية). جاهزة للبناء ورفعها على Google Play.

## الأقسام الحالية (مجموعة "الأساسيات")

| القسم | العدد | المحتوى |
|---|---|---|
| الحروف العربية | 28 | حرف + حيوان مرافق + سطر أغنية + نطق AR/EN + صوت الحيوان الحقيقي |
| الحروف الإنجليزية | 26 | نفس الفكرة بجملة تعريفية إنجليزية |
| الأرقام | 10 | صورة + نطق AR/EN |
| الأشكال | 10 | صورة + نطق AR/EN |
| الألوان | 11 | صورة + نطق AR/EN |
| الحيوانات | 16 | إيموجي + حقيقة قصيرة (بدون تغيير — من مزرعة التعلّم الأصلية) |

باقي أقسام "أول كلماتي" (حيوانات موسّعة، طعام، مركبات، مطبخ، ...) لم تُضَف بعد — راجع سيناريو الأقسام
المتفق عليه لإضافتها لاحقًا بنفس الطريقة الموضحة أدناه.

## المتطلبات

- **Android Studio** (أحدث إصدار مستقر) — أسهل طريقة للفتح والبناء والتشغيل.
- أو **Gradle + Android SDK** مثبتين يدويًا إن كنت تفضل سطر الأوامر.

> ملاحظة: هذا المشروع بُني في بيئة بدون اتصال بخوادم Google (لا يوجد Android SDK هنا)، لذلك لم يُختبر ببناء APK فعلي داخل هذه الجلسة. راجعته بعناية سطرًا بسطر (تطابق كل `R.id` / `R.drawable` / `R.string` / `R.color` بين الكود و XML)، لكن **الرجاء فتحه في Android Studio أولًا والتأكد من نجاح Gradle Sync قبل أي تعديل إضافي.**

## فتح المشروع

1. افتح Android Studio → Open → اختر مجلد `android/` هذا (وليس مجلد المستودع الكامل).
2. سيقوم Android Studio تلقائيًا بإنشاء ملف `gradle-wrapper.jar` المفقود (لم يُرفق لأنه ملف ثنائي ولا يوجد اتصال إنترنت في بيئة الإنشاء). إن طلب منك الأمر، اختر "Use Gradle from: 'wrapper'" أو ببساطة اضغط Sync وسيتم تحميله.
3. انتظر Gradle Sync، ثم شغّل التطبيق (Run ▶) على محاكي أو جهاز حقيقي.

### البناء من سطر الأوامر (اختياري)

```bash
cd android
gradle wrapper --gradle-version 8.9   # يولّد gradlew وgradlew.bat مرة واحدة فقط
./gradlew assembleDebug               # ينتج APK تجريبي
./gradlew bundleRelease               # ينتج AAB لرفعه على Google Play (يحتاج توقيع)
```

## هيكل المشروع

```
app/src/main/java/com/mykids/learning/
  MainActivity.java        نقطة الدخول، يدير التنقل بين الشاشات (Fragments)
  data/                     نماذج البيانات: WordItem (حروف/أرقام/أشكال/ألوان — أصول حقيقية)،
                            AnimalItem (إيموجي)، Category، GameData (تجميع الأقسام)، WordCatalog (المحتوى الكامل)
  progress/                 حفظ التقدّم محليًا (SharedPreferences): الاسم، النجوم، الجواهر، السلسلة اليومية
  util/                     توليد الأسئلة، تحميل صور assets/ (AssetImageLoader)، تشغيل mp3 من assets/
                            (AssetAudioPlayer)، TextToSpeech (للحيوانات فقط)، أصوات تفاعل (ToneGenerator)، فرض العربية
  model/                    Question / Option لبناء أسئلة الاختبار
  ui/                       كل الشاشات (Welcome, Home, Path, Learn, Quiz, Result, Daily dialog)

app/src/main/assets/
  alphabet/, alphabet-e/, numbers/, shapes/, colors/, animals/
                            صور Solution.png + ملفات نطق mp3 (عربي/إنجليزي) لكل عنصر، منقولة من مشروع
                            "أول كلماتي" بنفس المسارات الأصلية بالضبط

app/src/main/res/
  drawable/                 خلفيات وأزرار مرسومة يدويًا (لا صور خارجية)
  layout/                   تخطيطات كل شاشة
  values/                   الألوان، النصوص العربية، الأنماط (Styles)
  font/cairo.ttf            خط Cairo المجاني (Google Fonts / OFL) — غير مفعّل افتراضيًا بعد
  mipmap-anydpi-v26/        أيقونة التطبيق (Adaptive Icon)
```

## عن الرسومات والأصوات

- محتوى الحروف/الأرقام/الأشكال/الألوان: صور (`Solution.png`) وملفات نطق (`mp3`) حقيقية منقولة من مشروعك
  السابق "أول كلماتي"، مبنية على `AssetManager` (لا حاجة لأي إنترنت وقت التشغيل).
- محتوى الحيوانات (16 عنصرًا القديمة فقط، غير الموسّعة): إيموجي قياسي + نطق آلي (TextToSpeech).
- **⚠ تنبيه ترخيص**: صور "أول كلماتي" (٧ مجلدات: alphabet, alphabet-e, numbers, shapes, colors, animals
  الفرعية) نزّلها صاحب المشروع من الإنترنت "بدون التحقق من الحقوق" (حسب إفادته). تم استخدامها بناءً على
  موافقته الصريحة على المخاطرة وتبديلها لاحقًا إن ظهرت مشكلة عند المراجعة على Google Play. إن رفضت مراجعة
  جوجل التطبيق أو ظهر بلاغ حقوق ملكية، استبدل الصور المعنية بأصول موثّقة الترخيص (كما في مسار
  `مزرعة التعلّم` الأصلي القائم على SVG/إيموجي بالكامل).
- أيقونة التطبيق (Adaptive Icon) مرسومة كـ Vector بسيط (وجه أسد ودود) — يمكنك استبدالها بتصميم احترافي لاحقًا عبر Image Asset Studio في Android Studio.

## إضافة الإعلانات لاحقًا (AdMob)

تم تجهيز المشروع لإضافة الإعلانات بسهولة:

1. أنشئ حساب [AdMob](https://admob.google.com) واحصل على **App ID** و **Ad Unit ID** لإعلان بانر (وربما إعلان بيني Interstitial).
2. في `app/build.gradle`، فعّل السطر المُعلَّق:
   ```gradle
   implementation 'com.google.android.gms:play-services-ads:23.3.0'
   ```
3. أضف App ID داخل `AndroidManifest.xml` ضمن `<application>`:
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY" />
   ```
4. في `MainActivity.java` يوجد بالفعل حاوية جاهزة `ad_container` (في `activity_main.xml`) — أضف فيها `AdView` وحمّل الإعلان، أو أظهر إعلان بيني بين المراحل (مثلًا بعد كل نتيجة اختبار في `ResultFragment`).
5. لا تنسَ استخدام **Test Ad Unit IDs** أثناء التطوير لتفادي حظر الحساب.

## قبل الرفع على Google Play

- **اسم الحزمة** الحالي: `com.mykids.learning` (لا يمكن تغييره بعد أول نشر).
- أنشئ **Keystore** للتوقيع (`Build > Generate Signed Bundle/APK` في Android Studio) واحتفظ به في مكان آمن — فقدانه يمنعك من تحديث التطبيق مستقبلًا.
- اجعل `versionCode` / `versionName` في `app/build.gradle` يزدادان مع كل إصدار جديد.
- جهّز: أيقونة 512×512، صور شاشة (Screenshots)، وصف التطبيق، وسياسة الخصوصية (**مطلوبة إلزاميًا** لأي تطبيق أطفال على Google Play، خصوصًا إذا أضفت إعلانات — راجع متطلبات [Google Play Families Policy](https://play.google.com/console/about/families/)).
- إن كان التطبيق موجّهًا للأطفال، ستحتاج للتسجيل في برنامج **Designed for Families** والالتزام بقيود خاصة على الإعلانات (مثلًا حظر إعلانات غير مناسبة للأطفال عبر AdMob's "Tag for child-directed treatment").
