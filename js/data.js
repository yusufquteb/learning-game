// بيانات اللعبة: الحروف، الأشكال، الحيوانات
// كل الرسومات هنا مجانية 100%: إيموجي قياسي أو أشكال SVG مرسومة يدويًا

const LETTERS = [
  { letter: "أ", name: "أَلِف", word: "أسد", emoji: "🦁" },
  { letter: "ب", name: "باء", word: "بيت", emoji: "🏠" },
  { letter: "ت", name: "تاء", word: "تفاحة", emoji: "🍎" },
  { letter: "ث", name: "ثاء", word: "ثعلب", emoji: "🦊" },
  { letter: "ج", name: "جيم", word: "جزرة", emoji: "🥕" },
  { letter: "ح", name: "حاء", word: "حصان", emoji: "🐴" },
  { letter: "خ", name: "خاء", word: "خروف", emoji: "🐑" },
  { letter: "د", name: "دال", word: "دجاجة", emoji: "🐔" },
  { letter: "ذ", name: "ذال", word: "ذرة", emoji: "🌽" },
  { letter: "ر", name: "راء", word: "ريشة", emoji: "🪶" },
  { letter: "ز", name: "زاي", word: "زرافة", emoji: "🦒" },
  { letter: "س", name: "سين", word: "سمكة", emoji: "🐟" },
  { letter: "ش", name: "شين", word: "شمس", emoji: "☀️" },
  { letter: "ص", name: "صاد", word: "صابون", emoji: "🧼" },
  { letter: "ض", name: "ضاد", word: "ضفدع", emoji: "🐸" },
  { letter: "ط", name: "طاء", word: "طائرة", emoji: "✈️" },
  { letter: "ظ", name: "ظاء", word: "ظرف", emoji: "✉️" },
  { letter: "ع", name: "عين", word: "عصفور", emoji: "🐦" },
  { letter: "غ", name: "غين", word: "غزال", emoji: "🦌" },
  { letter: "ف", name: "فاء", word: "فيل", emoji: "🐘" },
  { letter: "ق", name: "قاف", word: "قطة", emoji: "🐱" },
  { letter: "ك", name: "كاف", word: "كلب", emoji: "🐶" },
  { letter: "ل", name: "لام", word: "ليمون", emoji: "🍋" },
  { letter: "م", name: "ميم", word: "موز", emoji: "🍌" },
  { letter: "ن", name: "نون", word: "نمر", emoji: "🐯" },
  { letter: "هـ", name: "هاء", word: "هدية", emoji: "🎁" },
  { letter: "و", name: "واو", word: "وردة", emoji: "🌹" },
  { letter: "ي", name: "ياء", word: "يد", emoji: "✋" },
];

// نقاط نجمة خماسية (10 رؤوس) محسوبة مسبقًا داخل viewBox 120x120
const STAR_POINTS =
  "60,8 72.93,42.2 109.46,43.9 80.92,66.8 90.57,102.07 60,82 29.43,102.07 39.08,66.8 10.54,43.9 47.07,42.2";

const HEART_PATH =
  "M23.6,0c-3.4,0-6.3,2.7-7.6,5.6C14.7,2.7,11.8,0,8.4,0C3.8,0,0,3.8,0,8.4c0,9.4,9.5,11.9,16,21.6c6.5-9.7,16-12.2,16-21.6C32,3.8,28.2,0,23.6,0z";

