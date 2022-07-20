package com.anc.surveyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.anc.surveyservice.entity.QuestionAnswer;

public interface QuestionAnswerRepository
		extends JpaRepository<QuestionAnswer, Long>, JpaSpecificationExecutor<QuestionAnswer> {

	@Modifying
	@Query(value = "delete from question_answer where questionnaire_id=?", nativeQuery = true)
	void deleteByQuestionnaire(Long id);

}
