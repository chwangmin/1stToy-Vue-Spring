package com.first.board.domain.board.type;

public enum BoardType {
    OPEN, QUESTION;

    public boolean isOpen() {
        return this == OPEN;
    }

    public boolean isQuestion() {
        return this == QUESTION;
    }
}
