package com.dinesh.quiz_service.service;



import com.dinesh.quiz_service.feign.QuizInterface;
import com.dinesh.quiz_service.model.*;
import com.dinesh.quiz_service.repo.QuizRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepo quizRepo;

//    @Autowired
//    private QuestionRepo questionRepo;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(QuizDto quizDto) {

        List<Integer> questions =quizInterface.getQuestionsForQuiz(quizDto.getCategory(), quizDto.getNumQ()).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDto.getTitle());
        quiz.setQuestionIds(questions);
        quizRepo.save(quiz);

        return new ResponseEntity<>("Successfully created quiz question ids", HttpStatus.CREATED);



    }


    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {
        Optional<Quiz> quiz = quizRepo.findById(id);
        List<Integer> quizQuestionIds = quiz.get().getQuestionIds();

        List<QuestionWrapper> questionsForUser = quizInterface.getQuestionsFromId(quizQuestionIds).getBody();

        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);

    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        ResponseEntity<Integer> count;
        count = quizInterface.getScoreFromResponse(responses);

        return count;
    }
}
