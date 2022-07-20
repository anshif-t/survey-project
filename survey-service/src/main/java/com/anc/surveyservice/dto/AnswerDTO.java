package com.anc.surveyservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDTO {
	private Long id;
	private String answer;
	private QuestionDTO question;

	//UI
	private Long quesionId;
}
