package com.anc.surveyservice.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "questionnaire")
public class Questionnaire {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name")
	private String username;

	@Column(name = "status")
	private String status;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "survey_id", referencedColumnName = "id")
	private SurveyDetails surveyDetails;

	// @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade =
	// CascadeType.PERSIST)
	// @JoinColumn(name = "questionnaire_id")
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "questionnaire", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	private List<QuestionAnswer> questionAnswer;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "last_modified_date")
	private LocalDateTime lastModifiedDate;

}
