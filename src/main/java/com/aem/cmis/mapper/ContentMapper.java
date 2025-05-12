package com.aem.cmis.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Component;

import com.aem.cmis.dto.ContentDTO;
import com.aem.cmis.model.Content;

@Component
public class ContentMapper {
	
	public Content mapToContent(ContentDTO dto) {
		try {
            if (dto == null) {
                throw new ServiceException("ContentDTO cannot be null");
            }
            if (dto.getTitle() == null || dto.getTitle().isBlank()) {
                throw new ServiceException("Title is mandatory for ContentDTO");
            }
            if (dto.getBody() == null || dto.getBody().isBlank()) {
                throw new ServiceException("Body is mandatory for ContentDTO");
            }
            if (dto.getAuthor() == null || dto.getAuthor().isBlank()) {
                throw new ServiceException("Author is mandatory for ContentDTO");
            }

            Content content = new Content();
            content.setId(dto.getId());
            content.setTitle(dto.getTitle());
            content.setBody(dto.getBody());
            content.setAuthor(dto.getAuthor());
            content.setTags(dto.getTags());
            content.setCreatedAt(dto.getCreatedAt());
            content.setUpdatedAt(dto.getUpdatedAt());
            content.setJcrMetadata(dto.getJcrMetadata());
            content.setChildren(dto.getChildren() != null
                    ? dto.getChildren().stream().map(this::mapToContent).collect(Collectors.toList())
                    : Collections.emptyList());
            return content;
        } catch (Exception e) {
            throw new ServiceException("Failed to map ContentDTO to Content: " + e.getMessage(), e);
        }
    }

    public ContentDTO mapToContentDTO(Content content) {
    	try {
            if (content == null) {
                throw new ServiceException("Content cannot be null");
            }
            ContentDTO dto = new ContentDTO();
            dto.setId(content.getId());
            dto.setTitle(content.getTitle());
            dto.setBody(content.getBody());
            dto.setAuthor(content.getAuthor());
            dto.setTags(content.getTags());
            dto.setCreatedAt(content.getCreatedAt());
            dto.setUpdatedAt(content.getUpdatedAt());
            dto.setJcrMetadata(content.getJcrMetadata());
            dto.setChildren(content.getChildren() != null
                    ? content.getChildren().stream().map(this::mapToContentDTO).collect(Collectors.toList())
                    : Collections.emptyList());
            return dto;
        } catch (Exception e) {
            throw new ServiceException("Failed to map Content to ContentDTO: " + e.getMessage(), e);
        }
    }
}
