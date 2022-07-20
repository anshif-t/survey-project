package com.anc.surveyservice.util;

public interface Constants {

	/*
	 * API response statuses
	 */
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
	public static final String DUPLICATE = "DUPLICATE";
	
	/*
	 * API response messages
	 */
	public static final String CREATE_SURVEY_SUCCESS = "Successfully created the survey.";
	public static final String CREATE_SURVEY_FAILED = "Failed to created the survey.";
	public static final String CREATE_SURVEY_DUPLICATE = "The survey already exists";
	public static final String FETCH_SURVEY_SUCCESS = "Successfully fetched survey.";
	public static final String FETCH_SURVEY_FAILED = "Failed to fetch the survey.";
	public static final String CREATE_QUESTION_SUCCESS = "Successfully created the question.";
	public static final String CREATE_QUESTION_FAILED = "Failed to created the question.";
	public static final String FETCH_QUESTION_SUCCESS = "Successfully fetched questions.";
	public static final String FETCH_QUESTION_FAILED = "Failed to fetch questions.";
	public static final String CREATE_QUESTIONNAIRE_SUCCESS = "Successfully saved the Questionnaire.";
	public static final String CREATE_QUESTIONNAIRE_FAILED = "Failed to save the Questionnaire.";
	public static final String FETCH_QUESTIONNAIRE_FAILED = "Failed to fetch the Questionnaire.";
	public static final String FETCH_QUESTIONNAIRE_SUCCESS = "Successfully fetched Questionnaire.";
	
	
	/*
	 * API response status codes
	 */
	public static final String STATUS_CODE_200 = "200";
	public static final String STATUS_CODE_500 = "500";
	public static final String STATUS_CODE_406 = "406";
	
	public static final String SORT_ASCENDING = "asc";
	
	/*
	 * Questionnaire statuses
	 */
	public static final String STATUS_COMPLETED = "Completed";
	public static final String STATUS_IN_PROGRESS = "In Progress";
	
	
}
