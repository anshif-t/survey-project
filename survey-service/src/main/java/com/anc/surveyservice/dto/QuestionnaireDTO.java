package com.anc.surveyservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionnaireDTO {

	private Long id;
	private String username;
	private String status;
	private SurveyDetailsDTO surveyDetails;
	private List<QuestionAnswerDTO> questionAnswer;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;

	// UI
	private Long surveyId;
}
