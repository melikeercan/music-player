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

import com.tidal.refactoring.entities.dao.NewArtist;
import com.tidal.refactoring.exceptions.DatabaseException;
import com.tidal.refactoring.exceptions.EntityAlreadyExistsException;
import com.tidal.refactoring.exceptions.EntityNotFoundException;
import com.tidal.refactoring.exceptions.InvalidParameterException;
import com.tidal.refactoring.responses.RestCallResponse;
import com.tidal.refactoring.services.ArtistServiceImp;

import static com.tidal.refactoring.utils.Constants.ARTIST_WITH_ID_DELETED_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.CREATED_NEW_ARTIST_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.DELETED_ALL_ARTISTS_SUCCESSFULLY;

@RestController
@RequestMapping("/api/v0/artists")
public class ArtistController {

  private final ArtistServiceImp artistService;

  public ArtistController(ArtistServiceImp artistService) {
    this.artistService = artistService;
  }

  @GetMapping("/")
  RestCallResponse getAllArtist() throws DatabaseException {
    return new RestCallResponse(HttpStatus.OK, artistService.findAll());
  }

  @GetMapping("/name={name}")
  RestCallResponse getByArtistName(@PathVariable("name") String name)
      throws InvalidParameterException, EntityNotFoundException, DatabaseException {
    return new RestCallResponse(HttpStatus.OK, artistService.findByArtistByName(name));
  }

  @GetMapping("/id={id}")
  RestCallResponse getById(@PathVariable("id") String id)
      throws InvalidParameterException, DatabaseException {
    return new RestCallResponse(HttpStatus.OK, artistService.findById(id));
  }

  @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  RestCallResponse addNewArtist(@RequestBody NewArtist newArtist)
      throws InvalidParameterException, EntityAlreadyExistsException,
      DatabaseException {
    artistService.create(newArtist.getName());
    return new RestCallResponse(HttpStatus.CREATED, CREATED_NEW_ARTIST_SUCCESSFULLY, newArtist);
  }

  @DeleteMapping("/")
  RestCallResponse deleteAllArtists() throws DatabaseException {
    artistService.deleteAll();
    return new RestCallResponse(HttpStatus.OK, DELETED_ALL_ARTISTS_SUCCESSFULLY);
  }

  @DeleteMapping("/{id}")
  RestCallResponse deleteById(@PathVariable String id)
      throws DatabaseException, EntityNotFoundException {
    artistService.deleteById(id);
    return new RestCallResponse(HttpStatus.OK,
        String.format(ARTIST_WITH_ID_DELETED_SUCCESSFULLY, id));
  }
}