const SHAPES = [
  {
    id: "circle",
    name: "دائرة",
    color: "#4aa8ff",
    svg: (c) => `<svg viewBox="0 0 120 120"><circle cx="60" cy="60" r="52" fill="${c}"/></svg>`,
  },
  {
    id: "square",
    name: "مربع",
    color: "#ffa834",
    svg: (c) => `<svg viewBox="0 0 120 120"><rect x="14" y="14" width="92" height="92" rx="16" fill="${c}"/></svg>`,
  },
  {
    id: "triangle",
    name: "مثلث",
    color: "#5cc55c",
    svg: (c) => `<svg viewBox="0 0 120 120"><polygon points="60,12 12,104 108,104" fill="${c}"/></svg>`,
  },
  {
    id: "rectangle",
    name: "مستطيل",
    color: "#a463d9",
    svg: (c) => `<svg viewBox="0 0 120 120"><rect x="6" y="30" width="108" height="60" rx="14" fill="${c}"/></svg>`,
  },
  {
    id: "star",
    name: "نجمة",
    color: "#ffd23f",
    svg: (c) => `<svg viewBox="0 0 120 120"><polygon points="${STAR_POINTS}" fill="${c}"/></svg>`,
  },
  {
    id: "heart",
    name: "قلب",
    color: "#ff6b81",
    svg: (c) => `<svg viewBox="0 0 32 30"><path d="${HEART_PATH}" fill="${c}"/></svg>`,
  },
  {
    id: "oval",
    name: "بيضاوي",
    color: "#34c9c9",
    svg: (c) => `<svg viewBox="0 0 120 120"><ellipse cx="60" cy="60" rx="54" ry="34" fill="${c}"/></svg>`,
  },
  {
    id: "diamond",
    name: "معين",
    color: "#ff7043",
    svg: (c) => `<svg viewBox="0 0 120 120"><polygon points="60,8 112,60 60,112 8,60" fill="${c}"/></svg>`,
  },
];

const ANIMALS = [
  { name: "أسد", emoji: "🦁", fact: "ملك الغابة، وصوته زئير قوي." },
  { name: "بقرة", emoji: "🐄", fact: "تعطينا الحليب اللذيذ كل يوم." },
  { name: "خروف", emoji: "🐑", fact: "صوفه الناعم يُنسج لنا الملابس." },
  { name: "أرنب", emoji: "🐰", fact: "يقفز بسرعة ويحب أكل الجزر." },
  { name: "حصان", emoji: "🐴", fact: "يجري بسرعة كبيرة جدًا." },
  { name: "فيل", emoji: "🐘", fact: "أكبر حيوان بري، وله خرطوم طويل." },
  { name: "زرافة", emoji: "🦒", fact: "أطول حيوان في العالم كله." },
  { name: "دب", emoji: "🐻", fact: "ينام طوال فصل الشتاء البارد." },
  { name: "قطة", emoji: "🐱", fact: "تحب اللعب وشرب اللبن." },
  { name: "كلب", emoji: "🐶", fact: "صديق وفي يحب اللعب معنا." },
  { name: "دجاجة", emoji: "🐔", fact: "تعطينا بيضًا طازجًا كل يوم." },
  { name: "بطة", emoji: "🦆", fact: "تحب السباحة في الماء كثيرًا." },
  { name: "ضفدع", emoji: "🐸", fact: "يقفز عاليًا ويعيش قرب الماء." },
  { name: "سلحفاة", emoji: "🐢", fact: "تمشي ببطء لكنها تعيش طويلًا." },
  { name: "بطريق", emoji: "🐧", fact: "يعيش في الأماكن الباردة جدًا." },
  { name: "ثعلب", emoji: "🦊", fact: "ذكي جدًا وفروه جميل الألوان." },
];

function chunk(arr, size) {
  const out = [];
  for (let i = 0; i < arr.length; i += size) out.push(arr.slice(i, i + size));
  return out;
}

const CATEGORIES = {
  letters: {
    id: "letters",
    title: "الحروف",
    icon: "أ",
    color: "#4aa8ff",
    colorSoft: "#e8f4ff",
    items: LETTERS,
    stages: chunk(LETTERS, 4),
  },
  shapes: {
    id: "shapes",
    title: "الأشكال",
    icon: "🔺",
    color: "#a463d9",
    colorSoft: "#f3e9fb",
    items: SHAPES,
    stages: chunk(SHAPES, 2),
  },
  animals: {
    id: "animals",
    title: "الحيوانات",
    icon: "🦁",
    color: "#5cc55c",
    colorSoft: "#eaf9ea",
    items: ANIMALS,
    stages: chunk(ANIMALS, 4),
  },
};
