package com.tidal.refactoring.entities.nodes;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.DateLong;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tidal.refactoring.entities.relationships.PlayListTrackRelationship;

@Node("Playlist")
public class PlayList {
  @Id
  private String id;
  private String playListName;
  @DateLong
  private Date registeredDate;
  @DateLong
  private Date lastUpdated;
  private Boolean deleted;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Relationship(type = "HAS_TRACK")
  private Set<PlayListTrackRelationship> tracks;
  private Integer trackSize;

  private PlayList() { }

  public PlayList(String playListName) {
    this.id = UUID.randomUUID().toString();
    this.playListName = playListName;
    this.registeredDate = new Date();
    this.lastUpdated = new Date();
    this.deleted = false;
    this.tracks = new HashSet<>();
    this.trackSize = 0;
  }

  public PlayList(String id, String playListName, Date registeredDate, Date lastUpdated,
      Boolean deleted, Set<PlayListTrackRelationship> tracks, Integer trackSize) {
    this.id = id;
    this.playListName = playListName;
    this.registeredDate = registeredDate;
    this.lastUpdated = lastUpdated;
    this.deleted = deleted;
    this.tracks = tracks;
    this.trackSize = trackSize;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPlayListName() {
    return playListName;
  }

  public void setPlayListName(String playListName) {
    this.playListName = playListName;
  }

  public Date getRegisteredDate() {
    return registeredDate;
  }

  public void setRegisteredDate(Date registeredDate) {
    this.registeredDate = registeredDate;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public Set<PlayListTrackRelationship> getTracks() {
    return tracks;
  }

  public void setTracks(Set<PlayListTrackRelationship> tracks) {
    this.tracks = tracks;
  }

  public Integer getTrackSize() {
    return trackSize;
  }

  public void setTrackSize(Integer trackSize) {
    this.trackSize = trackSize;
  }
}
