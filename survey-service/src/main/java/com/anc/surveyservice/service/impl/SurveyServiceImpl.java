package com.anc.surveyservice.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.anc.surveyservice.dto.QuestionDTO;
import com.anc.surveyservice.dto.SurveyDetailsDTO;
import com.anc.surveyservice.entity.Answer;
import com.anc.surveyservice.entity.Question;
import com.anc.surveyservice.entity.SurveyDetails;
import com.anc.surveyservice.repository.QuestionRepository;
import com.anc.surveyservice.repository.SurveyDetailsRepository;
import com.anc.surveyservice.service.SurveyService;
import com.anc.surveyservice.util.Constants;
import com.anc.surveyservice.util.ModelMapperUtil;

@Service
public class SurveyServiceImpl implements SurveyService {

	private static final Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

	@Autowired
	private SurveyDetailsRepository surveyDetailsRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Override
	public String createSurvey(SurveyDetailsDTO surveyDetailsDTO) {
		String status = Constants.SUCCESS;
		try {
			SurveyDetails existingSurvey = surveyDetailsRepository.findByName(surveyDetailsDTO.getName());
			if (existingSurvey == null) {
				logger.info("No duplicate survey found : {}", surveyDetailsDTO.getName());
				SurveyDetails surveyDetails = ModelMapperUtil.map(surveyDetailsDTO, SurveyDetails.class);
				surveyDetails.setCreatedDate(LocalDateTime.now());
				surveyDetails.setLastModifiedDate(LocalDateTime.now());
				surveyDetailsRepository.save(surveyDetails);
				logger.info("Successfully saved the survey : {}", surveyDetailsDTO.getName());
			} else {
				logger.error("Duplicate survey found : {}", surveyDetailsDTO.getName());
				status = Constants.DUPLICATE;
			}
		} catch (Exception ex) {
			logger.error("Failed to save the survey", ex);
			status = Constants.FAILED;
		}
		return status;
	}

	@Override
	public List<SurveyDetailsDTO> getSurveys(String searchKey, int limit, int offset, String sortBy, String sortOrder) {
		List<SurveyDetailsDTO> resultList = null;
		try {
			Specification<SurveyDetails> surveySpec = createSurveySpecification(searchKey);
			Pageable pageable = getPagination(offset, limit, sortBy, sortOrder);
			List<SurveyDetails> surveys = surveyDetailsRepository.findAll(surveySpec, pageable).toList();
			if (CollectionUtils.isEmpty(surveys)) {
				logger.info("No survey found for search key: {}", searchKey);
				resultList = new ArrayList<>();
			} else {
				resultList = ModelMapperUtil.mapAll(surveys, SurveyDetailsDTO.class);
			}
		} catch (Exception ex) {
			logger.error("Failed to fetch the survey", ex);
		}
		return resultList;
	}

	@Override
	@Transactional
	public String addQuestion(QuestionDTO questionDTO) {
		String status = Constants.SUCCESS;
		try {
			Optional<SurveyDetails> surveyOptional = surveyDetailsRepository.findById(questionDTO.getSurveyId());
			if (surveyOptional.isPresent()) {
				Question question = ModelMapperUtil.map(questionDTO, Question.class);
				question.setSurvey(surveyOptional.get());
				if (!CollectionUtils.isEmpty(question.getOptions())) {
					for (Answer o : question.getOptions()) {
						o.setQuestion(question);
					}
				}
				question.setCreatedDate(LocalDateTime.now());
				question = questionRepository.save(question);
				logger.info("Successfully added the question.");
			} else {
				logger.error("Mapped Survey ID : {} is wrong for the question", questionDTO.getSurveyId());
				status = Constants.FAILED;
			}
		} catch (Exception ex) {
			logger.error("Failed to add the question", ex);
			status = Constants.FAILED;
		}
		return status;
	}

	@Override
	public List<QuestionDTO> getQuestions(Long survayId, String searchKey, int limit, int offset, String sortBy,
			String sortOrder) {
		List<QuestionDTO> resultList = null;
		try {
			Specification<Question> questionSpec = createQuestionNotification(survayId, searchKey);
			Pageable pageable = getPagination(offset, limit, sortBy, sortOrder);
			List<Question> questions = questionRepository.findAll(questionSpec, pageable).toList();
			if (CollectionUtils.isEmpty(questions)) {
				logger.info("No Questions found for survey id: {} and searchKey: {}", survayId, searchKey);
				resultList = new ArrayList<>();
			} else {
				resultList = ModelMapperUtil.mapAll(questions, QuestionDTO.class);
				resultList.stream().forEach(q -> {
					q.setSurveyId(q.getSurvey().getId());
					q.setSurvey(null);
					if (!CollectionUtils.isEmpty(q.getOptions())) {
						q.getOptions().forEach(o -> {
							o.setQuesionId(o.getQuestion().getId());
							o.setQuestion(null);
						});
					}
				});
				logger.info("Populated Questions for survey id: {} and searchKey: {}", survayId, searchKey);
			}
		} catch (Exception ex) {
			logger.error("Failed to fetch the Questions", ex);
		}
		return resultList;
	}

	/**
	 * @param surveyId
	 * @param searchKey
	 * @return
	 */
	private Specification<Question> createQuestionNotification(Long surveyId, String searchKey) {
		Specification<Question> qnSpec = (qn, query, cb) -> {
			return cb.equal(qn.get("survey"), surveyId);
		};
		if (searchKey != null && searchKey.length() > 0) {
			Specification<Question> searchKeySpec = (qn, query, cb) -> {
				return cb.like(qn.get("questionLabel"), "%" + searchKey + "%");
			};
			qnSpec = qnSpec.and(searchKeySpec);
		}
		return qnSpec;
	}

	/**
	 * @param searchKey
	 * @return
	 */
	private Specification<SurveyDetails> createSurveySpecification(String searchKey) {
		Specification<SurveyDetails> surveySpec = null;
		if (searchKey != null && searchKey.length() > 0) {
			surveySpec = (survey, query, cb) -> {
				return cb.or(cb.like(survey.get("name"), "%" + searchKey + "%"),
						cb.like(survey.get("description"), "%" + searchKey + "%"));
			};
		}
		return surveySpec;
	}

	/**
	 * @param offset
	 * @param limit
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	private Pageable getPagination(Integer offset, Integer limit, String sortBy, String sortOrder) {
		if(offset == null)offset = 0;
		if(limit == null || limit == 0)limit = Integer.MAX_VALUE;
		Sort.Direction direction = (sortOrder != null && Constants.SORT_ASCENDING.equals(sortOrder))
				? Sort.Direction.ASC
				: Sort.Direction.DESC;
		sortBy = (sortBy != null && sortBy.length() > 0) ? sortBy : "createdDate";
		return PageRequest.of(offset, limit, direction, sortBy);
	}

}
