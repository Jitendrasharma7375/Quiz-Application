package com.smartdocs.question_service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smartdocs.question_service.model.Question;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {

    // Find all questions by category
    List<Question> findByCategory(String category);

    // Custom query to get random questions by category
    @Query(value = "SELECT q.id FROM questions q WHERE q.category = :category ORDER BY RAND() LIMIT :numQuestion", nativeQuery = true)
    List<Integer> findRandomQuestionsByCategory(String category, Integer numQuestion);
}
