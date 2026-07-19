// لعبة "مزرعة التعلّم" - منطق التطبيق (بدون أي مكتبات خارجية)

const STORAGE_KEY = "farm-learning-game-state-v1";
const $app = document.getElementById("app");

function todayStr() {
  const d = new Date();
  return `${d.getFullYear()}-${d.getMonth() + 1}-${d.getDate()}`;
}

function defaultState() {
  return {
    name: "",
    gems: 0,
    streak: 0,
    lastPlayedDate: null,
    dailyDoneDate: null,
    screen: "welcome",
    activeCategory: null,
    activeStageIndex: null,
    learnIndex: 0,
    quiz: null,
    progress: {
      letters: { unlockedStage: 0, stars: {} },
      shapes: { unlockedStage: 0, stars: {} },
      animals: { unlockedStage: 0, stars: {} },
    },
  };
}

function loadState() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return defaultState();
    const parsed = JSON.parse(raw);
    return Object.assign(defaultState(), parsed, {
      progress: Object.assign(defaultState().progress, parsed.progress),
    });
  } catch (e) {
    return defaultState();
  }
}

let state = loadState();

function saveState() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
}

function updateStreak() {
  const today = todayStr();
  if (state.lastPlayedDate === today) return;
  const y = new Date();
  y.setDate(y.getDate() - 1);
  const yesterday = `${y.getFullYear()}-${y.getMonth() + 1}-${y.getDate()}`;
  state.streak = state.lastPlayedDate === yesterday ? state.streak + 1 : 1;
  state.lastPlayedDate = today;
  saveState();
}

function escapeHtml(str) {
  return String(str).replace(/[&<>"']/g, (c) => ({
    "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;",
  }[c]));
}

function shuffled(arr) {
  const a = arr.slice();
  for (let i = a.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [a[i], a[j]] = [a[j], a[i]];
  }
  return a;
}

function sample(arr, n, excludeFn) {
  const pool = excludeFn ? arr.filter((x) => !excludeFn(x)) : arr.slice();
  return shuffled(pool).slice(0, n);
}

/* ---------------- audio feedback (no external files) ---------------- */
let audioCtx = null;
function tone(freq, duration, delay = 0, type = "sine", vol = 0.15) {
  try {
    audioCtx = audioCtx || new (window.AudioContext || window.webkitAudioContext)();
    const osc = audioCtx.createOscillator();
    const gain = audioCtx.createGain();
    osc.type = type;
    osc.frequency.value = freq;
    gain.gain.value = vol;
    osc.connect(gain).connect(audioCtx.destination);
    const t0 = audioCtx.currentTime + delay;
    gain.gain.setValueAtTime(vol, t0);
    gain.gain.exponentialRampToValueAtTime(0.001, t0 + duration);
    osc.start(t0);
    osc.stop(t0 + duration);
  } catch (e) { /* audio not available */ }
}
function playCorrect() { tone(523, 0.12, 0); tone(659, 0.12, 0.1); tone(784, 0.18, 0.2); }
function playWrong() { tone(220, 0.25, 0, "sawtooth", 0.1); }

function speak(text) {
  if (!("speechSynthesis" in window)) return;
  try {
    speechSynthesis.cancel();
    const u = new SpeechSynthesisUtterance(text);
    u.lang = "ar-SA";
    u.rate = 0.85;
    speechSynthesis.speak(u);
  } catch (e) { /* ignore */ }
}

/* ---------------- category helpers ---------------- */
function catMeta(id) { return CATEGORIES[id]; }

function totalStars(cat) {
  return Object.values(state.progress[cat.id].stars).reduce((a, b) => a + b, 0);
}
function maxStars(cat) { return cat.stages.length * 3; }

function overallStars() {
  return Object.values(CATEGORIES).reduce((sum, c) => sum + totalStars(c), 0);
}
function overallMax() {
  return Object.values(CATEGORIES).reduce((sum, c) => sum + maxStars(c), 0);
}

