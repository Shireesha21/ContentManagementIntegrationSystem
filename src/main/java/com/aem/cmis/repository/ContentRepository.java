package com.aem.cmis.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.aem.cmis.model.Content;
import java.util.*;

@Repository
public class ContentRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentRepository.class);
    private final Map<String, Content> contentStore = new HashMap<>();

    public Content save(Content content) {
        String id = UUID.randomUUID().toString();
        content.setId(id);
        content.setCreatedAt(LocalDateTime.now());
        content.setUpdatedAt(LocalDateTime.now());
        content.setJcrMetadata(createJcrMetadata(content.getCreatedAt()));

        // Save children recursively
        List<Content> savedChildren = new ArrayList<>();
        for (Content child : content.getChildren()) {
            savedChildren.add(saveChild(child));
        }
        content.setChildren(savedChildren);

        contentStore.put(id, content);
        return content;
    }

    private Content saveChild(Content child) {
        String childId = UUID.randomUUID().toString();
        child.setId(childId);
        child.setCreatedAt(LocalDateTime.now());
        child.setUpdatedAt(LocalDateTime.now());
        child.setJcrMetadata(createJcrMetadata(child.getCreatedAt()));

        // Recursively save child's children
        List<Content> savedGrandChildren = new ArrayList<>();
        for (Content grandChild : child.getChildren()) {
            savedGrandChildren.add(saveChild(grandChild));
        }
        child.setChildren(savedGrandChildren);

        contentStore.put(childId, child);
        return child;
    }

    private Map<String, Object> createJcrMetadata(LocalDateTime createdAt) {
        return new HashMap<String, Object>() {{
            put("jcr:primaryType", "nt:unstructured");
            put("jcr:created", createdAt.toString());
        }};
    }

    public Optional<Content> findById(String id) {
        Content content = contentStore.get(id);
        if (content == null) {
            return Optional.empty();
        }
        // Ensure children are fully loaded
        Content clonedContent = cloneContent(content);
        return Optional.of(clonedContent);
    }

    public List<Content> findByTagOrAuthor(String tag, String author) {
        return contentStore.values().stream()
                .filter(content -> (tag == null || content.getTags().contains(tag)) &&
                        (author == null || content.getAuthor().equalsIgnoreCase(author)))
                .map(this::cloneContent)
                .collect(Collectors.toList());
    }

    public Content update(String id, Content updatedContent) {
        Content existingContent = contentStore.get(id);
        if (existingContent == null) {
            throw new RuntimeException("Content not found: " + id);
        }
        existingContent.setTags(updatedContent.getTags());
        existingContent.setUpdatedAt(LocalDateTime.now());
        existingContent.getJcrMetadata().put("jcr:lastModified", existingContent.getUpdatedAt().toString());

        List<Content> savedChildren = new ArrayList<>();

        for (Content child : updatedContent.getChildren()) {
        	savedChildren.add(saveChild(child));
        }
        existingContent.setChildren(savedChildren);

        contentStore.put(id, existingContent);
        return cloneContent(existingContent);
    }

    public void delete(String id) {
        Content content = contentStore.get(id);
        if (content != null) {
            deleteChildren(content.getChildren());
            contentStore.remove(id);
        }
    }

    private void deleteChildren(List<Content> children) {
        for (Content child : children) {
            deleteChildren(child.getChildren());
            contentStore.remove(child.getId());
        }
    }

    private Content cloneContent(Content content) {
        Content clone = new Content();
        clone.setId(content.getId());
        clone.setTitle(content.getTitle());
        clone.setBody(content.getBody());
        clone.setAuthor(content.getAuthor());
        clone.setTags(new ArrayList<>(content.getTags()));
        clone.setCreatedAt(content.getCreatedAt());
        clone.setUpdatedAt(content.getUpdatedAt());
        clone.setJcrMetadata(new HashMap<>(content.getJcrMetadata()));
        List<Content> clonedChildren = content.getChildren().stream()
                .map(this::cloneContent)
                .collect(Collectors.toList());
        clone.setChildren(clonedChildren);
        return clone;
    }
    
}
