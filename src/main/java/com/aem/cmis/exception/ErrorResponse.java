package com.aem.cmis.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponse {
	private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<String> details;
}
