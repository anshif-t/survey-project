package com.anc.surveyservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anc.surveyservice.dto.AnswerDTO;
import com.anc.surveyservice.dto.QuestionDTO;
import com.anc.surveyservice.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long>, JpaSpecificationExecutor<Answer> {

	List<AnswerDTO> findByQuestion(QuestionDTO q);

}
