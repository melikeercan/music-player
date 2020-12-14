package com.tidal.refactoring.services;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.tidal.refactoring.entities.dao.EditPlayList;
import com.tidal.refactoring.entities.dao.NewPlayList;
import com.tidal.refactoring.entities.dao.NewTrackToPlayList;
import com.tidal.refactoring.entities.dao.RemoveFromPlayList;
import com.tidal.refactoring.entities.nodes.PlayList;

public interface PlayListService {
  List<PlayList> findAll();

  PlayList findByName(String name);

  List<PlayList> findAllCreatedAfter(Long date);

  List<PlayList> sortByLastUpdateDate();

  PlayList findById(String id);

  HttpStatus deleteAll();

  HttpStatus deleteById(String id);

  HttpStatus create(NewPlayList newPlayList);

  HttpStatus remove(RemoveFromPlayList removeFromPlayList);

  HttpStatus add(NewTrackToPlayList newTrackToPlayList);

  HttpStatus updateTrackIndex(NewTrackToPlayList trackIndexInfoToUpdate);

  HttpStatus updateName(EditPlayList editPlayList);
}
