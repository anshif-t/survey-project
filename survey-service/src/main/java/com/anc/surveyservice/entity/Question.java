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
@Table(name = "question")
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "question_label")
	private String questionLabel;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "question", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	private List<Answer> options;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "survey_id", referencedColumnName = "id")
	private SurveyDetails survey;
	
	@Column(name = "created_date")
	private LocalDateTime createdDate;


}
