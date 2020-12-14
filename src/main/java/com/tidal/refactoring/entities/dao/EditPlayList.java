package com.tidal.refactoring.entities.dao;

public class EditPlayList {
  private String newName;
  private String playListId;

  private EditPlayList() {
  }

  public EditPlayList(String newName, String playListId) {
    this.newName = newName;
    this.playListId = playListId;
  }

  public String getNewName() {
    return newName;
  }

  public void setNewName(String newName) {
    this.newName = newName;
  }

  public String getPlayListId() {
    return playListId;
  }

  public void setPlayListId(String playListId) {
    this.playListId = playListId;
  }
}
