package com.tidal.refactoring.services;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.tidal.refactoring.entities.dao.NewTrack;
import com.tidal.refactoring.entities.nodes.Track;

public interface TrackService {
  List<Track> findAll();

  Track findByTitle(String title);

  List<Track> findByDurationLongerThan(Integer duration);

  Track findById(String id);

  HttpStatus deleteAll();

  HttpStatus deleteById(String id);

  HttpStatus create(NewTrack newTrack);
}
