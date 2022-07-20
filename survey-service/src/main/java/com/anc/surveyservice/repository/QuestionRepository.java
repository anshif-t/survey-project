package com.anc.surveyservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anc.surveyservice.entity.Question;
import com.anc.surveyservice.entity.SurveyDetails;

public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {

	List<Question> findBySurvey(SurveyDetails survey);

}
