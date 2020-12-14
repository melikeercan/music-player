package com.tidal.refactoring.entities.dao;

public class NewArtist {
  private String name;

  private NewArtist() {
  }

  public NewArtist(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
