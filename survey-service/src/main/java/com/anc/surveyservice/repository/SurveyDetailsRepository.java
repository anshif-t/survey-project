package com.anc.surveyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anc.surveyservice.entity.SurveyDetails;

public interface SurveyDetailsRepository extends JpaRepository<SurveyDetails, Long>,  JpaSpecificationExecutor<SurveyDetails> {

	SurveyDetails findByName(String name);

}
