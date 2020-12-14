package com.tidal.refactoring.entities.nodes;

import java.util.UUID;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Artist")
public class Artist {
  @Id
  private String id;

  private String artistName;

  private Artist() {
  }

  public Artist(String id, String artistName) {
    this.id = id;
    this.artistName = artistName;
  }

  public Artist(String artistName) {
    this.id = UUID.randomUUID().toString();
    this.artistName = artistName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getArtistName() {
    return artistName;
  }

  public void setArtistName(String artistName) {
    this.artistName = artistName;
  }
}
