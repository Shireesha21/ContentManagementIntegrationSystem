package com.aem.cmis.controller;

import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aem.cmis.dto.ContentDTO;
import com.aem.cmis.exception.ResourceNotFoundException;
import com.aem.cmis.service.ContentService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/content")
@Tag(name = "Content Management System", description = "API for managing content in an AEM like system")
public class ContentManagementController {

	private final ContentService contentService;

	public ContentManagementController(ContentService contentService) {
		this.contentService = contentService;
	}

	@PostMapping
	@Operation(summary = "Create new content with optional children")
	public ResponseEntity<ContentDTO> createContent(@Valid @RequestBody ContentDTO contentDTO) {
		try {
			ContentDTO createdDTO = contentService.createContent(contentDTO);
			return new ResponseEntity<>(createdDTO, HttpStatus.CREATED);
		} catch (ServiceException e) {
			throw new ServiceException("Error creating content: " + e.getMessage(), e);
		}
	}

	@GetMapping("/{id}")
	@Operation(summary = "Retrieve content by ID with children")
	public ResponseEntity<ContentDTO> getContent(@PathVariable("id") String id) {
		try {
			return contentService.getContent(id).map(contentDTO -> new ResponseEntity<>(contentDTO, HttpStatus.OK))
					.orElseThrow(() -> new ResourceNotFoundException("Content not found with id: " + id));
		} catch (ServiceException e) {
			throw new ServiceException("Error retrieving content with id: " + id + ": " + e.getMessage(), e);
		}
	}

	@GetMapping("/search")
	@Operation(summary = "Search content by tag or author, including children")
	public ResponseEntity<List<ContentDTO>> searchContent(@RequestParam(value="tag",required = false, defaultValue = "tag") String tag,
			@RequestParam(value="author",required = false) String author) {
		try {
			List<ContentDTO> resultDTOs = contentService.searchContent(tag, author);
			return new ResponseEntity<>(resultDTOs, HttpStatus.OK);
		} catch (ServiceException e) {
			throw new ServiceException("Error searching content: " + e.getMessage(), e);
		}
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update content metadata and children")
	public ResponseEntity<ContentDTO> updateContent(@PathVariable("id") String id,
			@Valid @RequestBody ContentDTO contentDTO) {
		try {
			return contentService.updateContent(id, contentDTO)
					.map(updatedDTO -> new ResponseEntity<>(updatedDTO, HttpStatus.OK))
					.orElseThrow(() -> new ResourceNotFoundException("Content not found with id: " + id));
		} catch (ServiceException e) {
			throw new ServiceException("Error updating content with id: " + id + ": " + e.getMessage(), e);
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete content and its children by ID")
	public ResponseEntity<String> deleteContent(@PathVariable("id") String id) {
		try {
			boolean deleted = contentService.deleteContent(id);
			if (deleted) {
				return new ResponseEntity<>("Content and children deleted", HttpStatus.OK);
			}
			throw new ResourceNotFoundException("Content not found with id: " + id);
		} catch (ServiceException e) {
			throw new ServiceException("Error deleting content with id: " + id + ": " + e.getMessage(), e);
		}
	}

}