/* ---------------- daily challenge ---------------- */
function dailyItem() {
  const all = [];
  Object.values(CATEGORIES).forEach((cat) => {
    cat.items.forEach((item) => all.push({ cat: cat.id, item }));
  });
  const seed = new Date().getFullYear() * 1000 + dayOfYear();
  return all[seed % all.length];
}
function dayOfYear() {
  const d = new Date();
  const start = new Date(d.getFullYear(), 0, 0);
  return Math.floor((d - start) / 86400000);
}
function isDailyDone() { return state.dailyDoneDate === todayStr(); }

/* ---------------- quiz generation ---------------- */
function buildQuizForStage(catId, stageIndex) {
  const cat = catMeta(catId);
  const stageItems = cat.stages[stageIndex];
  const questions = stageItems.map((item) => {
    if (catId === "letters") {
      const distractors = sample(cat.items, 2, (x) => x.letter === item.letter);
      const options = shuffled([item, ...distractors]).map((o) => ({
        html: `<span class="opt-emoji">${o.emoji}</span><span>${o.word}</span>`,
        correct: o.letter === item.letter,
      }));
      return {
        instruction: "اختر الكلمة التي تبدأ بهذا الحرف",
        subjectHtml: `<div class="quiz-subject-letter">${item.letter}</div>`,
        options,
      };
    }
    if (catId === "shapes") {
      const distractors = sample(cat.items, 2, (x) => x.id === item.id);
      const options = shuffled([item, ...distractors]).map((o) => ({
        html: `<span>${o.name}</span>`,
        correct: o.id === item.id,
      }));
      return {
        instruction: "ما اسم هذا الشكل؟",
        subjectHtml: `<div class="quiz-subject-shape">${item.svg(item.color)}</div>`,
        options,
      };
    }
    // animals
    const distractors = sample(cat.items, 2, (x) => x.name === item.name);
    const options = shuffled([item, ...distractors]).map((o) => ({
      html: `<span>${o.name}</span>`,
      correct: o.name === item.name,
    }));
    return {
      instruction: "ما اسم هذا الحيوان؟",
      subjectHtml: `<div class="quiz-subject-emoji">${item.emoji}</div>`,
      options,
    };
  });
  return { questions: shuffled(questions), currentIndex: 0, correctCount: 0, answered: false };
}

/* ---------------- navigation ---------------- */
function go(screen, extra = {}) {
  state.screen = screen;
  Object.assign(state, extra);
  saveState();
  render();
  const appEl = document.getElementById("app");
  if (appEl) appEl.scrollTop = 0;
}

/* ---------------- render: welcome ---------------- */
function renderWelcome() {
  return `
    <div class="screen welcome-screen">
      <div class="welcome-mascot">🦁</div>
      <h1>أهلًا بك في مزرعة التعلّم!</h1>
      <p>تعلّم الحروف والأشكال والحيوانات بطريقة ممتعة</p>
      <input id="nameInput" class="name-input" type="text" maxlength="16" placeholder="اكتب اسمك هنا" value="${escapeHtml(state.name)}" />
      <button class="btn-primary" data-action="start-game">ابدأ المغامرة 🚀</button>
    </div>
  `;
}

