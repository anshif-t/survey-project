package com.anc.surveyservice.service;

import com.anc.surveyservice.dto.QuestionnaireDTO;

public interface QuestionnaireService {

	/**
	 * Method to save a questionnaire
	 * 
	 * @param questionnaireDTO
	 * @return
	 */
	String saveQuestionnaire(QuestionnaireDTO questionnaireDTO);

	/**
	 * 
	 * Method to fetch a questionnaire
	 * 
	 * @param username
	 * @param surveyId
	 * @return
	 */
	QuestionnaireDTO getQuestionnaire(String username, Long surveyId);

	
}
