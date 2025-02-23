package com.bigfive.personality_test.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bigfive.personality_test.DTO.InputAdmin;
import com.bigfive.personality_test.DTO.QuestionWithUserInfo;
import com.bigfive.personality_test.Server.AdminService;
import com.bigfive.personality_test.Server.QuestionService;
import com.bigfive.personality_test.entities.Question;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5500") 
public class AdminController {
    
    @Autowired
    private QuestionService questions;
    @Autowired
    private AdminService admins;

    private final Map<String, String[]> personalitiesWithSubcategories = new LinkedHashMap<>() {{
        put("openness", new String[]{"imagination", "artistic_interests", "emotional_depth", "adventurousness", "intellectual_curiosity", "openness_to_values"});
        put("conscientiousness", new String[]{"self_discipline", "orderliness", "responsibility", "achievement_striving", "cautiousness", "time_management", "attention_to_detail"});
        put("extraversion", new String[]{"sociability", "assertiveness", "energy_level", "adventurousness", "optimism"});
        put("agreeableness", new String[]{"trust", "morality", "altruism", "cooperation", "modesty", "sympathy"});
        put("neuroticism", new String[]{"anxiety", "anger", "depression", "self_consciousness", "vulnerability", "stress_tolerance"});
    }};
        

     @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody InputAdmin requestBody){
        Boolean getResponse =admins.checkAdmin(requestBody);
        if(!getResponse){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin/PassWord is wrong/Dont exist.");
        }
        return  ResponseEntity.ok(getResponse);
    }

    @PostMapping("/getAll")
    public ResponseEntity<List<Question>> getAllQuestions(@RequestBody InputAdmin requestBody){
        Boolean getResponse =admins.checkAdmin(requestBody);
        if(!getResponse){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Havent Validate,please login.");
        }else{
            try{
                return ResponseEntity.ok(admins.getCurrentQuestions());
            }
            catch(Exception e){
               throw new RuntimeException();
            }
            
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody int id) {
        try {
            admins.deleteQuestion(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }
    

    @PostMapping("/add")
    public ResponseEntity<Boolean> add(@RequestBody QuestionWithUserInfo question){
        for(Question question1 : question.getQuestion()){
            String category = question1.getCategory().toLowerCase();
            String subcategory = question1.getSubcategory().toLowerCase();
    
            // 检查 category 是否在 Map 中
            if (!personalitiesWithSubcategories.containsKey(category)) {
                throw new RuntimeException("illegal property: " + category);
            }
    
            // 检查 subcategory 是否属于 category
            String[] validSubcategories = personalitiesWithSubcategories.get(category);
            boolean isValidSubcategory = false;
            for (String validSub : validSubcategories) {
                if (validSub.equals(subcategory)) {
                    isValidSubcategory = true;
                    break;
                }
            }
    
            if (!isValidSubcategory) {
                throw new RuntimeException("illegal property: " + category + " - " + subcategory);
            }
        }
    
        InputAdmin admin = new InputAdmin(question.getUserName(), question.getPassWord());
        Boolean getResponse = admins.checkAdmin(admin);
        if (!getResponse) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Havent Validate,please login.");
        }
    
        List<Question> currentQuestions = new ArrayList<>();
        try {
            currentQuestions = admins.getCurrentQuestions();
        } catch (Exception e) {
            throw new RuntimeException("adding failed");
        }
    
        for (Question question2 : currentQuestions) {
            for (Question question3 : question.getQuestion()) {
                if (question2.getContent().equals(question3.getContent())) {
                    return ResponseEntity.ok(false);
                }
            }
        }
    
        try {
            admins.addQuestion(question.getQuestion());
        } catch (Exception e) {
            new RuntimeException();
        }
        return ResponseEntity.ok(true);
    }
    

}
