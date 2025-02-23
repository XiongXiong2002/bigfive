package com.bigfive.personality_test.Server;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.bigfive.personality_test.DTO.PersonalityScore;
import com.bigfive.personality_test.DTO.SimpleResult;
import com.bigfive.personality_test.Repository.QuestionRepository;
import com.bigfive.personality_test.entities.Question;



@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AiResult aiResult;
    private final Map<String, String[]> personalitiesWithSubcategories = new LinkedHashMap<>() {{
        put("openness", new String[]{"imagination", "artistic_interests", "emotional_depth", "adventurousness", "intellectual_curiosity", "openness_to_values"});
        put("conscientiousness", new String[]{"self_discipline", "orderliness", "responsibility", "achievement_striving", "cautiousness", "time_management", "attention_to_detail"});
        put("extraversion", new String[]{"sociability", "assertiveness", "energy_level", "adventurousness", "optimism"});
        put("agreeableness", new String[]{"trust", "morality", "altruism", "cooperation", "modesty", "sympathy"});
        put("neuroticism", new String[]{"anxiety", "anger", "depression", "self_consciousness", "vulnerability", "stress_tolerance"});
    }};
    


    @Transactional(readOnly = true)
    public List<Question> getAllQuestions(String state) {
        // 校验 state 参数
        if (!"standardTest".equals(state) && !"quickTest".equals(state)) {
            throw new IllegalArgumentException("Invalid state: " + state + ". Allowed values are 'q' or 's'.");
        }
    
        List<Question> questions = new ArrayList<>();
        int question =0;
        
        if("quickTest".equals(state)){
            for (Map.Entry<String, String[]> entry : personalitiesWithSubcategories.entrySet()) {
                String personality = entry.getKey();  // 获取主类别 (Openness, Conscientiousness 等)
                questions.addAll(questionRepository.findQuestionsByCategory(personality,5));
                
            }            
        }else{
            for (Map.Entry<String, String[]> entry : personalitiesWithSubcategories.entrySet()) {
                String personality = entry.getKey();  // 获取主类别 (Openness, Conscientiousness 等)
                
                String[] subcategories = entry.getValue(); // 获取对应的子维度

                for (String subcategory : subcategories) { 
                    questions.addAll(questionRepository.findQuestionsBySubCategory(personality, subcategory));

                }

                question=0;
                
            }            
        }

    
        return questions;
    }

    @Transactional(readOnly = true)
    public List<SimpleResult> setSimpleResult(List<PersonalityScore> score){
        if (score == null || score.isEmpty()) {
            return null;
        }


        List <SimpleResult> results = new ArrayList<>();

        // 遍历列表，计算每个 PersonalityScore 的总分，并找出给予评价
        for (PersonalityScore personalityScore : score) {
            if (personalityScore == null) continue;
            int totalScore = personalityScore.calculateTotalScore();
             
            //获得当前level 
            String level = getLevel(totalScore);

            results.add(new SimpleResult(personalityScore.getPersonality(), questionRepository.findSimpleResult(personalityScore.getPersonality(), level).getComment()));
       
        }

        return results;
    }

    public String setComplexResult(List<PersonalityScore> score) {
        if (score == null || score.isEmpty()) {
            return "未提供评分数据，无法生成分析结果。";
        }
    
        // 使用 StringBuilder 进行高效字符串拼接
        StringBuilder builder = new StringBuilder();
        builder.append("你是一个大五人格分析专家，接下来你要通过我上传的信息来进行大五人格分析\n");
    
        for (PersonalityScore personality : score) {
            builder.append("主维度 ：").append(personality.getPersonality()).append("\n");
    
            for (Map.Entry<String, Integer> entry : personality.getSubcategories().entrySet()) {
                builder.append("子维度 ：").append(entry.getKey())
                       .append(" 得分 :").append(entry.getValue()).append("\n");
            }
        }
    
        builder.append("满分为300，根据每个维度的得分以及总分得出该用户人格评价josn文件,格式如下{\n" + //
                        "  \"traits\": [\n" + //
                        "    {\n" + //
                        "      \"trait\": \"\",  // 人格特质名称，如 \"Openness\"\n" + //
                        "      \"total_score\": 0,  // 该特质的总得分,这个分数是所有子维度得分的总和\n" + //
                        "      \"Max_score\": 0,  // 该特质的能得到的最高分\n" + //
                        "      \"details\": [\n" + //
                        "        {\n" + //
                        "          \"aspect\": \"\",  // 子维度名称，如 \"Imagination\"\n" + //
                        "          \"score\": 0,  // 子维度得分\n" + //
                        "          \"description\": \"\"  // 该子维度的描述\n" + //
                        "        }\n" + //
                        "      ]\n" + //
                        "    }\n" + //
                        "  ],\n" + //
                        "  \"summary\": \"\"  // 总结整体人格特质\n" + //
                        "}并转化为英文,注意，Openness: 子维度 6, 总分最高 120, Conscientiousness: 子维度 7, 总分最高 140, Extraversion: 子维度 5, 总分最高 100, Agreeableness: 子维度 6, 总分最高 120, Neuroticism: 子维度 6, 总分最高 120, 且语气要温和的面向客户。比如您在xxx的得分较高，说明您xxxx，在josn的最后加上“总结”属性，总结大体性格\n，注意所有结果都要转换成英文,所有结果都需要是英文！！！"+"\"本次请求时间：\""+ new Date().toString());
    
        return aiResult.getAIResponse(builder.toString()); // 发送给 AI 进行分析
    }
    


    //测定当前人格等级
    private String getLevel(int score){
        String level = null;
        if(score<8){
        level="low";
        }else if(score<13){
        level ="medium";
        }else if (score<=25){
        level ="high";
        }
        return  level;
    }
}
