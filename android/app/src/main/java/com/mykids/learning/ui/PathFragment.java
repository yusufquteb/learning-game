package com.mykids.learning.ui;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.mykids.learning.MainActivity;
import com.mykids.learning.R;
import com.mykids.learning.data.AnimalItem;
import com.mykids.learning.data.Category;
import com.mykids.learning.data.GameData;
import com.mykids.learning.data.Letter;
import com.mykids.learning.data.ShapeItem;
import com.mykids.learning.progress.ProgressManager;

import java.util.List;

public class PathFragment extends Fragment {

    private static final String ARG_CATEGORY = "category_id";

    public static PathFragment newInstance(String categoryId) {
        PathFragment f = new PathFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, categoryId);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_path, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) requireActivity();
        String categoryId = requireArguments().getString(ARG_CATEGORY);
        Category cat = GameData.CATEGORIES.get(categoryId);
        ProgressManager pm = activity.getProgressManager();

        view.findViewById(R.id.backButton).setOnClickListener(v -> requireActivity().onBackPressed());

        TextView title = view.findViewById(R.id.pathTitle);
        title.setText(cat.icon + " " + cat.title);

        TextView starsPill = view.findViewById(R.id.pathStars);
        starsPill.setText(getString(R.string.stars_fraction, pm.totalStars(categoryId), pm.maxStars(categoryId)));

        LinearLayout container = view.findViewById(R.id.stagesContainer);
        container.removeAllViews();

        int unlockedStage = pm.getUnlockedStage(categoryId);
        int catColor = ContextCompat.getColor(requireContext(), cat.colorRes);

        for (int i = 0; i < cat.stages.size(); i++) {
            boolean unlocked = i <= unlockedStage;
            int stars = pm.getStars(categoryId, i);
            View row = LayoutInflater.from(requireContext()).inflate(R.layout.item_stage_row, container, false);

            TextView badge = row.findViewById(R.id.badge);
            TextView rowTitle = row.findViewById(R.id.rowTitle);
            TextView rowStars = row.findViewById(R.id.rowStars);

            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.OVAL);

            if (unlocked) {
                bg.setColor(catColor);
                badge.setText(stars > 0 ? repeat("⭐", stars) : cat.icon);
                rowStars.setText(stars > 0 ? repeat("⭐", stars) + repeat("☆", 3 - stars) : getString(R.string.new_label));
            } else {
                bg.setColor(ContextCompat.getColor(requireContext(), R.color.locked_bg));
                badge.setText("🔒");
                rowStars.setText(getString(R.string.locked_label));
            }
            badge.setBackground(bg);
            rowTitle.setText(stageLabel(cat, cat.stages.get(i)));

            final int stageIndex = i;
            if (unlocked) {
                row.setOnClickListener(v -> activity.pushFragment(LearnFragment.newInstance(categoryId, stageIndex)));
            } else {
                row.setAlpha(0.65f);
            }
            container.addView(row);
        }
    }

    private String stageLabel(Category cat, List<Object> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (i > 0) sb.append(cat.id.equals(GameData.CAT_LETTERS) ? "  " : "  ·  ");
            if (item instanceof Letter) sb.append(((Letter) item).letter);
            else if (item instanceof ShapeItem) sb.append(((ShapeItem) item).name);
            else if (item instanceof AnimalItem) sb.append(((AnimalItem) item).name);
        }
        return sb.toString();
    }

    private String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }
}
