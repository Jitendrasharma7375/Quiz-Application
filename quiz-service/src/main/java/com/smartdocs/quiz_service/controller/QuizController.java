package com.smartdocs.quiz_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import com.smartdocs.quiz_service.service.QuizService;
import com.smartdocs.quiz_service.model.QuestionWrapper;
import com.smartdocs.quiz_service.model.QuizDto;
import com.smartdocs.quiz_service.model.Response;

@RestController
@RequestMapping("quiz")
public class QuizController {

    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizDto quizDto, HttpServletRequest request) {
        ResponseEntity<String> response = quizService.createQuiz(quizDto.getCategoryName(), quizDto.getNumQuestions(), quizDto.getTitle());
        return response;
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id, HttpServletRequest request) {
        ResponseEntity<List<QuestionWrapper>> response = quizService.getQuizQuestions(id);
        return response;
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Integer id, @RequestBody List<Response> responses, HttpServletRequest request) {
        ResponseEntity<Integer> response = quizService.calculateResult(id, responses);
        return response;
    }
}
