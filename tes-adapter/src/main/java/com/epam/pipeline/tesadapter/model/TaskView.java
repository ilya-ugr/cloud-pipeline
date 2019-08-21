package com.epam.pipeline.tesadapter.model;

public enum TaskView {
    MINIMAL("MINIMAL"),
    BASIC("BASIC"),
    FULL("FULL");

    private String value;

    TaskView(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
