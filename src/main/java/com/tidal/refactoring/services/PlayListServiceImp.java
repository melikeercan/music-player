package com.tidal.refactoring.services;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tidal.refactoring.entities.dao.EditPlayList;
import com.tidal.refactoring.entities.dao.NewPlayList;
import com.tidal.refactoring.entities.dao.NewTrackToPlayList;
import com.tidal.refactoring.entities.dao.RemoveFromPlayList;
import com.tidal.refactoring.entities.nodes.PlayList;
import com.tidal.refactoring.entities.nodes.Track;
import com.tidal.refactoring.entities.relationships.PlayListTrackRelationship;
import com.tidal.refactoring.exceptions.DatabaseException;
import com.tidal.refactoring.exceptions.EntityAlreadyExistsException;
import com.tidal.refactoring.exceptions.EntityNotFoundException;
import com.tidal.refactoring.exceptions.EntityUpdateNotRequiredException;
import com.tidal.refactoring.exceptions.InvalidParameterException;
import com.tidal.refactoring.repositories.PlayListRepository;

import static com.tidal.refactoring.utils.Constants.DATE_IS_INVALID;
import static com.tidal.refactoring.utils.Constants.NEW_PLAYLIST_NAME_IS_NULL_OR_EMPTY;
import static com.tidal.refactoring.utils.Constants.PLAYLIST_NAME_IS_NULL_OR_EMPTY;
import static com.tidal.refactoring.utils.Constants.TRACK_INDEX_IS_INVALID;
import static com.tidal.refactoring.utils.StringChecker.isNullOrEmpty;
import static com.tidal.refactoring.utils.TimeUtils.isDateValid;

@Service
public class PlayListServiceImp extends BaseServiceImp implements PlayListService {
  private final PlayListRepository playListRepository;
  private final TrackServiceImp trackService;

  public PlayListServiceImp(PlayListRepository playListRepository,
      TrackServiceImp trackService) {
    this.playListRepository = playListRepository;
    this.trackService = trackService;
  }

  public List<PlayList> findAll() {
    try {
      return playListRepository.findAll();
    } catch (Exception ex) {
      throw new DatabaseException(PlayList.class, "findAll");
    }
  }

  public List<PlayList> findByActive(Boolean isActive) {
    try {
      if (isActive) {
        return playListRepository.findPlayListsByDeletedFalse();
      }
      return playListRepository.findPlayListsByDeletedTrue();
    } catch (Exception ex) {
      throw new DatabaseException(PlayList.class, "findAllActive");
    }
  }

  public PlayList findByName(String name) {
    if (name == null || name.isEmpty()) {
      throw new InvalidParameterException(PLAYLIST_NAME_IS_NULL_OR_EMPTY);
    }
    PlayList found;
    try {
      found = playListRepository.findByPlayListName(name);
    } catch (Exception ex) {
      throw new DatabaseException(PlayList.class, "findByName", name);
    }
    if (found == null) {
      throw new EntityNotFoundException(PlayList.class, "name", name);
    }
    return found;
  }

  public List<PlayList> findAllCreatedAfter(Long date) {
    if (!isDateValid(date)) {
      throw new InvalidParameterException(DATE_IS_INVALID,
          date == null ? null : Long.toString(date));
    }
    try {
      return playListRepository.findPlayListsByRegisteredDateIsGreaterThan(new Date(date));
    } catch (Exception ex) {
      throw new DatabaseException(PlayList.class, "findAllCreatedAfter",
          date == null ? null : String.valueOf(date));
    }
  }

  public List<PlayList> sortByLastUpdateDate() {
    try {
      return playListRepository.findByOrderByLastUpdatedDesc();
    } catch (Exception ex) {
      throw new DatabaseException(PlayList.class, "sortByLastUpdateDate");
    }
  }

  public PlayList findById(String id) {
    if (id == null || id.isEmpty()) {
      throw new InvalidParameterException(PLAYLIST_NAME_IS_NULL_OR_EMPTY);
    }
    try {
      return playListRepository.findById(id);
    } catch (Exception ex) {
      throw new DatabaseException(PlayList.class, "findById", id);
    }
  }

