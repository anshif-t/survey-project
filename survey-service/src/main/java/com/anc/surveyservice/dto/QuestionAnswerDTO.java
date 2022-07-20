package com.anc.surveyservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionAnswerDTO {
	private Long id;
	private QuestionDTO question;
	private AnswerDTO answer;
	private QuestionnaireDTO questionnaire;
	
	//UI
	private Long questionId;
	private Long answerId;
}
