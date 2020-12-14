package com.tidal.refactoring.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tidal.refactoring.entities.nodes.Artist;
import com.tidal.refactoring.exceptions.DatabaseException;
import com.tidal.refactoring.exceptions.EntityAlreadyExistsException;
import com.tidal.refactoring.exceptions.EntityNotFoundException;
import com.tidal.refactoring.exceptions.InvalidParameterException;
import com.tidal.refactoring.repositories.ArtistRepository;

import static com.tidal.refactoring.utils.Constants.ARTIST_NAME_IS_NULL_OR_EMPTY;

@Service
public class ArtistServiceImp extends BaseServiceImp implements ArtistService {
  private final ArtistRepository artistRepository;

  ArtistServiceImp(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  public List<Artist> findAll() {
    try {
      return artistRepository.findAll();
    } catch (Exception ex) {
      throw new DatabaseException(Artist.class, "findAll");
    }
  }

  public Artist findByArtistByName(String name) {
    if (name == null || name.isEmpty()) {
      throw new InvalidParameterException(ARTIST_NAME_IS_NULL_OR_EMPTY);
    }
    Artist found;
    try {
      found = artistRepository.findArtistByArtistName(name);
    } catch (Exception ex) {
      throw new DatabaseException(Artist.class, "findByArtistByName", name);
    }
    if (found == null) {
      throw new EntityNotFoundException(Artist.class, "name", name);
    }
    return found;
  }

  public Artist findById(String id) {
    if (id == null || id.isEmpty()) {
      throw new InvalidParameterException(ARTIST_NAME_IS_NULL_OR_EMPTY);
    }
    try {
      return artistRepository.findById(id);
    } catch (Exception ex) {
      throw new DatabaseException(Artist.class, "findById", id);
    }
  }

  public HttpStatus create(String name) {
    if (name == null || name.isEmpty()) {
      throw new InvalidParameterException(ARTIST_NAME_IS_NULL_OR_EMPTY);
    }
    Artist found;
    try {
      found = artistRepository.findArtistByArtistName(name);
    } catch (Exception ex) {
      throw new DatabaseException(Artist.class, "create", name);
    }
    if (found != null) {
      throw new EntityAlreadyExistsException(Artist.class, "name", name);
    }
    Artist artist = new Artist(name);
    try {
      artistRepository.save(artist);
    } catch (Exception ex) {
      throw new DatabaseException(Artist.class, "create", name);
    }

    return HttpStatus.CREATED;
  }

  public HttpStatus deleteAll() {
    try {
      artistRepository.deleteAll();
    } catch (Exception e) {
      throw new DatabaseException(Artist.class, "deleteAll");
    }
    return HttpStatus.OK;
  }

  public HttpStatus deleteById(String id) {
    Artist found = findById(id);
    if (found == null) {
      throw new EntityNotFoundException(Artist.class, "id", id);
    }
    try {
      artistRepository.deleteById(id);
    } catch (Exception e) {
      throw new DatabaseException(Artist.class, "deleteById", id);
    }
    return HttpStatus.OK;
  }
}
