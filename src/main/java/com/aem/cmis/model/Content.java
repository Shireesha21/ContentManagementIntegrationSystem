package com.aem.cmis.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Content {
	@NotBlank
	private String id;
	@NotBlank(message = "Title is mandatory")
	@Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
	private String title;
	@NotBlank(message = "Body is mandatory")
	private String body;
	@NotBlank(message = "Author is mandatory")
	private String author;
	@Size(max = 10, message = "Maximum 10 tags allowed")
	private List<String> tags;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@NotBlank(message = "Path is mandatory")
    private String path;

	// Simulating AEM JCR structure
	private Map<String, Object> jcrMetadata;
	private List<Content> children = new ArrayList<>();

}