/* ---------------- render: home ---------------- */
function renderHome() {
  const daily = dailyItem();
  const done = isDailyDone();
  const cats = Object.values(CATEGORIES);
  return `
    <div class="screen home-screen">
      <div class="topbar">
        <div class="stat-pill">🔥 ${state.streak}</div>
        <div class="stat-pill">💎 ${state.gems}</div>
        <div class="stat-pill">⭐ ${overallStars()}/${overallMax()}</div>
      </div>
      <div class="home-header">
        <div class="home-avatar">🧒</div>
        <div>
          <h1>أهلًا، ${escapeHtml(state.name) || "يا بطل"}!</h1>
          <span>هيا نتعلم شيئًا جديدًا اليوم</span>
        </div>
      </div>

      <div class="section-title">مسارات التعلّم</div>
      <div class="category-grid">
        ${cats.map(catCardHtml).join("")}
      </div>

      <div class="section-title">تحدّي اليوم</div>
      <div class="daily-card ${done ? "done" : ""}" data-action="open-daily">
        <div class="badge">${done ? "✅" : "🎯"}</div>
        <div class="info">
          <b>${done ? "أحسنت! أنجزت تحدي اليوم" : "هل تعرف هذا؟"}</b>
          <span>${done ? "عد غدًا لتحدٍّ جديد" : "اضغط لتجربة سؤال سريع واربح جواهر 💎"}</span>
        </div>
        ${done ? "" : '<div class="go">‹</div>'}
      </div>
      ${state.dailyModal ? renderDailyModal(daily) : ""}
    </div>
  `;
}

function catCardHtml(cat) {
  const pct = Math.round((totalStars(cat) / maxStars(cat)) * 100);
  return `
    <button class="category-card" style="background:${cat.colorSoft}; color:${cat.color}" data-action="open-category" data-cat="${cat.id}">
      <span class="emoji">${cat.icon}</span>
      <h3>${cat.title}</h3>
      <div class="progress-track"><div class="progress-fill" style="width:${pct}%; background:${cat.color}"></div></div>
      <span class="cat-count">${totalStars(cat)}⭐ من ${maxStars(cat)}</span>
    </button>
  `;
}

function renderDailyModal(daily) {
  if (!state.dailyQuestion) {
    const cat = catMeta(daily.cat);
    const item = daily.item;
    let subjectHtml, instruction, options;
    if (daily.cat === "letters") {
      instruction = "اختر الكلمة التي تبدأ بهذا الحرف";
      subjectHtml = `<div class="quiz-subject-letter">${item.letter}</div>`;
      const distractors = sample(cat.items, 2, (x) => x.letter === item.letter);
      options = shuffled([item, ...distractors]).map((o) => ({ html: `<span class="opt-emoji">${o.emoji}</span><span>${o.word}</span>`, correct: o.letter === item.letter }));
    } else if (daily.cat === "shapes") {
      instruction = "ما اسم هذا الشكل؟";
      subjectHtml = `<div class="quiz-subject-shape">${item.svg(item.color)}</div>`;
      const distractors = sample(cat.items, 2, (x) => x.id === item.id);
      options = shuffled([item, ...distractors]).map((o) => ({ html: `<span>${o.name}</span>`, correct: o.id === item.id }));
    } else {
      instruction = "ما اسم هذا الحيوان؟";
      subjectHtml = `<div class="quiz-subject-emoji">${item.emoji}</div>`;
      const distractors = sample(cat.items, 2, (x) => x.name === item.name);
      options = shuffled([item, ...distractors]).map((o) => ({ html: `<span>${o.name}</span>`, correct: o.name === item.name }));
    }
    state.dailyQuestion = { instruction, subjectHtml, options, answered: false };
  }
  const q = state.dailyQuestion;
  return `
    <div class="modal-overlay" data-action="noop">
      <div class="modal-card">
        <button class="modal-close" data-action="close-daily">✕</button>
        <h3>${q.instruction}</h3>
        ${q.subjectHtml}
        <div class="quiz-options">
          ${q.options.map((o, i) => `
            <button class="quiz-option ${q.answered ? (o.correct ? "correct" : (q.pickedIndex === i ? "wrong" : "")) : ""}" data-action="answer-daily" data-idx="${i}" ${q.answered ? "disabled" : ""}>
              ${o.html}
            </button>
          `).join("")}
        </div>
        ${q.answered ? `<button class="btn-primary" data-action="close-daily">تمام 👍</button>` : ""}
      </div>
    </div>
  `;
}

