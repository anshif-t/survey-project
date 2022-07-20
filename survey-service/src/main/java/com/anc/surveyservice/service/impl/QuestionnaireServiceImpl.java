package com.anc.surveyservice.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.anc.surveyservice.dto.QuestionAnswerDTO;
import com.anc.surveyservice.dto.QuestionDTO;
import com.anc.surveyservice.dto.QuestionnaireDTO;
import com.anc.surveyservice.dto.SurveyDetailsDTO;
import com.anc.surveyservice.entity.Answer;
import com.anc.surveyservice.entity.Question;
import com.anc.surveyservice.entity.QuestionAnswer;
import com.anc.surveyservice.entity.Questionnaire;
import com.anc.surveyservice.entity.SurveyDetails;
import com.anc.surveyservice.repository.AnswerRepository;
import com.anc.surveyservice.repository.QuestionAnswerRepository;
import com.anc.surveyservice.repository.QuestionRepository;
import com.anc.surveyservice.repository.QuestionnaireRepository;
import com.anc.surveyservice.repository.SurveyDetailsRepository;
import com.anc.surveyservice.service.QuestionnaireService;
import com.anc.surveyservice.util.Constants;
import com.anc.surveyservice.util.ModelMapperUtil;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

	private static final Logger logger = LoggerFactory.getLogger(QuestionnaireServiceImpl.class);

	@Autowired
	private QuestionnaireRepository questionnaireRepository;

	@Autowired
	private SurveyDetailsRepository surveyDetailsRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionAnswerRepository questionAnswerRepository;

	@Override
	@Transactional
	public String saveQuestionnaire(QuestionnaireDTO questionnaireDTO) {
		String status = Constants.SUCCESS;
		try {
			if (questionnaireDTO.getId() != null) {
				updateQuestionnaire(questionnaireDTO);
			} else {
				createQuestionnaire(questionnaireDTO);
			}
		} catch (Exception ex) {
			logger.error("Failed to save the Questionnaire", ex);
			status = Constants.FAILED;
		}
		return status;
	}

	/**
	 * @param questionnaireDTO
	 */
	private void createQuestionnaire(QuestionnaireDTO questionnaireDTO) {
		Questionnaire questionnaire = ModelMapperUtil.map(questionnaireDTO, Questionnaire.class);
		questionnaire.setQuestionAnswer(getQuestionAnswers(questionnaireDTO, questionnaire));
		Optional<SurveyDetails> surveyOp = surveyDetailsRepository.findById(questionnaireDTO.getSurveyId());
		if (surveyOp.isPresent()) {
			SurveyDetails survey = surveyOp.get();
			questionnaire.setSurveyDetails(survey);
			questionnaire.setCreatedDate(LocalDateTime.now());
			questionnaire.setLastModifiedDate(LocalDateTime.now());
			questionnaireRepository.save(questionnaire);
			logger.info("Successfully saved the questionnaire.");
		} else {
			logger.error("No survey with survey Id : {}", questionnaireDTO.getSurveyId());
			throw new RuntimeException("Invalid Survey ID");
		}
	}

	/**
	 * @param questionnaireDTO
	 */
	private void updateQuestionnaire(QuestionnaireDTO questionnaireDTO) {
		Optional<Questionnaire> questionnaireOp = questionnaireRepository.findById(questionnaireDTO.getId());
		if (questionnaireOp.isPresent()) {
			Questionnaire qr = questionnaireOp.get();
			qr.setLastModifiedDate(LocalDateTime.now());
			qr.setStatus(questionnaireDTO.getStatus());
			questionAnswerRepository.deleteByQuestionnaire(qr.getId());
			qr.setQuestionAnswer(getQuestionAnswers(questionnaireDTO, qr));
			questionnaireRepository.save(qr);
		} else {
			logger.info("Invalid questionnaire with ID : {}", questionnaireDTO.getId());
			throw new RuntimeException("Invalid questionnaire ID");
		}
	}

	/**
	 * Method to create QuestionAnswer entity list for a Questionnaire
	 * 
	 * @param questionnaireDTO
	 * @param qr
	 * @return
	 */
	private List<QuestionAnswer> getQuestionAnswers(QuestionnaireDTO questionnaireDTO, Questionnaire qr) {
		List<QuestionAnswer> qaList = null;
		if (!CollectionUtils.isEmpty(questionnaireDTO.getQuestionAnswer())) {
			qaList = new ArrayList<>();
			QuestionAnswer newQa = null;
			for (QuestionAnswerDTO qa : questionnaireDTO.getQuestionAnswer()) {
				newQa = new QuestionAnswer();
				Optional<Question> qnOp = questionRepository.findById(qa.getQuestionId());
				if (qnOp.isPresent()) {
					Question qn = qnOp.get();
					newQa.setQuestion(qn);
					Optional<Answer> anOp = answerRepository.findById(qa.getAnswerId());
					if (anOp.isPresent()) {
						newQa.setAnswer(anOp.get());
					} else {
						logger.info("Invalid Answer with ID : {}", qa.getAnswerId());
						throw new RuntimeException("Invalid Answer ID");
					}
				} else {
					logger.info("Invalid question with ID : {}", qa.getQuestionId());
					throw new RuntimeException("Invalid Question ID");
				}
				newQa.setQuestionnaire(qr);
				qaList.add(newQa);
			}
		}
		return qaList;
	}

	@Override
	@Transactional
	public QuestionnaireDTO getQuestionnaire(String username, Long surveyId) {
		QuestionnaireDTO questionnaireDTO = null;
		try {
			Optional<SurveyDetails> surveyOp = surveyDetailsRepository.findById(surveyId);
			if (surveyOp.isPresent()) {
				SurveyDetails survey = surveyOp.get();
				if (username == null || username.isEmpty()) {
					logger.info("Anonymous user login with survey Id : {}", surveyId);
				} else {
					Questionnaire questionnaire = questionnaireRepository.findByUsernameAndSurveyDetails(username,
							survey);
					if (questionnaire == null) {
						logger.info("Creating new questionnaire for the user : {}", username);
						questionnaireDTO = getNewQuestionnaire(username, survey);
					} else {
						logger.info("{} Questionnaire found for user : {}", questionnaire.getId(), username);
						if (Constants.STATUS_COMPLETED.equals(questionnaire.getStatus())) {
							questionnaireDTO = new QuestionnaireDTO();
							questionnaireDTO.setSurveyDetails(ModelMapperUtil.map(survey, SurveyDetailsDTO.class));
							questionnaireDTO.setStatus(Constants.STATUS_COMPLETED);
							logger.info("{} Questionnaire is already completed", questionnaire.getId(), username);
						} else {
							questionnaireDTO = getExistingQuestionnaire(questionnaire, survey);
						}
					}
				}
			} else {
				logger.error("Invalid survey Id : {}", surveyId);
			}
		} catch (Exception ex) {
			logger.error("Failed to get the Questionnaire", ex);
		}
		return questionnaireDTO;
	}

	/**
	 * @param questionnaire
	 * @param survey
	 * @return
	 */
	private QuestionnaireDTO getExistingQuestionnaire(Questionnaire questionnaire, SurveyDetails survey) {
		QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO();
		questionnaireDTO.setId(questionnaire.getId());
		questionnaireDTO.setUsername(questionnaire.getUsername());
		questionnaireDTO.setCreatedDate(questionnaire.getCreatedDate());
		questionnaireDTO.setLastModifiedDate(questionnaire.getLastModifiedDate());
		questionnaireDTO.setStatus(questionnaire.getStatus());
		SurveyDetailsDTO surveyDTO = ModelMapperUtil.map(survey, SurveyDetailsDTO.class);
		List<Question> questions = questionRepository.findBySurvey(survey);
		if (!CollectionUtils.isEmpty(questions)) {
			List<QuestionDTO> qList = ModelMapperUtil.mapAll(questions, QuestionDTO.class);
			qList.stream().forEach(q -> {
				q.setSurveyId(q.getSurvey().getId());
				q.setSurvey(null);
				if (!CollectionUtils.isEmpty(q.getOptions())) {
					q.getOptions().forEach(o -> {
						o.setQuesionId(o.getQuestion().getId());
						o.setQuestion(null);
					});
				}
			});
			surveyDTO.setQuestions(qList);
		}
		questionnaireDTO.setSurveyDetails(surveyDTO);
		List<QuestionAnswerDTO> qaList = null;
		if (!CollectionUtils.isEmpty(questionnaire.getQuestionAnswer())) {
			qaList = ModelMapperUtil.mapAll(questionnaire.getQuestionAnswer(), QuestionAnswerDTO.class);
			qaList.forEach(qa -> {
				if (qa.getQuestion() != null)
					qa.setQuestionId(qa.getQuestion().getId());
				if (qa.getAnswer() != null)
					qa.setAnswerId(qa.getAnswer().getId());
				qa.setQuestion(null);
				qa.setAnswer(null);
				qa.setQuestionnaire(null);
			});
		} else {
			qaList = new ArrayList<>();
		}
		questionnaireDTO.setQuestionAnswer(qaList);
		questionnaireDTO.setSurveyId(survey.getId());
		return questionnaireDTO;
	}

	/**
	 * @param username
	 * @param survey
	 * @return
	 */
	private QuestionnaireDTO getNewQuestionnaire(String username, SurveyDetails survey) {
		QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO();
		questionnaireDTO.setUsername(username);
		SurveyDetailsDTO surveyDTO = ModelMapperUtil.map(survey, SurveyDetailsDTO.class);
		List<Question> questions = questionRepository.findBySurvey(survey);
		if (!CollectionUtils.isEmpty(questions)) {
			List<QuestionDTO> qList = ModelMapperUtil.mapAll(questions, QuestionDTO.class);
			qList.stream().forEach(q -> {
				q.setSurveyId(q.getSurvey().getId());
				q.setSurvey(null);
				if (!CollectionUtils.isEmpty(q.getOptions())) {
					q.getOptions().forEach(o -> {
						o.setQuesionId(o.getQuestion().getId());
						o.setQuestion(null);
					});
				}
			});
			surveyDTO.setQuestions(qList);
		}
		questionnaireDTO.setSurveyDetails(surveyDTO);
		questionnaireDTO.setQuestionAnswer(new ArrayList<QuestionAnswerDTO>());
		questionnaireDTO.setSurveyId(survey.getId());
		return questionnaireDTO;
	}

}
