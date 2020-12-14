package com.tidal.refactoring.entities.dao;

public class NewTrackToPlayList {
  private String playListId;
  private String trackId;
  private Integer index;

  private NewTrackToPlayList() {
  }

  public NewTrackToPlayList(String playListId, String trackId, Integer index) {
    this.playListId = playListId;
    this.trackId = trackId;
    this.index = index;
  }

  public String getPlayListId() {
    return playListId;
  }

  public void setPlayListId(String playListId) {
    this.playListId = playListId;
  }

  public String getTrackId() {
    return trackId;
  }

  public void setTrackId(String trackId) {
    this.trackId = trackId;
  }

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }
}
