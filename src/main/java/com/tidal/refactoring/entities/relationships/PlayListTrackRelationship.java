package com.tidal.refactoring.entities.relationships;

import java.util.Date;

import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import org.springframework.data.neo4j.core.support.DateLong;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tidal.refactoring.entities.nodes.PlayList;
import com.tidal.refactoring.entities.nodes.Track;

@RelationshipProperties
public class PlayListTrackRelationship {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @TargetNode
  private Track track;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private PlayList playList;

  @DateLong
  private Date dateAdded;

  private Integer index;

  private PlayListTrackRelationship() {
  }

  public PlayListTrackRelationship(Track track, PlayList playList, Integer index) {
    this.track = track;
    this.playList = playList;
    this.dateAdded = new Date();
    this.index = index;
  }

  public Track getTrack() {
    return track;
  }

  public void setTrack(Track track) {
    this.track = track;
  }

  public PlayList getPlayList() {
    return playList;
  }

  public void setPlayList(PlayList playList) {
    this.playList = playList;
  }

  public Date getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(Date dateAdded) {
    this.dateAdded = dateAdded;
  }

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }
}
