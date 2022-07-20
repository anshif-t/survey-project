package com.anc.surveyservice.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "survey_details")
public class SurveyDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

//	@OneToMany(mappedBy = "survey", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
//	private List<Question> questions;
	
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "last_modified_date")
	private LocalDateTime lastModifiedDate;

}
