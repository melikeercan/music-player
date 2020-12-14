package com.tidal.refactoring.entities.dao;

public class RemoveFromPlayList {
  private String playListId;
  private String trackId;

  private RemoveFromPlayList() {
  }

  public RemoveFromPlayList(String playListId, String trackId) {
    this.playListId = playListId;
    this.trackId = trackId;
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
}
