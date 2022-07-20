package com.anc.surveyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anc.surveyservice.entity.Questionnaire;
import com.anc.surveyservice.entity.SurveyDetails;

public interface QuestionnaireRepository
		extends JpaRepository<Questionnaire, Long>, JpaSpecificationExecutor<Questionnaire> {

	Questionnaire findByUsernameAndSurveyDetails(String username, SurveyDetails surveyDetails);

}
