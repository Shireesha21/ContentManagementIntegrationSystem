package com.aem.cmis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aem.cmis.mapper.ContentMapper;
import com.aem.cmis.repository.ContentRepository;
import com.aem.cmis.service.ContentService;

@Configuration
public class AppConfig {
	@Bean
    public ContentRepository contentRepository() {
        return new ContentRepository();
    }

    @Bean
    public ContentMapper contentMapper() {
        return new ContentMapper();
    }

    @Bean
    public ContentService contentService(ContentRepository repository, ContentMapper contentMapper) {
        return new ContentService(repository, contentMapper);
    }

}