/* ---------------- render: path ---------------- */
function renderPath() {
  const cat = catMeta(state.activeCategory);
  const prog = state.progress[cat.id];
  const aligns = ["align-right", "align-center", "align-left", "align-center"];
  const nodes = cat.stages.map((stage, i) => {
    const unlocked = i <= prog.unlockedStage;
    const stars = prog.stars[i] || 0;
    const label = catId => catId === "letters"
      ? stage.map((s) => s.letter).join(" ")
      : stage.map((s) => s.name).join(" · ");
    return `
      <div class="path-node-row ${aligns[i % aligns.length]}">
        <button class="path-node ${unlocked ? "" : "locked"}" style="${unlocked ? `background:${cat.color}` : ""}" data-action="${unlocked ? "open-stage" : "noop"}" data-stage="${i}">
          ${unlocked ? (stars > 0 ? "★".repeat(stars) + "☆".repeat(3 - stars) : cat.icon) : "🔒"}
          ${unlocked ? `<span class="stars">${"⭐".repeat(stars)}${stars === 0 ? "جديد" : ""}</span>` : ""}
        </button>
      </div>
      <div class="path-node-label" style="text-align:${aligns[i % aligns.length] === "align-right" ? "right" : aligns[i % aligns.length] === "align-left" ? "left" : "center"}; padding:0 6px;">
        ${label(cat.id)}
      </div>
    `;
  }).join("");

  return `
    <div class="screen path-screen">
      <div class="path-header">
        <button class="icon-btn" data-action="go-home">→</button>
        <h2>${cat.icon} ${cat.title}</h2>
        <div class="stat-pill">⭐ ${totalStars(cat)}/${maxStars(cat)}</div>
      </div>
      <div class="path-track">
        ${nodes}
      </div>
    </div>
  `;
}

/* ---------------- render: learn ---------------- */
function renderLearn() {
  const cat = catMeta(state.activeCategory);
  const stage = cat.stages[state.activeStageIndex];
  const item = stage[state.learnIndex];
  const isLast = state.learnIndex === stage.length - 1;

  let cardInner, speakText;
  if (cat.id === "letters") {
    speakText = `${item.name}. ${item.word}`;
    cardInner = `
      <div class="big-letter">${item.letter}</div>
      <div class="sub-name">${item.name}</div>
      <div class="big-emoji">${item.emoji}</div>
      <div class="main-word">${item.word}</div>
    `;
  } else if (cat.id === "shapes") {
    speakText = item.name;
    cardInner = `
      <div class="big-shape">${item.svg(item.color)}</div>
      <div class="main-word">${item.name}</div>
    `;
  } else {
    speakText = `${item.name}. ${item.fact}`;
    cardInner = `
      <div class="big-emoji">${item.emoji}</div>
      <div class="main-word">${item.name}</div>
      <div class="fact">${item.fact}</div>
    `;
  }

  return `
    <div class="screen learn-screen">
      <div class="learn-top">
        <button class="icon-btn" data-action="back-to-path">✕</button>
        <div class="progress-bar-outer"><div class="progress-bar-inner" style="width:${((state.learnIndex + 1) / stage.length) * 100}%"></div></div>
      </div>
      <div class="flash-wrap">
        <div class="flashcard">
          ${cardInner}
          <button class="speak-btn" data-action="speak-card" data-text="${escapeHtml(speakText)}">🔊 استمع</button>
        </div>
        <div class="dots">
          ${stage.map((_, i) => `<span class="dot ${i === state.learnIndex ? "active" : ""}"></span>`).join("")}
        </div>
        <div class="flash-nav">
          <button class="nav-circle" data-action="learn-prev" ${state.learnIndex === 0 ? "disabled" : ""}>›</button>
          <button class="nav-circle" data-action="learn-next" ${isLast ? "disabled" : ""}>‹</button>
        </div>
      </div>
      <div class="bottom-action">
        <button class="btn-primary" data-action="start-quiz">${isLast ? "ابدأ الاختبار 📝" : "التالي"}</button>
      </div>
    </div>
  `;
}

