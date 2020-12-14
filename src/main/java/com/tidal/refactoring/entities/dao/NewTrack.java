package com.tidal.refactoring.entities.dao;

public class NewTrack {
  private String title;
  private Integer durationInSeconds;
  private String artistId;
  private String albumName;
  private Integer releaseYear;

  private NewTrack() {
  }

  public NewTrack(String title, Integer durationInSeconds, String artistId,
      String albumName, Integer releaseYear) {
    this.title = title;
    this.durationInSeconds = durationInSeconds;
    this.artistId = artistId;
    this.albumName = albumName;
    this.releaseYear = releaseYear;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getDurationInSeconds() {
    return durationInSeconds;
  }

  public void setDurationInSeconds(Integer durationInSeconds) {
    this.durationInSeconds = durationInSeconds;
  }

  public String getArtistId() {
    return artistId;
  }

  public void setArtistId(String artistId) {
    this.artistId = artistId;
  }

  public String getAlbumName() {
    return albumName;
  }

  public void setAlbumName(String albumName) {
    this.albumName = albumName;
  }

  public Integer getReleaseYear() {
    return releaseYear;
  }

  public void setReleaseYear(Integer releaseYear) {
    this.releaseYear = releaseYear;
  }
}
