package com.mykids.learning.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mykids.learning.MainActivity;
import com.mykids.learning.R;
import com.mykids.learning.progress.ProgressManager;

public class WelcomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText nameInput = view.findViewById(R.id.nameInput);

        view.findViewById(R.id.startButton).setOnClickListener(v -> {
            MainActivity activity = (MainActivity) requireActivity();
            String typed = nameInput.getText() == null ? "" : nameInput.getText().toString().trim();
            String finalName = typed.isEmpty() ? "بطل صغير" : typed;

            ProgressManager pm = activity.getProgressManager();
            pm.setName(finalName);
            pm.touchStreak();

            activity.resetToFragment(new HomeFragment());
        });
    }
}
