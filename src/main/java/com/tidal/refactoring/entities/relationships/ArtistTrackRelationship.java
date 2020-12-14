package com.tidal.refactoring.entities.relationships;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tidal.refactoring.entities.nodes.Artist;
import com.tidal.refactoring.entities.nodes.Track;

@RelationshipProperties
public class ArtistTrackRelationship {
  @Id
  private String id;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Track track;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @TargetNode
  private Artist artist;
  private String albumName;
  private Integer releaseYear;

  private ArtistTrackRelationship() {
  }

  public ArtistTrackRelationship(Track track, Artist artist, String albumName,
      Integer releaseYear) {
    this.track = track;
    this.artist = artist;
    this.albumName = albumName;
    this.releaseYear = releaseYear;
  }

  public Track getTrack() {
    return track;
  }

  public void setTrack(Track track) {
    this.track = track;
  }

  public Artist getArtist() {
    return artist;
  }

  public void setArtist(Artist artist) {
    this.artist = artist;
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