  public HttpStatus deleteAll() {
    try {
      playListRepository.deleteAll();
    } catch (Exception e) {
      throw new DatabaseException(PlayList.class, "deleteAll");
    }
    return HttpStatus.OK;
  }

  public HttpStatus deleteById(String id) {
    PlayList found = findById(id);
    if (found == null) {
      throw new EntityNotFoundException(PlayList.class, "id", id);
    }
    try {
      playListRepository.deleteById(id);
      return HttpStatus.OK;
    } catch (Exception e) {
      throw new DatabaseException(PlayList.class, "deleteById", id);
    }
  }

  public HttpStatus create(NewPlayList newPlayList) {
    if (isNullOrEmpty(newPlayList.getPlayListName())) {
      throw new InvalidParameterException(PLAYLIST_NAME_IS_NULL_OR_EMPTY);
    }
    String name = newPlayList.getPlayListName();
    PlayList found;
    try {
      found = playListRepository.findByPlayListName(name);
    } catch (Exception e) {
      throw new DatabaseException(PlayList.class, "create", newPlayList.getPlayListName());
    }
    if (found != null) {
      throw new EntityAlreadyExistsException(PlayList.class, "name", name);
    }
    PlayList playList = new PlayList(name);
    try {
      playListRepository.save(playList);
    } catch (Exception e) {
      throw new DatabaseException(PlayList.class, "create", newPlayList.getPlayListName());
    }
    return HttpStatus.CREATED;
  }

  public HttpStatus remove(RemoveFromPlayList removeFromPlayList) {
    String trackId = removeFromPlayList.getTrackId();
    String playListId = removeFromPlayList.getPlayListId();
    Track track = trackService.findById(trackId);
    PlayList playList = findById(playListId);
    if (track == null) {
      throw new EntityNotFoundException(Track.class, "id", trackId);
    }
    if (playList == null) {
      throw new EntityNotFoundException(PlayList.class, "id", playListId);
    }
    Set<PlayListTrackRelationship> tracks = playList.getTracks();
    // if the track is not in the playlist, do nothing
    if (isTrackInPlayList(playList.getTracks(), trackId)) {
      int indexToRemove = getCurrentIndex(tracks, trackId);
      Iterator<PlayListTrackRelationship> iterator = tracks.iterator();
      while (iterator.hasNext()) {
        PlayListTrackRelationship playListTrackRelationship = iterator.next();
        if (playListTrackRelationship.getTrack().getId().equals(trackId)) {
          iterator.remove();
        } else {
          Integer currentIndex = playListTrackRelationship.getIndex();
          if (currentIndex > indexToRemove) {
            playListTrackRelationship.setIndex(--currentIndex);
          }
        }
      }
      playList.setLastUpdated(new Date());
      playList.setTrackSize(playList.getTrackSize() - 1);
      try {
        playListRepository.save(playList);
        playListRepository.removeRelationship(trackId, playListId);
      } catch (Exception e) {
        throw new DatabaseException(PlayList.class, "remove", playListId, trackId);
      }
    }
    return HttpStatus.OK;
  }

  public HttpStatus add(NewTrackToPlayList newTrackToPlayList) {
    String trackId = newTrackToPlayList.getTrackId();
    String playListId = newTrackToPlayList.getPlayListId();
    Integer index = newTrackToPlayList.getIndex();
    Track track = trackService.findById(trackId);
    PlayList playList = findById(playListId);

    if (index == null || index < 0 || index > playList.getTrackSize()) {
      throw new InvalidParameterException(TRACK_INDEX_IS_INVALID, String.valueOf(index));
    }
    if (track == null) {
      throw new EntityNotFoundException(Track.class, "id", trackId);
    }
    if (playList == null) {
      throw new EntityNotFoundException(PlayList.class, "id", playListId);
    }
    // if the track is already in the playlist, do nothing
    if (!isTrackInPlayList(playList.getTracks(), trackId)) {
      PlayListTrackRelationship playListTrackRelationship = new PlayListTrackRelationship(track,
          playList, index);
      shiftIndexes(playList.getTracks(), index, playListTrackRelationship);
      playList.setTrackSize(playList.getTrackSize() + 1);
      playList.setLastUpdated(new Date());
      try {
        playListRepository.save(playList);
      } catch (Exception e) {
        throw new DatabaseException(PlayList.class, "add", playListId, trackId,
            String.valueOf(index));
      }
    }
    return HttpStatus.OK;
  }

