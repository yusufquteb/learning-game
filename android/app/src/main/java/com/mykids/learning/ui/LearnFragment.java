package com.mykids.learning.ui;

import android.graphics.drawable.GradientDrawable;
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
import com.mykids.learning.util.SpeechUtil;

import java.util.List;

public class LearnFragment extends Fragment {

    private static final String ARG_CATEGORY = "category_id";
    private static final String ARG_STAGE = "stage_index";

    private int currentIndex = 0;

    public static LearnFragment newInstance(String categoryId, int stageIndex) {
        LearnFragment f = new LearnFragment();
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
        return inflater.inflate(R.layout.fragment_learn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) requireActivity();
        String categoryId = requireArguments().getString(ARG_CATEGORY);
        int stageIndex = requireArguments().getInt(ARG_STAGE);
        Category cat = GameData.CATEGORIES.get(categoryId);
        List<Object> items = cat.stages.get(stageIndex);

        view.findViewById(R.id.closeButton).setOnClickListener(v -> requireActivity().onBackPressed());

        renderCard(view, cat, items);

        view.findViewById(R.id.prevButton).setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                renderCard(view, cat, items);
            }
        });
        view.findViewById(R.id.nextCardButton).setOnClickListener(v -> {
            if (currentIndex < items.size() - 1) {
                currentIndex++;
                renderCard(view, cat, items);
            }
        });

        MaterialButton bottomButton = view.findViewById(R.id.bottomActionButton);
        bottomButton.setOnClickListener(v -> {
            if (currentIndex < items.size() - 1) {
                currentIndex++;
                renderCard(view, cat, items);
            } else {
                activity.pushFragment(QuizFragment.newInstance(categoryId, stageIndex));
            }
        });
    }

    private void renderCard(View root, Category cat, List<Object> items) {
        Object item = items.get(currentIndex);

        TextView bigLetter = root.findViewById(R.id.bigLetter);
        TextView subName = root.findViewById(R.id.subName);
        ImageView shapeImage = root.findViewById(R.id.shapeImage);
        TextView bigEmoji = root.findViewById(R.id.bigEmoji);
        TextView mainWord = root.findViewById(R.id.mainWord);
        TextView factText = root.findViewById(R.id.factText);

        bigLetter.setVisibility(View.GONE);
        subName.setVisibility(View.GONE);
        shapeImage.setVisibility(View.GONE);
        bigEmoji.setVisibility(View.GONE);
        factText.setVisibility(View.GONE);

        String speakText;

        if (item instanceof Letter) {
            Letter l = (Letter) item;
            bigLetter.setText(l.letter);
            bigLetter.setVisibility(View.VISIBLE);
            subName.setText(l.name);
            subName.setVisibility(View.VISIBLE);
            bigEmoji.setText(l.emoji);
            bigEmoji.setVisibility(View.VISIBLE);
            mainWord.setText(l.word);
            speakText = l.name + "، " + l.word;
        } else if (item instanceof ShapeItem) {
            ShapeItem s = (ShapeItem) item;
            shapeImage.setImageResource(s.drawableRes);
            shapeImage.setVisibility(View.VISIBLE);
            mainWord.setText(s.name);
            speakText = s.name;
        } else {
            AnimalItem a = (AnimalItem) item;
            bigEmoji.setText(a.emoji);
            bigEmoji.setVisibility(View.VISIBLE);
            mainWord.setText(a.name);
            factText.setText(a.fact);
            factText.setVisibility(View.VISIBLE);
            speakText = a.name + "، " + a.fact;
        }

        SpeechUtil speechUtil = ((MainActivity) requireActivity()).getSpeechUtil();
        String finalSpeakText = speakText;
        root.findViewById(R.id.speakButton).setOnClickListener(v -> speechUtil.speak(finalSpeakText));

        ProgressBar progressBar = root.findViewById(R.id.learnProgress);
        progressBar.setProgress((int) (((currentIndex + 1) / (float) items.size()) * 100));

        renderDots(root, items.size());

        boolean isLast = currentIndex == items.size() - 1;
        MaterialButton bottomButton = root.findViewById(R.id.bottomActionButton);
        bottomButton.setText(isLast ? getString(R.string.start_quiz) : getString(R.string.next_button));

        root.findViewById(R.id.prevButton).setAlpha(currentIndex == 0 ? 0.35f : 1f);
        root.findViewById(R.id.nextCardButton).setAlpha(isLast ? 0.35f : 1f);
    }

    private void renderDots(View root, int count) {
        LinearLayout dotsContainer = root.findViewById(R.id.dotsContainer);
        dotsContainer.removeAllViews();
        int sizeInactive = dp(8);
        int sizeActive = dp(20);
        int margin = dp(3);

        for (int i = 0; i < count; i++) {
            View dot = new View(requireContext());
            boolean active = i == currentIndex;
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(dp(8));
            bg.setColor(ContextCompat.getColor(requireContext(),
                    active ? R.color.green : R.color.locked_bg));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    active ? sizeActive : sizeInactive, sizeInactive);
            lp.setMargins(margin, 0, margin, 0);
            dot.setLayoutParams(lp);
            dot.setBackground(bg);
            dotsContainer.addView(dot);
        }
    }

    private int dp(int value) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }
}
