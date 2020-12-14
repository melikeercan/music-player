package com.tidal.refactoring.repositories;

import java.util.List;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.tidal.refactoring.entities.nodes.Track;

@RepositoryRestResource(path = "tracks")
public interface TrackRepository extends Repository<Track, String> {
  List<Track> findAll();

  Track findTrackByTitle(String title);

  List<Track> findTracksByDurationInSecondsGreaterThanEqual(Integer duration);

  Track findById(String id);

  void save(Track track);

  void deleteAll();

  void deleteById(String id);
}
