package com.anc.surveyservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDTO {

	private Long id;
	private String questionLabel;
	private List<AnswerDTO> options;
	private SurveyDetailsDTO survey;
	private LocalDateTime createdDate;
	//UI
	private long surveyId;
}
