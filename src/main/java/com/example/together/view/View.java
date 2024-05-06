package com.example.together.view;

public enum View {
    LOGIN("login-view.fxml"),
    TASKLIST("tasklist-view.fxml"),
    PROFILE("profile-view.fxml"),
    NEWTASK("new-task-view.fxml");
    private String fileName;

    View(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
