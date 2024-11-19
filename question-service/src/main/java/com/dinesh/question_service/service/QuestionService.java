package com.dinesh.question_service.service;



import com.dinesh.question_service.model.Question;
import com.dinesh.question_service.model.QuestionWrapper;
import com.dinesh.question_service.model.Response;
import com.dinesh.question_service.repo.QuestionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);
    @Autowired
    private QuestionRepo questionRepo;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionRepo.findAll(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {

            List<Question> categoryQuestions = questionRepo.findByCategory(category);
            if(!categoryQuestions.isEmpty()){
                return new ResponseEntity<>(categoryQuestions, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
            }
        }

    public ResponseEntity<String> addQuestion(Question question) {
        Question savedQuestion = questionRepo.save(question);
        if(savedQuestion.getId() != 0){
            return new ResponseEntity<>("success", HttpStatus.CREATED);

        }else {
            return new ResponseEntity<>("not saved", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Integer>> getQuestionForQuiz(String categoryName, int numOfQuestions) {

        List<Integer> questionsForQuiz = questionRepo.findRandomQuestionsByCategory(categoryName, numOfQuestions);
        return new ResponseEntity<>(questionsForQuiz, HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        for(Integer id: questionIds){
            questions.add(questionRepo.findById(id).get());
        }

        for(Question question: questions){

            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());

            wrappers.add(wrapper);
        }
        return new ResponseEntity<>(wrappers, HttpStatus.OK);

    }

    public ResponseEntity<Integer> getScoreFromResponse(List<Response> responses) {
        int count =0;
        for(Response response: responses){
            Question question = questionRepo.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer())){
                count++;
            }
        }
        return new ResponseEntity<>(count, HttpStatus.ACCEPTED);

    }
}
