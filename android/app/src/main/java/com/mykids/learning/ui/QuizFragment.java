package com.mykids.learning.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.mykids.learning.MainActivity;
import com.mykids.learning.R;
import com.mykids.learning.data.AnimalItem;
import com.mykids.learning.data.Category;
import com.mykids.learning.data.GameData;
import com.mykids.learning.data.Letter;
import com.mykids.learning.data.ShapeItem;
import com.mykids.learning.model.Option;
import com.mykids.learning.model.Question;
import com.mykids.learning.progress.ProgressManager;
import com.mykids.learning.util.QuizGenerator;
import com.mykids.learning.util.SoundUtil;

import java.util.List;

public class QuizFragment extends Fragment {

    private static final String ARG_CATEGORY = "category_id";
    private static final String ARG_STAGE = "stage_index";

    private List<Question> questions;
    private int currentIndex = 0;
    private int correctCount = 0;
    private boolean answered = false;

    public static QuizFragment newInstance(String categoryId, int stageIndex) {
        QuizFragment f = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, categoryId);
        args.putInt(ARG_STAGE, stageIndex);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) requireActivity();
        String categoryId = requireArguments().getString(ARG_CATEGORY);
        int stageIndex = requireArguments().getInt(ARG_STAGE);
        Category cat = GameData.CATEGORIES.get(categoryId);

        questions = QuizGenerator.buildQuizForStage(categoryId, stageIndex);

        view.findViewById(R.id.closeButton).setOnClickListener(v -> requireActivity().onBackPressed());

        renderQuestion(view, cat, categoryId, stageIndex);
    }

    private void renderQuestion(View root, Category cat, String categoryId, int stageIndex) {
        MainActivity activity = (MainActivity) requireActivity();
        Question q = questions.get(currentIndex);
        answered = false;

        ((TextView) root.findViewById(R.id.quizInstruction)).setText(q.instructionText);

        TextView subjectLetter = root.findViewById(R.id.subjectLetter);
        ImageView subjectShape = root.findViewById(R.id.subjectShape);
        TextView subjectEmoji = root.findViewById(R.id.subjectEmoji);
        subjectLetter.setVisibility(View.GONE);
        subjectShape.setVisibility(View.GONE);
        subjectEmoji.setVisibility(View.GONE);

        if (q.subjectItem instanceof Letter) {
            subjectLetter.setText(((Letter) q.subjectItem).letter);
            subjectLetter.setVisibility(View.VISIBLE);
        } else if (q.subjectItem instanceof ShapeItem) {
            subjectShape.setImageResource(((ShapeItem) q.subjectItem).drawableRes);
            subjectShape.setVisibility(View.VISIBLE);
        } else if (q.subjectItem instanceof AnimalItem) {
            subjectEmoji.setText(((AnimalItem) q.subjectItem).emoji);
            subjectEmoji.setVisibility(View.VISIBLE);
        }

        ProgressBar progressBar = root.findViewById(R.id.quizProgress);
        progressBar.setProgress((int) ((currentIndex / (float) questions.size()) * 100));

        TextView feedback = root.findViewById(R.id.feedbackBanner);
        feedback.setVisibility(View.GONE);
        MaterialButton nextButton = root.findViewById(R.id.nextQuestionButton);
        nextButton.setVisibility(View.GONE);
        nextButton.setText(currentIndex + 1 < questions.size()
                ? getString(R.string.next_question) : getString(R.string.show_result));

        LinearLayout optionsContainer = root.findViewById(R.id.optionsContainer);
        optionsContainer.removeAllViews();

        for (int i = 0; i < q.options.size(); i++) {
            Option opt = q.options.get(i);
            View optView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_quiz_option, optionsContainer, false);
            TextView label = optView.findViewById(R.id.optionLabel);
            TextView emoji = optView.findViewById(R.id.optionEmoji);
            label.setText(opt.label);
            if (opt.emoji != null && !opt.emoji.isEmpty()) {
                emoji.setText(opt.emoji);
                emoji.setVisibility(View.VISIBLE);
            } else {
                emoji.setVisibility(View.GONE);
            }

            final int idx = i;
            optView.setOnClickListener(v -> onAnswer(root, cat, categoryId, stageIndex, idx));
            optionsContainer.addView(optView);
        }

        nextButton.setOnClickListener(v -> {
            if (currentIndex + 1 < questions.size()) {
                currentIndex++;
                renderQuestion(root, cat, categoryId, stageIndex);
            } else {
                finishQuiz(cat, categoryId, stageIndex);
            }
        });
    }

    private void onAnswer(View root, Category cat, String categoryId, int stageIndex, int pickedIndex) {
        if (answered) return;
        answered = true;

        Question q = questions.get(currentIndex);
        boolean correct = q.options.get(pickedIndex).correct;
        SoundUtil soundUtil = ((MainActivity) requireActivity()).getSoundUtil();
        if (correct) {
            correctCount++;
            soundUtil.playCorrect();
        } else {
            soundUtil.playWrong();
        }

        LinearLayout optionsContainer = root.findViewById(R.id.optionsContainer);
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            View optView = optionsContainer.getChildAt(i);
            Option opt = q.options.get(i);
            if (opt.correct) {
                optView.setBackgroundResource(R.drawable.bg_quiz_option_correct);
            } else if (i == pickedIndex) {
                optView.setBackgroundResource(R.drawable.bg_quiz_option_wrong);
            }
            optView.setClickable(false);
        }

        TextView feedback = root.findViewById(R.id.feedbackBanner);
        feedback.setVisibility(View.VISIBLE);
        if (correct) {
            feedback.setText(R.string.feedback_correct);
            feedback.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_soft));
            feedback.setTextColor(ContextCompat.getColor(requireContext(), R.color.green));
        } else {
            feedback.setText(R.string.feedback_wrong);
            feedback.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink_soft));
            feedback.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
        }

        root.findViewById(R.id.nextQuestionButton).setVisibility(View.VISIBLE);
    }

    private void finishQuiz(Category cat, String categoryId, int stageIndex) {
        MainActivity activity = (MainActivity) requireActivity();
        ProgressManager pm = activity.getProgressManager();

        float pct = correctCount / (float) questions.size();
        int stars = pct >= 0.99f ? 3 : pct >= 0.6f ? 2 : pct > 0f ? 1 : 0;

        pm.recordStageResult(categoryId, stageIndex, stars, cat.stages.size());
        pm.addGems(stars);

        activity.pushFragment(ResultFragment.newInstance(categoryId, stageIndex, correctCount, questions.size(), stars));
    }
}
