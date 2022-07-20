package com.anc.surveyservice.service;

import java.util.List;

import com.anc.surveyservice.dto.QuestionDTO;
import com.anc.surveyservice.dto.SurveyDetailsDTO;

public interface SurveyService {

	/**
	 * Method to create a Survey
	 * 
	 * @param surveyDetailsDTO
	 * @return
	 */
	String createSurvey(SurveyDetailsDTO surveyDetailsDTO);

	/**
	 * 
	 * Method to fetch the Surveys
	 * 
	 * @param searchKey
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	List<SurveyDetailsDTO> getSurveys(String searchKey, int limit, int offset, String sortBy, String sortOrder);

	/**
	 * Method to add a new question
	 * 
	 * @param questionDTO
	 * @return
	 */
	String addQuestion(QuestionDTO questionDTO);

	/**
	 * 
	 * Method to fetch questions corresponding to a Survey
	 * 
	 * @param surveyId
	 * @param searchKey
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	List<QuestionDTO> getQuestions(Long surveyId, String searchKey, int limit, int offset, String sortBy, String sortOrder);

}
