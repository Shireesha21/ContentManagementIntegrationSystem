package com.aem.cmis.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aem.cmis.controller.ContentManagementController;
import com.aem.cmis.dto.ContentDTO;
import com.aem.cmis.model.Content;
import com.aem.cmis.service.ContentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import org.springframework.http.MediaType;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(ContentManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ContentManagementControllerTest {
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService service;

    @Autowired
    private ObjectMapper objectMapper;
    
    private ContentDTO contentDTO;
    private ContentDTO childDTO;

    
	@BeforeEach
	void setup() {
		contentDTO = createValidContentDTO();
		childDTO = createValidChildDTO();
	}
    

    private ContentDTO createValidContentDTO() {
        ContentDTO dto = new ContentDTO();
        dto.setTitle("Test Article");
        dto.setBody("This is a test article.");
        dto.setAuthor("Admin");
        dto.setTags(Arrays.asList("news"));
        dto.setId("1");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setPath("/content/article");
        return dto;
    }

    private ContentDTO createValidChildDTO() {
        ContentDTO child = new ContentDTO();
        child.setTitle("Child Section");
        child.setBody("This is a child section.");
        child.setAuthor("Admin");
        child.setTags(Arrays.asList("section"));
        child.setId("2");
        child.setCreatedAt(LocalDateTime.now());
        child.setUpdatedAt(LocalDateTime.now());
        child.setPath("content/child");
        return child;
    }


    @Test
    void createContent_invalidContent_returnsBadRequest() throws Exception {
        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setTitle("");
        contentDTO.setBody("");
        contentDTO.setAuthor("");
        contentDTO.setCreatedAt(LocalDateTime.now()); 

        mockMvc.perform(post("/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void createContent_nullContentDTO_returnsInternalServerError() throws Exception {
        when(service.createContent(null))
                .thenThrow(new ServiceException("ContentDTO cannot be null"));

        mockMvc.perform(post("/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

    @Test
    void createContent_invalidChildDTO_returnsInternalServerError() throws Exception {
        ContentDTO childDTO = new ContentDTO();
        childDTO.setTitle(""); // Invalid: empty title
        ContentDTO contentDTO = createValidContentDTO();
        contentDTO.setChildren(Arrays.asList(childDTO));

        when(service.createContent(any(ContentDTO.class)))
                .thenThrow(new ServiceException("Title is mandatory for ContentDTO"));

        mockMvc.perform(post("/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contentDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

    // 2. getContent Tests
    @Test
    void getContent_existingIdWithChildren_returnsOk() throws Exception {
        ContentDTO contentDTO = createValidContentDTO();
        contentDTO.setChildren(Arrays.asList(createValidChildDTO()));

        when(service.getContent("1")).thenReturn(Optional.of(contentDTO));

        mockMvc.perform(get("/content/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Test Article"))
                .andExpect(jsonPath("$.children[0].id").value("2"));
    }

    @Test
    void getContent_nonExistingId_returnsNotFound() throws Exception {
        when(service.getContent("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/content/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Content not found with id: 1"));
    }

    @Test
    void getContent_serviceThrowsException_returnsInternalServerError() throws Exception {
        when(service.getContent("1"))
                .thenThrow(new ServiceException("Failed to retrieve content"));

        mockMvc.perform(get("/content/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

    // 3. searchContent Tests
    @Test
    void searchContent_validTagAndAuthor_returnsOk() throws Exception {
    	 ContentDTO contentDTO = createValidContentDTO();
         contentDTO.setChildren(Arrays.asList(createValidChildDTO()));
         

        List<ContentDTO> results = Arrays.asList(contentDTO);

        when(service.searchContent("news", "Admin")).thenReturn(results);

        mockMvc.perform(get("/content/search")
                        .param("tag", "news")
                        .param("author", "Admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].title").value("Test Article"));
    }


    @Test
    void searchContent_noResults_returnsOkWithEmptyList() throws Exception {
        when(service.searchContent("unknown", null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/content/search")
                        .param("tag", "unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    void searchContent_serviceThrowsException_returnsInternalServerError() throws Exception {
        when(service.searchContent("news", "Admin"))
                .thenThrow(new ServiceException("Failed to search content"));

        mockMvc.perform(get("/content/search")
                        .param("tag", "news")
                        .param("author", "Admin"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

    // 4. updateContent Tests
    @Test
    void testUpdateContent_ExistingIdValidContent() throws Exception {
        ContentDTO updatedDTO = createValidContentDTO();
        updatedDTO.setId("1"); 
        updatedDTO.setTags(Arrays.asList("updated"));

        when(service.updateContent(eq("1"), any(ContentDTO.class))).thenReturn(Optional.of(updatedDTO));

        mockMvc.perform(patch("/content/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))               
                .andExpect(jsonPath("$.tags[0]").value("updated")); 
    }

    @Test
    void updateContent_nonExistingId_returnsNotFound() throws Exception {
        when(service.updateContent(eq("1"), any(ContentDTO.class))).thenReturn(Optional.empty());

        ContentDTO contentDTO = createValidContentDTO();

        mockMvc.perform(patch("/content/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contentDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Content not found with id: 1"));
    }

    @Test
    void updateContent_invalidContent_returnsBadRequest() throws Exception {

        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setTitle("");
        contentDTO.setBody("");
        contentDTO.setAuthor("");
        contentDTO.setCreatedAt(LocalDateTime.now()); 

        mockMvc.perform(patch("/content/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void updateContent_serviceThrowsException_returnsInternalServerError() throws Exception {
        ContentDTO contentDTO = createValidContentDTO();
        when(service.updateContent(eq("1"), any(ContentDTO.class)))
                .thenThrow(new ServiceException("Failed to update content"));

        mockMvc.perform(patch("/content/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contentDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

    // 5. deleteContent Tests
    @Test
    void deleteContent_existingId_returnsOk() throws Exception {
        when(service.deleteContent("1")).thenReturn(true);

        mockMvc.perform(delete("/content/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Content and children deleted"));
    }

    @Test
    void deleteContent_nonExistingId_returnsNotFound() throws Exception {
        when(service.deleteContent("1")).thenReturn(false);

        mockMvc.perform(delete("/content/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Content not found with id: 1"));
    }

    @Test
    void deleteContent_serviceThrowsException_returnsInternalServerError() throws Exception {
        when(service.deleteContent("1"))
                .thenThrow(new ServiceException("Failed to delete content"));

        mockMvc.perform(delete("/content/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
    
}
