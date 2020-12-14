package com.tidal.refactoring.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tidal.refactoring.entities.dao.NewTrack;
import com.tidal.refactoring.exceptions.DatabaseException;
import com.tidal.refactoring.exceptions.EntityNotFoundException;
import com.tidal.refactoring.exceptions.InvalidParameterException;
import com.tidal.refactoring.responses.RestCallResponse;
import com.tidal.refactoring.services.TrackServiceImp;

import static com.tidal.refactoring.utils.Constants.CREATED_NEW_TRACK_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.DELETED_ALL_TRACKS_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.TRACK_WITH_ID_DELETED_SUCCESSFULLY;

@RestController
@RequestMapping("/api/v0/tracks")
public class TrackController {

  private final TrackServiceImp trackService;

  public TrackController(TrackServiceImp trackService) {
    this.trackService = trackService;
  }

  @GetMapping("/")
  RestCallResponse getAllTracks() throws DatabaseException {
    return new RestCallResponse(HttpStatus.OK, trackService.findAll());
  }

  @GetMapping("/title={title}")
  RestCallResponse getByTitle(@PathVariable("title") String title) throws InvalidParameterException,
      EntityNotFoundException, DatabaseException {
    return new RestCallResponse(HttpStatus.OK, trackService.findByTitle(title));
  }

  @GetMapping("/longerThan={duration}")
  RestCallResponse getByDurationLongerThan(@PathVariable("duration") Integer duration)
      throws InvalidParameterException, DatabaseException {
    return new RestCallResponse(HttpStatus.OK, trackService.findByDurationLongerThan(duration));
  }

  @GetMapping("/id={id}")
  RestCallResponse getById(@PathVariable("id") String id)
      throws InvalidParameterException, DatabaseException {
    return new RestCallResponse(HttpStatus.OK, trackService.findById(id));
  }

  @DeleteMapping("/")
  RestCallResponse deleteAllTracks() throws DatabaseException {
    trackService.deleteAll();
    return new RestCallResponse(HttpStatus.OK, DELETED_ALL_TRACKS_SUCCESSFULLY);
  }

  @DeleteMapping("/{id}")
  RestCallResponse deleteById(@PathVariable String id)
      throws InvalidParameterException, EntityNotFoundException, DatabaseException {
    trackService.deleteById(id);
    return new RestCallResponse(HttpStatus.OK,
        String.format(TRACK_WITH_ID_DELETED_SUCCESSFULLY, id));
  }

  @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  RestCallResponse addNewTrack(@RequestBody NewTrack newTrack) throws InvalidParameterException,
      EntityNotFoundException, DatabaseException {
    trackService.create(newTrack);
    return new RestCallResponse(HttpStatus.OK, CREATED_NEW_TRACK_SUCCESSFULLY, newTrack);
  }
}
