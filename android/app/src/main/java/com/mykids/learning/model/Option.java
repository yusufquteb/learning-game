package com.mykids.learning.model;

public class Option {
    public final String label;
    public final String emoji; // قد تكون فارغة
    public final boolean correct;

    public Option(String label, String emoji, boolean correct) {
        this.label = label;
        this.emoji = emoji;
        this.correct = correct;
    }
}
