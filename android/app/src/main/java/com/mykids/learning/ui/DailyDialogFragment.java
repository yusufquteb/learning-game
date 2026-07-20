package com.mykids.learning.ui;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.mykids.learning.MainActivity;
import com.mykids.learning.R;
import com.mykids.learning.data.AnimalItem;
import com.mykids.learning.data.Category;
import com.mykids.learning.data.WordItem;
import com.mykids.learning.model.Option;
import com.mykids.learning.model.Question;
import com.mykids.learning.progress.ProgressManager;
import com.mykids.learning.util.AssetImageLoader;
import com.mykids.learning.util.QuizGenerator;
import com.mykids.learning.util.SoundUtil;

public class DailyDialogFragment extends DialogFragment {

    private boolean answered = false;
    private Runnable onDismissedCallback;

    public void setOnDismissedCallback(Runnable callback) {
        this.onDismissedCallback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x8D281E3C));
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_daily, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        QuizGenerator.DailyPick pick = QuizGenerator.dailyItem();
        Category cat = pick.category;
        Question q = QuizGenerator.buildQuestion(cat, pick.item);

        ((TextView) view.findViewById(R.id.dialogInstruction)).setText(q.instructionText);

        ImageView subjectShape = view.findViewById(R.id.dialogSubjectShape);
        TextView subjectEmoji = view.findViewById(R.id.dialogSubjectEmoji);

        if (q.subjectItem instanceof WordItem) {
            WordItem w = (WordItem) q.subjectItem;
            subjectShape.setImageBitmap(AssetImageLoader.load(requireContext(), w.imageAssetPath));
            subjectShape.setVisibility(View.VISIBLE);
        } else if (q.subjectItem instanceof AnimalItem) {
            subjectEmoji.setText(((AnimalItem) q.subjectItem).emoji);
            subjectEmoji.setVisibility(View.VISIBLE);
        }

        LinearLayout optionsContainer = view.findViewById(R.id.dialogOptionsContainer);
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
            optView.setOnClickListener(v -> onAnswer(optionsContainer, q, idx));
            optionsContainer.addView(optView);
        }

        view.findViewById(R.id.dialogClose).setOnClickListener(v -> dismissAllowingStateLoss());
        view.findViewById(R.id.dialogOkButton).setOnClickListener(v -> dismissAllowingStateLoss());
    }

    private void onAnswer(LinearLayout optionsContainer, Question q, int pickedIndex) {
        if (answered) return;
        answered = true;

        boolean correct = q.options.get(pickedIndex).correct;
        MainActivity activity = (MainActivity) requireActivity();
        SoundUtil soundUtil = activity.getSoundUtil();
        ProgressManager pm = activity.getProgressManager();

        if (correct) {
            soundUtil.playCorrect();
            pm.addGems(5);
        } else {
            soundUtil.playWrong();
        }
        pm.markDailyDone();

        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            View optView = optionsContainer.getChildAt(i);
            optView.setClickable(false);
            if (q.options.get(i).correct) {
                optView.setBackgroundResource(R.drawable.bg_quiz_option_correct);
            } else if (i == pickedIndex) {
                optView.setBackgroundResource(R.drawable.bg_quiz_option_wrong);
            }
        }

        MaterialButton okButton = requireView().findViewById(R.id.dialogOkButton);
        okButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss(@NonNull android.content.DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissedCallback != null) onDismissedCallback.run();
    }
}
