package com.tidal.refactoring.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
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
import com.tidal.refactoring.entities.dao.EditPlayList;
import com.tidal.refactoring.entities.dao.NewPlayList;
import com.tidal.refactoring.entities.dao.NewTrackToPlayList;
import com.tidal.refactoring.entities.dao.RemoveFromPlayList;
import com.tidal.refactoring.entities.nodes.PlayList;
import com.tidal.refactoring.entities.nodes.Track;
import com.tidal.refactoring.exceptions.InvalidParameterException;
import com.tidal.refactoring.services.PlayListServiceImp;

import static com.tidal.refactoring.utils.Constants.ADDED_TRACK_TO_PLAYLIST_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.CREATED_NEW_PLAYLIST_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.DELETED_ALL_PLAYLISTS_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.REMOVED_TRACK_FROM_PLAYLIST_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.UPDATED_PLAYLIST_NAME_SUCCESSFULLY;
import static com.tidal.refactoring.utils.Constants.UPDATED_TRACK_INDEX_SUCCESSFULLY;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PlayListController.class)
public class PlayListControllerTests {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  PlayListServiceImp playListService;

  ObjectMapper mapper = new ObjectMapper();

  @Test
  public void shouldGetAllPlayLists() throws Exception {
    PlayList playList = new PlayList("Test_PlayList_Name");
    List<PlayList> list = singletonList(playList);
    when(playListService.findAll()).thenReturn(list);
    String url = "/api/v0/playlists/";
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(playList.getId())));
  }

  @Test
  public void shouldGetAllActivePlayLists() throws Exception {
    PlayList playList = new PlayList("Test_PlayList_Name");
    List<PlayList> list = singletonList(playList);
    when(playListService.findByActive(true)).thenReturn(list);
    String url = String.format("/api/v0/playlists/isActive=%s", true);
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(playList.getId())));
  }

  @Test
  public void shouldGetByArtistName() throws Exception {
    String name = "Test_PlayList_Name";
    PlayList playList = new PlayList(name);
    when(playListService.findByName(name)).thenReturn(playList);
    String url = String.format("/api/v0/playlists/name=%s", name);
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(playList.getId())));
  }

  @Test
  public void shouldThrowExceptionIfNameIsEmpty() throws Exception {
    when(playListService.findByName("")).thenThrow(InvalidParameterException.class);
    String url = "/api/v0/playlists/name=";
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("NOT_ACCEPTABLE")));
  }

  @Test
  public void shouldGetPlayListsCreatedAfter() throws Exception {
    PlayList playList = new PlayList("Test_PlayList_Name");
    List<PlayList> list = singletonList(playList);
    Date date = new Date();
    Long dateValue = date.getTime();
    when(playListService.findAllCreatedAfter(dateValue)).thenReturn(list);
    String url = String.format("/api/v0/playlists/createdAfter=%s", dateValue);
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(playList.getId())));
  }

  @Test
  public void shouldSortByDate() throws Exception {
    PlayList playList1 = new PlayList("Test_PlayList_Name");
    PlayList playList2 = new PlayList("Test_PlayList_Name");
    List<PlayList> list = new ArrayList<>(List.of(playList1, playList2));
    when(playListService.sortByLastUpdateDate()).thenReturn(list);
    String url = "/api/v0/playlists/sortByLastUpdated";
    Assert.assertFalse(playList1.getRegisteredDate().after(playList2.getRegisteredDate()));
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(playList1.getId())))
        .andExpect(content().string(containsString(playList2.getId())));
  }

  @Test
  public void shouldGetByPlayListId() throws Exception {
    String playListName = "Test_PlayList_Name";
    PlayList playList = new PlayList(playListName);
    String id = playList.getId();
    when(playListService.findById(id)).thenReturn(playList);
    String url = String.format("/api/v0/playlists/id=%s", id);
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(playList.getId())))
        .andExpect(content().string(containsString(playListName)));
  }

  @Test
  public void shouldThrowExceptionIfIdIsEmpty() throws Exception {
    when(playListService.findById("")).thenThrow(InvalidParameterException.class);
    String url = "/api/v0/playlists/id=";
    mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("NOT_ACCEPTABLE")));
  }

  @Test
  public void shouldDeleteAllPlayLists() throws Exception {
    when(playListService.deleteAll()).thenReturn(HttpStatus.OK);
    String url = "/api/v0/playlists/";

    mockMvc.perform(delete(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(DELETED_ALL_PLAYLISTS_SUCCESSFULLY)));
  }

  @Test
  public void shouldDeletePlayListsById() throws Exception {
    String name = "Test_Playlist_Name";
    PlayList playList = new PlayList(name);
    String id = playList.getId();
    when(playListService.findById(id)).thenReturn(playList);
    String url = String.format("/api/v0/playlists/%s", id);
    when(playListService.deleteById(id)).thenReturn(HttpStatus.OK);

    mockMvc.perform(delete(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("PlayList with id")));
  }

  @Test
  public void shouldAddPlayList() throws Exception {
    String name = "Test_Playlist_Name";
    NewPlayList newPlayList = new NewPlayList(name);
    when(playListService.create(newPlayList)).thenReturn(HttpStatus.CREATED);
    String url = "/api/v0/playlists/";
    String input = mapper.writeValueAsString(newPlayList);
    mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(input))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(CREATED_NEW_PLAYLIST_SUCCESSFULLY)));
  }

  @Test
  public void shouldAddTrackToPlayList() throws Exception {
    String playListName = "Test_Playlist_Name";
    PlayList playList = new PlayList(playListName);
    String trackName = "Test_Track_Name";
    Track track = new Track(trackName, 100);
    Integer index = 0;
    NewTrackToPlayList newTrackToPlayList = new NewTrackToPlayList(playList.getId(), track.getId(), index);
    when(playListService.add(newTrackToPlayList)).thenReturn(HttpStatus.OK);
    String url = "/api/v0/playlists/track/add";
    String input = mapper.writeValueAsString(newTrackToPlayList);
    mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(input))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(ADDED_TRACK_TO_PLAYLIST_SUCCESSFULLY)));
  }

  @Test
  public void shouldRemoveTrackFromPlayList() throws Exception {
    String playListName = "Test_Playlist_Name";
    PlayList playList = new PlayList(playListName);
    String trackName = "Test_Track_Name";
    Track track = new Track(trackName, 100);
    RemoveFromPlayList removeFromPlayList = new RemoveFromPlayList(playList.getId(), track.getId());
    when(playListService.remove(removeFromPlayList)).thenReturn(HttpStatus.OK);
    String url = "/api/v0/playlists/track/remove";
    String input = mapper.writeValueAsString(removeFromPlayList);
    mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(input))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(REMOVED_TRACK_FROM_PLAYLIST_SUCCESSFULLY)));
  }

  @Test
  public void shouldUpdateTrackIndexInPlayList() throws Exception {
    String playListName = "Test_Playlist_Name";
    PlayList playList = new PlayList(playListName);
    String trackName = "Test_Track_Name";
    Track track = new Track(trackName, 100);
    Integer index = 0;
    NewTrackToPlayList trackIndexInfoToUpdate = new NewTrackToPlayList(playList.getId(), track.getId(), index);
    when(playListService.updateTrackIndex(trackIndexInfoToUpdate)).thenReturn(HttpStatus.OK);
    String url = "/api/v0/playlists/track/index";
    String input = mapper.writeValueAsString(trackIndexInfoToUpdate);
    mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(input))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(UPDATED_TRACK_INDEX_SUCCESSFULLY)));
  }

  @Test
  public void shouldUpdatePlayListName() throws Exception {
    String playListName = "Test_Playlist_Name";
    String newName = "New_Playlist_Name";
    PlayList playList = new PlayList(playListName);
    EditPlayList editPlayList = new EditPlayList(newName, playList.getId());
    when(playListService.updateName(editPlayList)).thenReturn(HttpStatus.OK);
    String url = "/api/v0/playlists/update/name";
    String input = mapper.writeValueAsString(editPlayList);
    mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(input))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(UPDATED_PLAYLIST_NAME_SUCCESSFULLY)));
  }


}
