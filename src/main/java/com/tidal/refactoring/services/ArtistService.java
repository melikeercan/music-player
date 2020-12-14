package com.tidal.refactoring.services;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.tidal.refactoring.entities.nodes.Artist;

public interface ArtistService {
  List<Artist> findAll();

  Artist findByArtistByName(String name);

  Artist findById(String id);

  HttpStatus create(String name);

  HttpStatus deleteAll();

  HttpStatus deleteById(String id);
}
