package com.tidal.refactoring.api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tidal.refactoring.entities.dao.NewArtist;
import com.tidal.refactoring.entities.nodes.Artist;
import com.tidal.refactoring.exceptions.InvalidParameterException;
import com.tidal.refactoring.services.ArtistServiceImp;

import static com.tidal.refactoring.utils.Constants.CREATED_NEW_ARTIST_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.DELETED_ALL_ARTISTS_SUCCESSFULLY;
import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static java.util.Collections.singletonList;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(ArtistController.class)
public class ArtistControllerTests {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ArtistServiceImp artistService;

  ObjectMapper mapper = new ObjectMapper();

  @Test
  public void shouldGetAllArtists() throws Exception {
    Artist artist = new Artist("Test_Artist_Name");
    List<Artist> list = singletonList(artist);
    String contentExpected = mapper.writeValueAsString(list);
    System.out.println(contentExpected);
    when(artistService.findAll()).thenReturn(list);
    String url = "/api/v0/artists/";
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(contentExpected)));
  }

  @Test
  public void shouldGetByArtistName() throws Exception {

    String name = "Test_Artist_Name";
    Artist artist = new Artist(name);
    when(artistService.findByArtistByName(name)).thenReturn(artist);
    String url = String.format("/api/v0/artists/name=%s", name);
    String contentExpected = mapper.writeValueAsString(artist);
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(contentExpected)));
  }

  @Test
  public void shouldThrowExceptionIfNameIsEmpty() throws Exception {
    String name = "";
    when(artistService.findByArtistByName(name)).thenThrow(InvalidParameterException.class);
    String url = String.format("/api/v0/artists/name=%s", name);
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("NOT_ACCEPTABLE")));
  }

  @Test
  public void shouldGetByArtistId() throws Exception {
    String name = "Test_Artist_Name";
    Artist artist = new Artist(name);
    String id = artist.getId();
    when(artistService.findById(id)).thenReturn(artist);
    String url = String.format("/api/v0/artists/id=%s", id);
    String contentExpected = mapper.writeValueAsString(artist);
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(contentExpected)));
  }

  @Test
  public void shouldThrowExceptionIfIdIsEmpty() throws Exception {
    String id = "";
    when(artistService.findById(id)).thenThrow(InvalidParameterException.class);
    String url = String.format("/api/v0/artists/id=%s", id);
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("NOT_ACCEPTABLE")));
  }

  @Test
  public void shouldAddArtists() throws Exception {
    String name = "Test_Artist_Name";
    when(artistService.create(name)).thenReturn(HttpStatus.CREATED);
    String url = "/api/v0/artists/";
    NewArtist newArtist = new NewArtist(name);
    String input = mapper.writeValueAsString(newArtist);
    mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(input))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(CREATED_NEW_ARTIST_SUCCESSFULLY)));
  }

  @Test
  public void shouldThrowExceptionIdNameIsEmpty() throws Exception {
    String name = "";
    when(artistService.create(name)).thenThrow(InvalidParameterException.class);
    String url = "/api/v0/artists/";
    NewArtist newArtist = new NewArtist(name);
    String input = mapper.writeValueAsString(newArtist);
    mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(input))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("NOT_ACCEPTABLE")));
  }

  @Test
  public void shouldDeleteAllArtists() throws Exception {
    when(artistService.deleteAll()).thenReturn(HttpStatus.OK);
    String url = "/api/v0/artists/";

    mockMvc.perform(delete(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(DELETED_ALL_ARTISTS_SUCCESSFULLY)));
  }

  @Test
  public void shouldDeleteArtistById() throws Exception {
    String name = "Test_Artist_Name";
    Artist artist = new Artist(name);
    String id = artist.getId();
    when(artistService.findById(id)).thenReturn(artist);
    String url = String.format("/api/v0/artists/%s", id);
    when(artistService.deleteById(id)).thenReturn(HttpStatus.OK);

    mockMvc.perform(delete(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Artist with id")));
  }

  @Test
  public void shouldThrowExceptionIfIdIsNull() throws Exception {
    String id = "null";
    when(artistService.deleteById(id)).thenThrow(InvalidParameterException.class);
    String url = "/api/v0/artists/null";
    mockMvc.perform(delete(url))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("NOT_ACCEPTABLE")));
  }
}
