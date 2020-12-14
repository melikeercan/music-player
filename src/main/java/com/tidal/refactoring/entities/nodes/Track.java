package com.tidal.refactoring.entities.nodes;

import java.util.UUID;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tidal.refactoring.entities.relationships.ArtistTrackRelationship;

@Node("Track")
public class Track {
  @Id
  private String id;

  private String title;

  private Integer durationInSeconds;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Relationship(type = "BELONGS_TO_ARTIST")
  private ArtistTrackRelationship artistTrackRelationship;

  private Track() {
  }

  public Track(String id, String title, Integer durationInSeconds) {
    this.id = id;
    this.title = title;
    this.durationInSeconds = durationInSeconds;
  }

  public Track(String title, Integer durationInSeconds) {
    this.id = UUID.randomUUID().toString();
    this.title = title;
    this.durationInSeconds = durationInSeconds;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public ArtistTrackRelationship getArtistTrackRelationship() {
    return artistTrackRelationship;
  }

  public void setArtistTrackRelationship(
      ArtistTrackRelationship artistTrackRelationship) {
    this.artistTrackRelationship = artistTrackRelationship;
  }
}
