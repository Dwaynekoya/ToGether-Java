package com.example.together.view;

public enum View {
    LOGIN("login-view.fxml"),
    TASKLIST("tasklist-view.fxml"),
    PROFILE("profile-view.fxml"),
    NEWTASK("new-task-view.fxml"),
    NEWGROUP("new-group-view.fxml"),
    GROUPS("groups-view.fxml"),
    FEED("feed-view.fxml"),
    USER_POPUP("user-popup.fxml"),
    GROUP_POPUP("group-popup.fxml"),
    SETTINGS("settings-view.fxml");
    private String fileName;

    View(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
