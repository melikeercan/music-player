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
import com.tidal.refactoring.entities.dao.NewTrack;
import com.tidal.refactoring.entities.nodes.Artist;
import com.tidal.refactoring.entities.nodes.Track;
import com.tidal.refactoring.exceptions.InvalidParameterException;
import com.tidal.refactoring.services.TrackServiceImp;

import static com.tidal.refactoring.utils.Constants.CREATED_NEW_TRACK_SUCCESSFULLY;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.tidal.refactoring.utils.Constants.DELETED_ALL_TRACKS_SUCCESSFULLY;

@RunWith(SpringRunner.class)
@WebMvcTest(TrackController.class)
public class TrackControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  TrackServiceImp trackService;

  ObjectMapper mapper = new ObjectMapper();

  @Test
  public void shouldGetAllPlayLists() throws Exception {
    Track track = new Track("Test_Track_Name", 100);
    List<Track> list = singletonList(track);
    when(trackService.findAll()).thenReturn(list);
    String url = "/api/v0/tracks/";
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(track.getId())));
  }

  @Test
  public void shouldGetByTitle() throws Exception {
    String title = "Test_Track_Name";
    Track track = new Track(title, 100);
    when(trackService.findByTitle(title)).thenReturn(track);
    String url = String.format("/api/v0/tracks/title=%s", title);
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(track.getId())));
  }

  @Test
  public void shouldThrowExceptionIfTitleIsEmpty() throws Exception {
    when(trackService.findByTitle("")).thenThrow(InvalidParameterException.class);
    String url = "/api/v0/tracks/title=";
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("NOT_ACCEPTABLE")));
  }

  @Test
  public void shouldGetTracksLongerThan() throws Exception {
    String title = "Test_Track_Name";
    Integer duration = 100;
    Track track = new Track(title, duration);
    List<Track> list = singletonList(track);
    when(trackService.findByDurationLongerThan(duration)).thenReturn(list);
    String url = String.format("/api/v0/tracks/longerThan=%s", duration);
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(track.getId())));
  }

  @Test
  public void shouldGetByTrackId() throws Exception {
    String title = "Test_Track_Name";
    Integer duration = 100;
    Track track = new Track(title, duration);
    String id = track.getId();
    when(trackService.findById(id)).thenReturn(track);
    String url = String.format("/api/v0/tracks/id=%s", id);
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(track.getId())))
        .andExpect(content().string(containsString(title)));
  }

  @Test
  public void shouldDeleteAllTracks() throws Exception {
    when(trackService.deleteAll()).thenReturn(HttpStatus.OK);
    String url = "/api/v0/tracks/";

    mockMvc.perform(delete(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(DELETED_ALL_TRACKS_SUCCESSFULLY)));
  }

  @Test
  public void shouldDeleteTracksById() throws Exception {
    String title = "Test_Track_Name";
    Integer duration = 100;
    Track track = new Track(title, duration);
    String id = track.getId();
    when(trackService.findById(id)).thenReturn(track);
    String url = String.format("/api/v0/tracks/%s", id);
    when(trackService.deleteById(id)).thenReturn(HttpStatus.OK);

    mockMvc.perform(delete(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Track with id")));
  }

  @Test
  public void shouldAddTrack() throws Exception {
    String title = "Test_Track_Name";
    Integer duration = 100;
    Artist artist = new Artist("Test_Artist_Name");
    NewTrack newTrack = new NewTrack(title, duration, artist.getId(), "Test_Album_Name", 2020);
    when(trackService.create(newTrack)).thenReturn(HttpStatus.CREATED);
    String url = "/api/v0/tracks/";
    String input = mapper.writeValueAsString(newTrack);
    mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(input))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(CREATED_NEW_TRACK_SUCCESSFULLY)))
        .andExpect(content().string(containsString(title)))
        .andExpect(content().string(containsString(artist.getId())))
        .andExpect(content().string(containsString(newTrack.getAlbumName())))
        .andExpect(content().string(containsString(Integer.toString(duration))));
  }
}
