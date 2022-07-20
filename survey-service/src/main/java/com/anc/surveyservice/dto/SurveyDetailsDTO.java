package com.anc.surveyservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyDetailsDTO {
	private Long id;
	private String name;
	private String description;
	private List<QuestionDTO> questions;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
}
