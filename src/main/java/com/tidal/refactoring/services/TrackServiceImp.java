package com.tidal.refactoring.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tidal.refactoring.entities.dao.NewTrack;
import com.tidal.refactoring.entities.nodes.Artist;
import com.tidal.refactoring.entities.nodes.Track;
import com.tidal.refactoring.entities.relationships.ArtistTrackRelationship;
import com.tidal.refactoring.exceptions.DatabaseException;
import com.tidal.refactoring.exceptions.EntityNotFoundException;
import com.tidal.refactoring.exceptions.InvalidParameterException;
import com.tidal.refactoring.repositories.TrackRepository;

import static com.tidal.refactoring.utils.Constants.ALBUM_NAME_IS_NULL_OR_EMPTY;
import static com.tidal.refactoring.utils.Constants.ARTIST_ID_IS_NULL_OR_EMPTY;
import static com.tidal.refactoring.utils.Constants.DURATION_IS_INVALID;
import static com.tidal.refactoring.utils.Constants.PLAYLIST_ID_IS_NULL_OR_EMPTY;
import static com.tidal.refactoring.utils.Constants.RELEASE_YEAR_IS_INVALID;
import static com.tidal.refactoring.utils.Constants.TRACK_TITLE_IS_NULL_OR_EMPTY;
import static com.tidal.refactoring.utils.StringChecker.isNullOrEmpty;
import static com.tidal.refactoring.utils.TimeUtils.isDurationValid;
import static com.tidal.refactoring.utils.TimeUtils.isReleaseDateValid;

@Service
public class TrackServiceImp extends BaseServiceImp implements TrackService {
  private final TrackRepository trackRepository;
  private final ArtistServiceImp artistService;

  public TrackServiceImp(TrackRepository trackRepository, ArtistServiceImp artistService) {
    this.trackRepository = trackRepository;
    this.artistService = artistService;
  }

  public List<Track> findAll() {
    try {
      return trackRepository.findAll();
    } catch (Exception ex) {
      throw new DatabaseException(Track.class, "findAll");
    }
  }

  public Track findByTitle(String title) {
    if (title == null || title.isEmpty()) {
      throw new InvalidParameterException(TRACK_TITLE_IS_NULL_OR_EMPTY);
    }
    try {
      Track found = trackRepository.findTrackByTitle(title);
      if (found == null) {
        throw new EntityNotFoundException(Track.class, "title", title);
      }
      return found;
    } catch (Exception ex) {
      throw new DatabaseException(Track.class, "findByTitle", title);
    }
  }

  public List<Track> findByDurationLongerThan(Integer duration) {
    if (!isDurationValid(duration)) {
      throw new InvalidParameterException(DURATION_IS_INVALID,
          duration == null ? null : Integer.toString(duration));
    }
    try {
      return trackRepository.findTracksByDurationInSecondsGreaterThanEqual(duration);
    } catch (Exception ex) {
      throw new DatabaseException(Track.class, "findByDurationLongerThan",
          duration == null ? null : String.valueOf(duration));
    }
  }

  public Track findById(String id) {
    if (id == null || id.isEmpty()) {
      throw new InvalidParameterException(PLAYLIST_ID_IS_NULL_OR_EMPTY);
    }
    try {
      return trackRepository.findById(id);
    } catch (Exception ex) {
      throw new DatabaseException(Track.class, "findById", id);
    }
  }

  public HttpStatus deleteAll() {
    try {
      trackRepository.deleteAll();
    } catch (Exception e) {
      throw new DatabaseException(Track.class, "deleteAll");
    }
    return HttpStatus.OK;
  }

  public HttpStatus deleteById(String id) {
    Track found = findById(id);
    if (found != null) {
      try {
        trackRepository.deleteById(id);
      } catch (Exception e) {
        throw new DatabaseException(Track.class, "deleteById", id);
      }
    } else {
      throw new EntityNotFoundException(Track.class, "id", id);
    }
    return HttpStatus.OK;
  }

  public HttpStatus create(NewTrack newTrack) {
    if (isNullOrEmpty(newTrack.getTitle())) {
      throw new InvalidParameterException(TRACK_TITLE_IS_NULL_OR_EMPTY);
    }
    if (isNullOrEmpty(newTrack.getArtistId())) {
      throw new InvalidParameterException(ARTIST_ID_IS_NULL_OR_EMPTY);
    }
    if (isNullOrEmpty(newTrack.getAlbumName())) {
      throw new InvalidParameterException(ALBUM_NAME_IS_NULL_OR_EMPTY);
    }
    if (!isReleaseDateValid(newTrack.getReleaseYear())) {
      throw new InvalidParameterException(RELEASE_YEAR_IS_INVALID,
          newTrack.getReleaseYear() == null ? null : Integer.toString(newTrack.getReleaseYear()));
    }
    if (!isDurationValid(newTrack.getDurationInSeconds())) {
      throw new InvalidParameterException(DURATION_IS_INVALID,
          newTrack.getDurationInSeconds() == null
              ? null : Integer.toString(newTrack.getDurationInSeconds()));
    }
    Artist found = artistService.findById(newTrack.getArtistId());
    if (found == null) {
      throw new EntityNotFoundException(Artist.class, "id", newTrack.getArtistId());
    }
    Track track = new Track(newTrack.getTitle(), newTrack.getDurationInSeconds());
    ArtistTrackRelationship artistTrackRelationship = new ArtistTrackRelationship(track, found,
        newTrack.getAlbumName(), newTrack.getReleaseYear());
    track.setArtistTrackRelationship(artistTrackRelationship);
    try {
      trackRepository.save(track);
    } catch (Exception e) {
      throw new DatabaseException(Track.class, "create", newTrack.getTitle(),
          newTrack.getAlbumName(), newTrack.getArtistId(),
          String.valueOf(newTrack.getDurationInSeconds()),
          String.valueOf(newTrack.getReleaseYear()));
    }
    return HttpStatus.CREATED;
  }
}
