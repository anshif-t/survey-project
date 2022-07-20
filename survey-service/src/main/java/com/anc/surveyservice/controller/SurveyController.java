package com.anc.surveyservice.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anc.surveyservice.dto.QuestionDTO;
import com.anc.surveyservice.dto.ResponseDTO;
import com.anc.surveyservice.dto.SurveyDetailsDTO;
import com.anc.surveyservice.service.SurveyService;
import com.anc.surveyservice.util.Constants;

@RestController
@RequestMapping("/api/admin")
public class SurveyController {

	private static final Logger logger = LoggerFactory.getLogger(SurveyController.class);

	@Autowired
	private SurveyService surveyService;

	/**
	 * Method to create a Survey
	 * 
	 * @param surveyDetailsDTO
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@PostMapping(value = "/survey")
	ResponseEntity<ResponseDTO<String>> createSurvey(@RequestBody SurveyDetailsDTO surveyDetailsDTO)
			throws ServletException, IOException {
		logger.info("Start : Survey Controller - Create Survey");
		ResponseEntity<ResponseDTO<String>> responseEntity = null;
		String status = surveyService.createSurvey(surveyDetailsDTO);
		if (Constants.SUCCESS.equals(status)) {
			logger.info(Constants.CREATE_SURVEY_SUCCESS);
			ResponseDTO<String> response = new ResponseDTO<String>(Constants.SUCCESS, Constants.STATUS_CODE_200,
					Constants.CREATE_SURVEY_SUCCESS);
			response.setData(status);
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		} else if (Constants.DUPLICATE.equals(status)) {
			logger.error(Constants.CREATE_SURVEY_DUPLICATE);
			ResponseDTO<String> response = new ResponseDTO<String>(Constants.FAILED, Constants.STATUS_CODE_406,
					Constants.CREATE_SURVEY_DUPLICATE);
			responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			logger.error(Constants.CREATE_SURVEY_FAILED);
			ResponseDTO<String> response = new ResponseDTO<String>(Constants.FAILED, Constants.STATUS_CODE_500,
					Constants.CREATE_SURVEY_FAILED);
			responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("End : Survey Controller - Create Survey");
		return responseEntity;
	}


	/**
	 * Method to get all the Surveys
	 * 
	 * @param searchKey
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@GetMapping(value = "/survey")
	ResponseEntity<ResponseDTO<List<SurveyDetailsDTO>>> getSurveys(@RequestParam(required = false) String searchKey,
			@RequestParam int limit, @RequestParam int offset, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder) throws ServletException, IOException {
		logger.info("Start : Survey Controller - Get Survey");
		ResponseEntity<ResponseDTO<List<SurveyDetailsDTO>>> responseEntity = null;
		List<SurveyDetailsDTO> surveys = surveyService.getSurveys(searchKey, limit, offset, sortBy, sortOrder);
		if (surveys != null) {
			logger.info(Constants.FETCH_SURVEY_SUCCESS);
			ResponseDTO<List<SurveyDetailsDTO>> response = new ResponseDTO<List<SurveyDetailsDTO>>(Constants.SUCCESS,
					Constants.STATUS_CODE_200, Constants.FETCH_SURVEY_SUCCESS);
			response.setData(surveys);
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			logger.error(Constants.FETCH_SURVEY_FAILED);
			ResponseDTO<List<SurveyDetailsDTO>> response = new ResponseDTO<List<SurveyDetailsDTO>>(Constants.FAILED,
					Constants.STATUS_CODE_500, Constants.FETCH_SURVEY_FAILED);
			responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("End : Survey Controller - Get Survey");
		return responseEntity;
	}
	
	/**
	 * Method to add a new question corresponding to the Survey
	 * 
	 * @param questionDTO
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@PostMapping(value = "/survey/question")
	ResponseEntity<ResponseDTO<String>> addQuestion(@RequestBody QuestionDTO questionDTO)
			throws ServletException, IOException {
		logger.info("Start : Survey Controller - add Question");
		ResponseEntity<ResponseDTO<String>> responseEntity = null;
		String status = surveyService.addQuestion(questionDTO);
		if (Constants.SUCCESS.equals(status)) {
			logger.info(Constants.CREATE_QUESTION_SUCCESS);
			ResponseDTO<String> response = new ResponseDTO<String>(Constants.SUCCESS, Constants.STATUS_CODE_200,
					Constants.CREATE_QUESTION_SUCCESS);
			response.setData(status);
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			logger.error(Constants.CREATE_QUESTION_FAILED);
			ResponseDTO<String> response = new ResponseDTO<String>(Constants.FAILED, Constants.STATUS_CODE_500,
					Constants.CREATE_QUESTION_FAILED);
			responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("End : Survey Controller - add Question");
		return responseEntity;
	}
	
	/**
	 * Method to fetch questions corresponding to a Survey
	 * 
	 * @param searchKey
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@GetMapping(value = "/survey/question")
	ResponseEntity<ResponseDTO<List<QuestionDTO>>> getQuestions(@RequestParam Long surveyId, @RequestParam(required = false) String searchKey,
			@RequestParam(required = false) int limit, @RequestParam(required = false) int offset, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder) throws ServletException, IOException {
		logger.info("Start : Survey Controller - Get questions");
		ResponseEntity<ResponseDTO<List<QuestionDTO>>> responseEntity = null;
		List<QuestionDTO> surveys = surveyService.getQuestions(surveyId, searchKey, limit, offset, sortBy, sortOrder);
		if (surveys != null) {
			logger.info(Constants.FETCH_QUESTION_SUCCESS);
			ResponseDTO<List<QuestionDTO>> response = new ResponseDTO<List<QuestionDTO>>(Constants.SUCCESS,
					Constants.STATUS_CODE_200, Constants.FETCH_QUESTION_SUCCESS);
			response.setData(surveys);
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			logger.error(Constants.FETCH_QUESTION_FAILED);
			ResponseDTO<List<QuestionDTO>> response = new ResponseDTO<List<QuestionDTO>>(Constants.FAILED,
					Constants.STATUS_CODE_500, Constants.FETCH_QUESTION_FAILED);
			responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("End : Survey Controller - Get questions");
		return responseEntity;
	}
}
