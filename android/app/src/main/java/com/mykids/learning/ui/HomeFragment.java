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
import com.mykids.learning.data.Category;
import com.mykids.learning.data.GameData;
import com.mykids.learning.progress.ProgressManager;

public class HomeFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buildCategoryCards(view);
        refreshUI(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rootView != null) refreshUI(rootView);
    }

    private void buildCategoryCards(View view) {
        MainActivity activity = (MainActivity) requireActivity();
        LinearLayout container = view.findViewById(R.id.categoriesContainer);
        container.removeAllViews();

        for (Category cat : GameData.CATEGORIES.values()) {
            View card = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_category_card, container, false);

            View cardRoot = card.findViewById(R.id.cardRoot);
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(dp(22));
            bg.setColor(ContextCompat.getColor(requireContext(), cat.colorSoftRes));
            cardRoot.setBackground(bg);

            int catColor = ContextCompat.getColor(requireContext(), cat.colorRes);
            TextView icon = card.findViewById(R.id.cardIcon);
            TextView title = card.findViewById(R.id.cardTitle);
            icon.setText(cat.icon);
            icon.setTextColor(catColor);
            title.setText(cat.title);
            title.setTextColor(catColor);
            card.findViewById(R.id.progressFill).setBackgroundColor(catColor);

            card.setTag(cat.id);
            card.setOnClickListener(v -> activity.pushFragment(PathFragment.newInstance(cat.id), "path"));

            container.addView(card);
        }

        // ترتيب البطاقات في صفوف من عنصرين
        arrangeInRows(container);
    }

    private void arrangeInRows(LinearLayout flatContainer) {
        // نجمع البطاقات المُضافة مسبقًا ونعيد توزيعها في صفوف أفقية من عنصرين
        int count = flatContainer.getChildCount();
        View[] cards = new View[count];
        for (int i = 0; i < count; i++) cards[i] = flatContainer.getChildAt(i);
        flatContainer.removeAllViews();

        for (int i = 0; i < count; i += 2) {
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            row.addView(cards[i]);
            if (i + 1 < count) {
                row.addView(cards[i + 1]);
            } else {
                View spacer = new View(requireContext());
                spacer.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(spacer);
            }
            flatContainer.addView(row);
        }
    }

    private void refreshUI(View view) {
        MainActivity activity = (MainActivity) requireActivity();
        ProgressManager pm = activity.getProgressManager();

        ((TextView) view.findViewById(R.id.tvStreak)).setText("🔥 " + pm.getStreak());
        ((TextView) view.findViewById(R.id.tvGems)).setText("💎 " + pm.getGems());
        ((TextView) view.findViewById(R.id.tvStars))
                .setText("⭐ " + pm.overallStars() + "/" + pm.overallMaxStars());

        ((TextView) view.findViewById(R.id.tvGreeting))
                .setText(getString(R.string.greeting_prefix, pm.getName()));

        LinearLayout categoriesContainer = view.findViewById(R.id.categoriesContainer);
        for (int r = 0; r < categoriesContainer.getChildCount(); r++) {
            View row = categoriesContainer.getChildAt(r);
            if (!(row instanceof LinearLayout)) continue;
            LinearLayout rowLayout = (LinearLayout) row;
            for (int c = 0; c < rowLayout.getChildCount(); c++) {
                View card = rowLayout.getChildAt(c);
                Object tag = card.getTag();
                if (!(tag instanceof String)) continue;
                String categoryId = (String) tag;
                int total = pm.totalStars(categoryId);
                int max = pm.maxStars(categoryId);
                int pct = max == 0 ? 0 : Math.round((total / (float) max) * 100);

                LinearLayout track = card.findViewById(R.id.progressTrack);
                View fill = card.findViewById(R.id.progressFill);
                View remainder = card.findViewById(R.id.progressRemainder);
                setWeight(fill, Math.max(pct, 1));
                setWeight(remainder, Math.max(100 - pct, 0));
                track.requestLayout();

                ((TextView) card.findViewById(R.id.cardCount))
                        .setText(getString(R.string.stars_of, total, max));
            }
        }

        View dailyCard = view.findViewById(R.id.dailyCard);
        TextView dailyBadge = view.findViewById(R.id.dailyBadge);
        TextView dailyTitle = view.findViewById(R.id.dailyTitle);
        TextView dailySubtitle = view.findViewById(R.id.dailySubtitle);
        boolean done = pm.isDailyDone();
        dailyBadge.setText(done ? "✅" : "🎯");
        dailyTitle.setText(done ? R.string.daily_done_title : R.string.daily_prompt_title);
        dailySubtitle.setText(done ? R.string.daily_done_subtitle : R.string.daily_prompt_subtitle);
        dailyCard.setAlpha(done ? 0.6f : 1f);
        dailyCard.setOnClickListener(v -> {
            if (pm.isDailyDone()) return;
            DailyDialogFragment dialog = new DailyDialogFragment();
            dialog.setOnDismissedCallback(() -> refreshUI(rootView));
            dialog.show(getChildFragmentManager(), "daily");
        });
    }

    private void setWeight(View view, int weight) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.weight = weight;
        view.setLayoutParams(lp);
    }

    private float dp(int value) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return value * density;
    }
}
