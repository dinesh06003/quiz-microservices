package com.dinesh.question_service.controller;




import com.dinesh.question_service.model.Question;
import com.dinesh.question_service.model.QuestionWrapper;
import com.dinesh.question_service.model.Response;
import com.dinesh.question_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {


    @Autowired
    Environment environment;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/all-questions")
    public ResponseEntity<List<Question>> getAllQuestion(){
        return questionService.getAllQuestions();
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category){
        return questionService.getQuestionsByCategory(category);
    }


    @PostMapping("add-question")
    public ResponseEntity<String> addQuestion(@RequestBody Question question){

        return questionService.addQuestion(question);
    }

    @GetMapping("generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String categoryName, @RequestParam int numOfQuestions){
        return questionService.getQuestionForQuiz(categoryName, numOfQuestions);
    }


    @PostMapping("get-questions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds){
        environment.getProperty("local.server.port");
        return questionService.getQuestionsFromId(questionIds);
    }

    @PostMapping("get-score")
    public ResponseEntity<Integer> getScoreFromResponse(@RequestBody List<Response> responses){
        return questionService.getScoreFromResponse(responses);
    }
}