/* ---------------- render: quiz ---------------- */
function renderQuiz() {
  const cat = catMeta(state.activeCategory);
  const q = state.quiz;
  const question = q.questions[q.currentIndex];
  const progressPct = ((q.currentIndex) / q.questions.length) * 100;

  return `
    <div class="screen quiz-screen">
      <div class="learn-top">
        <button class="icon-btn" data-action="back-to-path">✕</button>
        <div class="progress-bar-outer"><div class="progress-bar-inner" style="width:${progressPct}%; background:${cat.color}"></div></div>
      </div>
      <div class="quiz-prompt">
        <div class="quiz-instruction">${question.instruction}</div>
        ${question.subjectHtml}
      </div>
      <div class="quiz-options">
        ${question.options.map((o, i) => `
          <button class="quiz-option ${q.answered ? (o.correct ? "correct" : (q.pickedIndex === i ? "wrong" : "")) : ""}" data-action="answer-quiz" data-idx="${i}" ${q.answered ? "disabled" : ""}>
            ${o.html}
          </button>
        `).join("")}
      </div>
      ${q.answered ? `
        <div class="feedback-banner ${q.lastCorrect ? "correct" : "wrong"}">${q.lastCorrect ? "أحسنت! إجابة صحيحة 🎉" : "حاول مرة أخرى في المرة القادمة"}</div>
        <div class="bottom-action">
          <button class="btn-primary" data-action="next-question">${q.currentIndex + 1 < q.questions.length ? "السؤال التالي" : "عرض النتيجة"}</button>
        </div>
      ` : ""}
    </div>
  `;
}

/* ---------------- render: result ---------------- */
function renderResult() {
  const cat = catMeta(state.activeCategory);
  const q = state.quiz;
  const pct = q.correctCount / q.questions.length;
  const stars = pct >= 0.99 ? 3 : pct >= 0.6 ? 2 : pct > 0 ? 1 : 0;
  const prevBest = state.progress[cat.id].stars[state.activeStageIndex] || 0;
  const isLastStage = state.activeStageIndex === cat.stages.length - 1;

  return `
    <div class="screen result-screen">
      <div class="result-emoji">${stars >= 2 ? "🎉" : stars === 1 ? "👍" : "💪"}</div>
      <div class="result-stars">${"⭐".repeat(stars)}${"☆".repeat(3 - stars)}</div>
      <h2 class="result-title">${stars >= 2 ? "أحسنت صنعًا!" : "عمل جيد!"}</h2>
      <p class="result-sub">أجبت بشكل صحيح على ${q.correctCount} من ${q.questions.length} أسئلة${Math.max(stars, prevBest) > prevBest ? " • رصيدك تحسّن!" : ""}</p>
      <div class="result-actions">
        ${isLastStage ? "" : `<button class="btn-primary" data-action="next-stage">المرحلة التالية</button>`}
        <button class="btn-secondary" data-action="back-to-path">العودة للمسار</button>
      </div>
    </div>
  `;
}

/* ---------------- main render ---------------- */
function render() {
  let html = "";
  switch (state.screen) {
    case "welcome": html = renderWelcome(); break;
    case "home": html = renderHome(); break;
    case "path": html = renderPath(); break;
    case "learn": html = renderLearn(); break;
    case "quiz": html = renderQuiz(); break;
    case "result": html = renderResult(); break;
    default: html = renderHome();
  }
  $app.innerHTML = html;
}

