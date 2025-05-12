package com.aem.cmis.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import com.aem.cmis.dto.ContentDTO;
import com.aem.cmis.mapper.ContentMapper;
import com.aem.cmis.model.Content;
import com.aem.cmis.repository.ContentRepository;

@Service
public class ContentService {

	private final ContentRepository repository;
    private final ContentMapper contentMapper;

    public ContentService(ContentRepository repository, ContentMapper contentMapper) {
        this.repository = repository;
        this.contentMapper = contentMapper;
    }

    public ContentDTO createContent(ContentDTO contentDTO) {
        try {
            if (contentDTO == null) {
                throw new ServiceException("ContentDTO cannot be null");
            }
            Content content = contentMapper.mapToContent(contentDTO);
            content.setCreatedAt(LocalDateTime.now());
            content.setUpdatedAt(LocalDateTime.now());
            content.setJcrMetadata(new HashMap<String, Object>() {{
                put("jcr:primaryType", "nt:unstructured");
                put("jcr:created", content.getCreatedAt().toString());
            }});
            for (Content child : content.getChildren()) {
                child.setCreatedAt(LocalDateTime.now());
                child.setUpdatedAt(LocalDateTime.now());
                child.setJcrMetadata(new HashMap<String, Object>() {{
                    put("jcr:primaryType", "nt:unstructured");
                    put("jcr:created", child.getCreatedAt().toString());
                }});
            }
            Content savedContent = repository.save(content);
            return contentMapper.mapToContentDTO(savedContent);
        } catch (ServiceException e) {
            throw e; 
        } catch (Exception e) {
            throw new ServiceException("Failed to create content", e);
        }
    }

    public Optional<ContentDTO> getContent(String id) {
        try {
            Optional<Content> content = repository.findById(id);
            return content.map(contentMapper::mapToContentDTO);
        } catch (ServiceException e) {
            throw e; 
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve content with id: " + id, e);
        }
    }

    public List<ContentDTO> searchContent(String tag, String author) {
        try {
            List<Content> results = repository.findByTagOrAuthor(tag, author);
            return results.stream().map(contentMapper::mapToContentDTO).collect(Collectors.toList());
        } catch (ServiceException e) {
            throw e; 
        } catch (Exception e) {
            throw new ServiceException("Failed to search content", e);
        }
    }

    public Optional<ContentDTO> updateContent(String id, ContentDTO contentDTO) {
        try {
            if (contentDTO == null) {
                throw new ServiceException("ContentDTO cannot be null");
            }
            Optional<Content> existingContent = repository.findById(id);
            if (existingContent.isEmpty()) {
                return Optional.empty();
            }
            Content content = contentMapper.mapToContent(contentDTO);
            Content updatedContent = existingContent.get();
            updatedContent.setTags(content.getTags());
            updatedContent.setChildren(content.getChildren());
            updatedContent.setUpdatedAt(LocalDateTime.now());
            updatedContent.getJcrMetadata().put("jcr:lastModified", updatedContent.getUpdatedAt().toString());
            Content savedContent = repository.update(id, updatedContent);
            return Optional.of(contentMapper.mapToContentDTO(savedContent));
        } catch (ServiceException e) {
            throw e; 
        } catch (Exception e) {
            throw new ServiceException("Failed to update content with id: " + id, e);
        }
    }

    public boolean deleteContent(String id) {
        try {
            Optional<Content> content = repository.findById(id);
            if (content.isEmpty()) {
                return false;
            }
            repository.delete(id);
            return true;
        } catch (Exception e) {
            throw new ServiceException("Failed to delete content with id: " + id, e);
        }
    }

}
