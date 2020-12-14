package com.tidal.refactoring.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.tidal.refactoring.entities.nodes.PlayList;

@RepositoryRestResource(path = "playlists")
public interface PlayListRepository extends Repository<PlayList, String> {
  List<PlayList> findAll();

  PlayList findByPlayListName(String playListName);

  List<PlayList> findPlayListsByDeletedFalse();

  List<PlayList> findPlayListsByDeletedTrue();

  List<PlayList> findPlayListsByRegisteredDateIsGreaterThan(Date date);

  List<PlayList> findByOrderByLastUpdatedDesc();

  PlayList findById(String id);

  void deleteAll();

  void deleteById(String id);

  void save(PlayList playList);

  @Query(
      "MATCH (p:Playlist {id: $playListId}) - [r:HAS_TRACK] -> (n:Track {id: $trackId}) DETACH DELETE r")
  void removeRelationship(String trackId, String playListId);
}