/* ---------------- actions ---------------- */
function handleAction(action, el) {
  const cat = () => catMeta(state.activeCategory);

  if (action === "start-game") {
    const input = document.getElementById("nameInput");
    state.name = (input && input.value.trim()) || "بطل صغير";
    updateStreak();
    go("home");
    return;
  }

  if (action === "go-home") { go("home"); return; }

  if (action === "open-category") {
    go("path", { activeCategory: el.dataset.cat });
    return;
  }

  if (action === "open-stage") {
    const stageIndex = Number(el.dataset.stage);
    go("learn", { activeStageIndex: stageIndex, learnIndex: 0 });
    return;
  }

  if (action === "back-to-path") { go("path"); return; }

  if (action === "learn-next") {
    const stage = cat().stages[state.activeStageIndex];
    if (state.learnIndex < stage.length - 1) state.learnIndex++;
    saveState(); render();
    return;
  }
  if (action === "learn-prev") {
    if (state.learnIndex > 0) state.learnIndex--;
    saveState(); render();
    return;
  }
  if (action === "speak-card") { speak(el.dataset.text); return; }

  if (action === "start-quiz") {
    const stage = cat().stages[state.activeStageIndex];
    if (state.learnIndex < stage.length - 1) {
      state.learnIndex++;
      saveState(); render();
      return;
    }
    const quiz = buildQuizForStage(state.activeCategory, state.activeStageIndex);
    go("quiz", { quiz });
    return;
  }

  if (action === "answer-quiz") {
    const idx = Number(el.dataset.idx);
    const q = state.quiz;
    if (q.answered) return;
    const question = q.questions[q.currentIndex];
    const correct = question.options[idx].correct;
    q.answered = true;
    q.pickedIndex = idx;
    q.lastCorrect = correct;
    if (correct) { q.correctCount++; playCorrect(); } else { playWrong(); }
    saveState(); render();
    return;
  }

  if (action === "next-question") {
    const q = state.quiz;
    if (q.currentIndex + 1 < q.questions.length) {
      q.currentIndex++;
      q.answered = false;
      q.pickedIndex = null;
      saveState(); render();
    } else {
      const c = cat();
      const pct = q.correctCount / q.questions.length;
      const stars = pct >= 0.99 ? 3 : pct >= 0.6 ? 2 : pct > 0 ? 1 : 0;
      const prog = state.progress[c.id];
      prog.stars[state.activeStageIndex] = Math.max(prog.stars[state.activeStageIndex] || 0, stars);
      if (prog.unlockedStage === state.activeStageIndex && prog.unlockedStage < c.stages.length - 1) {
        prog.unlockedStage++;
      }
      state.gems += stars;
      go("result");
    }
    return;
  }

  if (action === "next-stage") {
    const nextIndex = state.activeStageIndex + 1;
    go("learn", { activeStageIndex: nextIndex, learnIndex: 0 });
    return;
  }

  if (action === "open-daily") {
    if (isDailyDone()) return;
    state.dailyModal = true;
    state.dailyQuestion = null;
    render();
    return;
  }
  if (action === "close-daily") {
    state.dailyModal = false;
    state.dailyQuestion = null;
    saveState(); render();
    return;
  }
  if (action === "answer-daily") {
    const idx = Number(el.dataset.idx);
    const q = state.dailyQuestion;
    if (q.answered) return;
    q.answered = true;
    q.pickedIndex = idx;
    const correct = q.options[idx].correct;
    if (correct) {
      playCorrect();
      state.gems += 5;
      state.dailyDoneDate = todayStr();
    } else {
      playWrong();
      state.dailyDoneDate = todayStr();
    }
    saveState(); render();
    return;
  }
}

$app.addEventListener("click", (e) => {
  const el = e.target.closest("[data-action]");
  if (!el) return;
  handleAction(el.dataset.action, el);
});

document.addEventListener("keydown", (e) => {
  if (e.key === "Enter" && state.screen === "welcome") {
    handleAction("start-game");
  }
});

/* ---------------- boot ---------------- */
if (state.name) {
  updateStreak();
  if (state.screen === "welcome") state.screen = "home";
}
render();
