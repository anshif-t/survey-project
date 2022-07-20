package com.anc.surveyservice.controller;

import java.io.IOException;

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

import com.anc.surveyservice.dto.QuestionnaireDTO;
import com.anc.surveyservice.dto.ResponseDTO;
import com.anc.surveyservice.service.QuestionnaireService;
import com.anc.surveyservice.util.Constants;

@RestController
@RequestMapping("/api/customer")
public class QuestionnaireController {

	private static final Logger logger = LoggerFactory.getLogger(QuestionnaireController.class);

	@Autowired
	private QuestionnaireService questionnaireService;

	/**
	 * Method to save a questionnaire
	 * 
	 * @param httpServletRequest
	 * @param questionnaireDTO
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@PostMapping(value = "/questionnaire")
	ResponseEntity<ResponseDTO<String>> saveQuestionnaire(@RequestBody QuestionnaireDTO questionnaireDTO) throws ServletException, IOException {
		logger.info("Start : Questionnaire Controller - Create Questionnaire");
		ResponseEntity<ResponseDTO<String>> responseEntity = null;
		String status = questionnaireService.saveQuestionnaire(questionnaireDTO);
		if (Constants.SUCCESS.equals(status)) {
			logger.info(Constants.CREATE_QUESTIONNAIRE_SUCCESS);
			ResponseDTO<String> response = new ResponseDTO<String>(Constants.SUCCESS, Constants.STATUS_CODE_200,
					Constants.CREATE_QUESTIONNAIRE_SUCCESS);
			response.setData(status);
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			logger.error(Constants.CREATE_QUESTIONNAIRE_FAILED);
			ResponseDTO<String> response = new ResponseDTO<String>(Constants.FAILED, Constants.STATUS_CODE_500,
					Constants.CREATE_QUESTIONNAIRE_FAILED);
			responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("End : Questionnaire Controller - Create Questionnaire");
		return responseEntity;
	}

	/**
	 *  Method to get the Questionnaire
	 *  
	 * @param surveyId
	 * @param anonymous
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@GetMapping(value = "/questionnaire")
	ResponseEntity<ResponseDTO<QuestionnaireDTO>> getQuestionnaire(@RequestParam Long surveyId, @RequestParam String username)
			throws ServletException, IOException {
		logger.info("Start : Questionnaire Controller - Get Questionnaire");
		ResponseEntity<ResponseDTO<QuestionnaireDTO>> responseEntity = null;
		QuestionnaireDTO surveys = questionnaireService.getQuestionnaire(username, surveyId);
		if (surveys != null) {
			logger.info(Constants.FETCH_QUESTIONNAIRE_SUCCESS);
			ResponseDTO<QuestionnaireDTO> response = new ResponseDTO<QuestionnaireDTO>(Constants.SUCCESS,
					Constants.STATUS_CODE_200, Constants.FETCH_QUESTIONNAIRE_SUCCESS);
			response.setData(surveys);
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			logger.error(Constants.FETCH_QUESTIONNAIRE_FAILED);
			ResponseDTO<QuestionnaireDTO> response = new ResponseDTO<QuestionnaireDTO>(Constants.FAILED,
					Constants.STATUS_CODE_500, Constants.FETCH_QUESTIONNAIRE_FAILED);
			responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("End : Questionnaire Controller - Get Questionnaire");
		return responseEntity;
	}

}