  private void shiftIndexes(Set<PlayListTrackRelationship> tracks, Integer index,
      PlayListTrackRelationship playListTrackRelationship) {
    // shift the tracks with higher index (+1)
    tracks.stream().forEach(track -> {
      int trackIndex = track.getIndex();
      if (trackIndex >= index) {
        track.setIndex(++trackIndex);
      }
    });
    tracks.add(playListTrackRelationship);
  }

  private boolean isTrackInPlayList(Set<PlayListTrackRelationship> tracks, String trackId) {
    PlayListTrackRelationship result = tracks.stream()
        .filter(trackStream -> trackStream.getTrack().getId().equals(trackId)).findAny()
        .orElse(null);
    return result != null;
  }

  public HttpStatus updateTrackIndex(NewTrackToPlayList trackIndexInfoToUpdate) {
    String trackId = trackIndexInfoToUpdate.getTrackId();
    String playListId = trackIndexInfoToUpdate.getPlayListId();
    Integer index = trackIndexInfoToUpdate.getIndex();
    Track track = trackService.findById(trackId);
    PlayList playList = findById(playListId);
    if (index == null || index < 0 || index >= playList.getTrackSize()) {
      throw new InvalidParameterException(TRACK_INDEX_IS_INVALID, String.valueOf(index));
    }
    if (track == null) {
      throw new EntityNotFoundException(Track.class, "id", trackId);
    }
    if (playList == null) {
      throw new EntityNotFoundException(PlayList.class, "id", playListId);
    }

    if (isTrackInPlayList(playList.getTracks(), trackId)) {
      updateIndexes(playList.getTracks(), trackId, index);
      playList.setLastUpdated(new Date());
      try {
        playListRepository.save(playList);
      } catch (Exception e) {
        throw new DatabaseException(PlayList.class, playListId, trackId, String.valueOf(index));
      }
    }
    return HttpStatus.OK;
  }

  private void updateIndexes(Set<PlayListTrackRelationship> tracks, String trackId, Integer index) {
    if (index > 0) {
      Integer oldIndex = getCurrentIndex(tracks, trackId);
      if (index == oldIndex) {
        throw new EntityUpdateNotRequiredException(PlayList.class, "index", String.valueOf(index));
      }

      for (PlayListTrackRelationship relationship : tracks) {
        if (relationship.getTrack().getId().equals(trackId)) {
          relationship.setIndex(index);
        } else {
          int currentIndex = relationship.getIndex();
          if (currentIndex <= index && currentIndex >= oldIndex) {
            if (index > oldIndex) {
              relationship.setIndex(--currentIndex);
            } else {
              relationship.setIndex(++currentIndex);
            }
          }
        }
      }
    }
  }

  private int getCurrentIndex(Set<PlayListTrackRelationship> tracks, String trackId) {
    for (PlayListTrackRelationship relationship : tracks) {
      if (relationship.getTrack().getId().equals(trackId)) {
        return relationship.getIndex();
      }
    }
    return -1; // won't access there anyway
  }

  public HttpStatus updateName(EditPlayList editPlayList) {
    String newName = editPlayList.getNewName();
    if (newName == null || newName.isEmpty()) {
      throw new InvalidParameterException(NEW_PLAYLIST_NAME_IS_NULL_OR_EMPTY);
    }
    PlayList playListWithNewName = playListRepository.findByPlayListName(newName);
    if (playListWithNewName != null) {
      throw new EntityAlreadyExistsException(PlayList.class, "name", newName);
    }
    String playListId = editPlayList.getPlayListId();
    PlayList playList = findById(playListId);
    if (playList == null) {
      throw new EntityNotFoundException(PlayList.class, "id", playListId);
    }
    playList.setPlayListName(newName);
    playList.setLastUpdated(new Date());
    try {
      playListRepository.save(playList);
    } catch (Exception e) {
      throw new DatabaseException(PlayList.class, playListId, newName);
    }
    return HttpStatus.OK;
  }
}
