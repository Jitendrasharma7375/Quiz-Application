package com.smartdocs.question_service.model;

import jakarta.persistence.*; // JPA annotations
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "questions") 
@Data
public class Question {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id; 

	@Column(name = "question_title") 
	private String questionTitle;


	@Column(name = "option1")
	private String option1;

	@Column(name = "option2")
	private String option2;

	@Column(name = "option3")
	private String option3;

	@Column(name = "option4")
	private String option4;

	@Column(name = "right_answer")
	private String rightAnswer;

	@Column(name = "difficulty_level")
	private String difficultyLevel;

	@Column(name = "category")
	private String category;
}
