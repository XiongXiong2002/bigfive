package com.bigfive.personality_test.Server;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bigfive.personality_test.DTO.InputAdmin;
import com.bigfive.personality_test.Repository.AdminRepository;
import com.bigfive.personality_test.Repository.QuestionRepository;
import com.bigfive.personality_test.entities.AdminUser;
import com.bigfive.personality_test.entities.Question;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Autowired
    private QuestionRepository questionRepository;

    


    @Transactional(readOnly = true)
    public Boolean checkAdmin(InputAdmin admin) {
        if (admin == null || admin.getUserName() == null || admin.getPassWord() == null) {
            return false;
        }
    
        List<AdminUser> existAdmin = adminRepository.findByUsername(admin.getUserName());
    
        if (existAdmin.isEmpty()) {
            return false;
        }
    
        for (AdminUser user : existAdmin) {
            if (passwordEncoder.matches(admin.getPassWord(), user.getPassword())) {
                return true;
            }
        }
    
        return false;
    }
    

    @Transactional(readOnly = true)
    public List<Question> getCurrentQuestions(){
        return questionRepository.getAllQuestions();
    }

    @Transactional
    public void deleteQuestion(int id){
        questionRepository.deleteQuestionById(id);

    }

    @Transactional
    public void addQuestion(List<Question> questions){
        for(Question question :questions){
            questionRepository.save(question);
        }
        
    }
}
