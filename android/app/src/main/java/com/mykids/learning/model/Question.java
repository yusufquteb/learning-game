package com.mykids.learning.model;

import java.util.List;

public class Question {
    public final String categoryId;
    public final String instructionText;
    /** عنصر السؤال الأصلي (Letter / ShapeItem / AnimalItem) لعرضه في الأعلى. */
    public final Object subjectItem;
    public final List<Option> options;

    public Question(String categoryId, String instructionText, Object subjectItem, List<Option> options) {
        this.categoryId = categoryId;
        this.instructionText = instructionText;
        this.subjectItem = subjectItem;
        this.options = options;
    }
}
