package com.mykids.learning.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mykids.learning.MainActivity;
import com.mykids.learning.R;
import com.mykids.learning.data.Category;
import com.mykids.learning.data.GameData;

public class ResultFragment extends Fragment {

    private static final String ARG_CATEGORY = "category_id";
    private static final String ARG_STAGE = "stage_index";
    private static final String ARG_CORRECT = "correct_count";
    private static final String ARG_TOTAL = "total";
    private static final String ARG_STARS = "stars";

    public static ResultFragment newInstance(String categoryId, int stageIndex, int correct, int total, int stars) {
        ResultFragment f = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, categoryId);
        args.putInt(ARG_STAGE, stageIndex);
        args.putInt(ARG_CORRECT, correct);
        args.putInt(ARG_TOTAL, total);
        args.putInt(ARG_STARS, stars);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) requireActivity();
        String categoryId = requireArguments().getString(ARG_CATEGORY);
        int stageIndex = requireArguments().getInt(ARG_STAGE);
        int correct = requireArguments().getInt(ARG_CORRECT);
        int total = requireArguments().getInt(ARG_TOTAL);
        int stars = requireArguments().getInt(ARG_STARS);
        Category cat = GameData.CATEGORIES.get(categoryId);
        boolean isLastStage = stageIndex == cat.stages.size() - 1;

        ((TextView) view.findViewById(R.id.resultEmoji))
                .setText(stars >= 2 ? "🎉" : stars == 1 ? "👍" : "💪");
        ((TextView) view.findViewById(R.id.resultStars))
                .setText(repeat("⭐", stars) + repeat("☆", 3 - stars));
        ((TextView) view.findViewById(R.id.resultTitle))
                .setText(stars >= 2 ? R.string.result_great : R.string.result_good);
        ((TextView) view.findViewById(R.id.resultSubtitle))
                .setText(getString(R.string.result_subtitle, correct, total));

        View nextStageButton = view.findViewById(R.id.nextStageButton);
        if (isLastStage) {
            nextStageButton.setVisibility(View.GONE);
        } else {
            nextStageButton.setOnClickListener(v -> {
                activity.popToNamed("path");
                activity.pushFragment(LearnFragment.newInstance(categoryId, stageIndex + 1));
            });
        }

        view.findViewById(R.id.backToPathButton).setOnClickListener(v -> activity.popToNamed("path"));
    }

    private String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }
}
