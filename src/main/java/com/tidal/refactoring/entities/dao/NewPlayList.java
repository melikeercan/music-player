package com.tidal.refactoring.entities.dao;

public class NewPlayList {
  private String playListName;

  private NewPlayList() {
  }

  public NewPlayList(String playListName) {
    this.playListName = playListName;
  }

  public String getPlayListName() {
    return playListName;
  }

  public void setPlayListName(String playListName) {
    this.playListName = playListName;
  }
}
