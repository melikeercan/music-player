package com.tidal.refactoring.repositories;

import java.util.List;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.tidal.refactoring.entities.nodes.Artist;

@RepositoryRestResource(path = "artists")
public interface ArtistRepository extends Repository<Artist, String> {
  List<Artist> findAll();

  Artist findArtistByArtistName(String artistName);

  Artist findById(String id);

  void save(Artist artist);

  void deleteAll();

  void deleteById(String id);
}
