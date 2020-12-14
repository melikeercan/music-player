package com.tidal.refactoring.api;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tidal.refactoring.entities.dao.EditPlayList;
import com.tidal.refactoring.entities.dao.NewPlayList;
import com.tidal.refactoring.entities.dao.NewTrackToPlayList;
import com.tidal.refactoring.entities.dao.RemoveFromPlayList;
import com.tidal.refactoring.exceptions.DatabaseException;
import com.tidal.refactoring.exceptions.EntityAlreadyExistsException;
import com.tidal.refactoring.exceptions.EntityNotFoundException;
import com.tidal.refactoring.exceptions.EntityUpdateNotRequiredException;
import com.tidal.refactoring.exceptions.InvalidParameterException;
import com.tidal.refactoring.responses.RestCallResponse;
import com.tidal.refactoring.services.PlayListServiceImp;

import static com.tidal.refactoring.utils.Constants.ADDED_TRACK_TO_PLAYLIST_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.CREATED_NEW_PLAYLIST_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.DELETED_ALL_PLAYLISTS_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.PLAYLIST_WITH_ID_DELETED_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.REMOVED_TRACK_FROM_PLAYLIST_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.UPDATED_PLAYLIST_NAME_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.UPDATED_TRACK_INDEX_SUCCESSFULLY;

@RestController
@RequestMapping("/api/v0/playlists")
public class PlayListController {
  private final PlayListServiceImp playListService;

  public PlayListController(PlayListServiceImp playListService) {
    this.playListService = playListService;
  }

  @GetMapping("/")
  RestCallResponse getAllPlayLists() throws DatabaseException {
    return new RestCallResponse(HttpStatus.OK, playListService.findAll());
  }

  @GetMapping("/isActive={isActive}")
  RestCallResponse getAllActivePlayLists(@PathVariable("isActive") Boolean isActive)
      throws DatabaseException {
    return new RestCallResponse(HttpStatus.OK, playListService.findByActive(isActive));
  }

  @GetMapping("/name={name}")
  RestCallResponse getByPlayListName(@PathVariable("name") String name)
      throws InvalidParameterException,
      EntityNotFoundException, DatabaseException {
    return new RestCallResponse(HttpStatus.OK, playListService.findByName(name));
  }

  @GetMapping("/createdAfter={createdAfter}")
  RestCallResponse getPlayListsCreatedAfter(
      @PathVariable("createdAfter") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          Long createdAfter)
      throws DatabaseException, InvalidParameterException {
    return new RestCallResponse(HttpStatus.OK, playListService.findAllCreatedAfter(createdAfter));
  }

  @GetMapping("/sortByLastUpdated")
    // experimental. does not comply with rest api naming best practice
  RestCallResponse sortPlayListsByLastUpdateDate() throws DatabaseException {
    return new RestCallResponse(HttpStatus.OK, playListService.sortByLastUpdateDate());
  }

  @GetMapping("/id={id}")
  RestCallResponse getById(@PathVariable String id)
      throws InvalidParameterException, DatabaseException {
    return new RestCallResponse(HttpStatus.OK, playListService.findById(id));
  }

  @DeleteMapping("/")
  RestCallResponse deleteAllPlayLists() throws DatabaseException {
    playListService.deleteAll();
    return new RestCallResponse(HttpStatus.OK, DELETED_ALL_PLAYLISTS_SUCCESSFULLY);
  }

  @DeleteMapping("/{id}")
  RestCallResponse deleteById(@PathVariable String id)
      throws InvalidParameterException, EntityNotFoundException, DatabaseException {
    playListService.deleteById(id);
    return new RestCallResponse(HttpStatus.OK,
        String.format(PLAYLIST_WITH_ID_DELETED_SUCCESSFULLY, id));
  }

  @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  RestCallResponse addNewPlayList(@RequestBody NewPlayList newPlayList)
      throws InvalidParameterException, EntityAlreadyExistsException, DatabaseException {
    playListService.create(newPlayList);
    return new RestCallResponse(HttpStatus.CREATED, CREATED_NEW_PLAYLIST_SUCCESSFULLY, newPlayList);
  }

  @PostMapping(path = "/track/add", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  RestCallResponse addToPlayList(@RequestBody NewTrackToPlayList newTrackToPlayList)
      throws InvalidParameterException, EntityNotFoundException,
      DatabaseException {
    playListService.add(newTrackToPlayList);
    return new RestCallResponse(HttpStatus.OK, ADDED_TRACK_TO_PLAYLIST_SUCCESSFULLY,
        newTrackToPlayList);
  }

  @PostMapping(path = "/track/remove", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  RestCallResponse removeFromPlayList(@RequestBody RemoveFromPlayList removeFromPlayList)
      throws InvalidParameterException, EntityNotFoundException, DatabaseException {
    playListService.remove(removeFromPlayList);
    return new RestCallResponse(HttpStatus.OK, REMOVED_TRACK_FROM_PLAYLIST_SUCCESSFULLY,
        removeFromPlayList);
  }

  @PostMapping(path = "/track/index", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  RestCallResponse updatePlayListIndex(@RequestBody NewTrackToPlayList trackIndexInfoToUpdate)
      throws InvalidParameterException, EntityNotFoundException, EntityUpdateNotRequiredException {
    playListService.updateTrackIndex(trackIndexInfoToUpdate);
    return new RestCallResponse(HttpStatus.OK, UPDATED_TRACK_INDEX_SUCCESSFULLY,
        trackIndexInfoToUpdate);
  }

  @PostMapping(path = "/update/name", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  RestCallResponse updatePlayListName(@RequestBody EditPlayList editPlayList)
      throws InvalidParameterException, EntityNotFoundException, EntityAlreadyExistsException {
    playListService.updateName(editPlayList);
    return new RestCallResponse(HttpStatus.OK, UPDATED_PLAYLIST_NAME_SUCCESSFULLY, editPlayList);
  }

}
