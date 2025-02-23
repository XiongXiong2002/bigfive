package com.bigfive.personality_test.Server;

import java.util.List;
import java.util.Map;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component  
public class AiResult {
    private static final String API_URL = "https://spark-api-open.xf-yun.com/v1/chat/completions";
    private static final String API_TOKEN = "Bearer cYWXvSWdLimlYmxPbPEu:eLVluOTeDoxCfWPpoPTU";  // ✅ 确保 API Token 以 Bearer 开头
    private final WebClient webClient;

    public AiResult() {
        this.webClient = WebClient.builder()
                .baseUrl(API_URL) 
                .defaultHeader("Authorization", API_TOKEN) // ✅ 修正 Token 认证格式
                .defaultHeader("Content-Type", "application/json") 
                .build();
    }



    private Mono<String> queryAI(String userMessage) {
        Map<String, Object> requestBody = Map.of(
                "model", "4.0Ultra",  // 确保 model 正确
                "messages", List.of(Map.of("role", "user", "content", userMessage)),
                "stream", false
        );
    
        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> this.extractAIResponse(response))
                .doOnError(WebClientResponseException.class, ex -> { // 打印详细错误信息
                    System.out.println("请求失败: " + ex);
                })
                .onErrorReturn("AI 服务请求失败，请检查 API 参数");
    }
    

    // 解析 AI 返回的 JSON，提取 "content"
    private String extractAIResponse(Map<String, Object> response) {
        if (response.containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (!choices.isEmpty() && choices.get(0).containsKey("message")) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                System.out.println(message.getOrDefault("content", "AI 没有返回有效信息").toString()+"喜喜");
                return message.getOrDefault("content", "AI 没有返回有效信息").toString();
            }
        }
        return "AI 没有返回有效信息";
    }

    public String getAIResponse(String userMessage) {
        String s = queryAI(userMessage).block();
        return s ;// ✅ 设置超时 30 秒
    }
    
    
    
    

    // 🔹 测试方法
    /*public static void main(String[] args) {
        AiResult aiResult = new AiResult();
        String response = aiResult.getAIResponse("满分为300，根据每个维度的得分以及总分得出该用户人格评价josn文件,并转化为英文,参照以下例子"+ "[\n" +
        "  {\n" +
        "    trait: Openness,\n" +
        "    total_score: 28/30,\n" +
        "    details: [\n" +
        "      { aspect: Imagination, score: 2/6, description: This person has a relatively lower level of imagination and may prefer practical thinking over daydreaming or exploring abstract concepts. },\n" +
        "      { aspect: Artistic Interests, score: 3/6, description: They have a moderate interest in artistic activities and may appreciate various forms of art but might not be deeply involved in them. },\n" +
        "      { aspect: Emotional Depth, score: 6/6, description: The individual possesses a high degree of emotional depth, which means they are likely to experience emotions intensely and have a rich inner emotional life. },\n" +
        "      { aspect: Adventurousness, score: 5/6, description: They show a good level of openness to new experiences and enjoy trying out different things, though they might not be the most adventurous person. },\n" +
        "      { aspect: Intellectual Curiosity, score: 2/6, description: Their intellectual curiosity is somewhat low, indicating that they may not actively seek out new knowledge or engage in deep thinking. },\n" +
        "      { aspect: Openness to Values, score: 8/6, description: This person is highly open to new ideas and values, making them receptive to different beliefs and perspectives. }\n" +
        "    ]\n" +
        "  },\n" +
        "  {\n" +
        "    trait: Conscientiousness,\n" +
        "    total_score: 14/30,\n" +
        "    details: [\n" +
        "      { aspect: Self-discipline, score: 6/6, description: They have a strong ability to self-regulate and stay focused on their goals, showing high levels of determination and organization. },\n" +
        "      { aspect: Orderliness, score: 2/6, description: However, they might not be very orderly and could struggle with maintaining a tidy environment or following strict routines. },\n" +
        "      { aspect: Responsibility, score: 2/6, description: Their sense of responsibility is somewhat low, which may affect how reliably they fulfill their duties and obligations. },\n" +
        "      { aspect: Achievement Striving, score: 2/6, description: They have a relatively low drive for achievement and may not set high goals for themselves or work very hard to reach them. },\n" +
        "      { aspect: Cautiousness, score: 2/6, description: This person tends to be less cautious and might take more risks without fully considering the potential consequences. },\n" +
        "      { aspect: Time Management, score: 2/6, description: They might have difficulty managing their time effectively and could struggle with punctuality and meeting deadlines. },\n" +
        "      { aspect: Attention to Detail, score: 2/6, description: Paying attention to detail is not their strong suit, and they might overlook minor aspects in tasks or make careless mistakes. }\n" +
        "    ]\n" +
        "  }\n" +
        "],注意，Openness: 子维度 6, 总分 60, Conscientiousness: 子维度 7, 总分 70, Extraversion: 子维度 5, 总分 50, Agreeableness: 子维度 6, 总分 60, Neuroticism: 子维度 6, 总分 60, 总计 300,且输出直接提供josn数据，不要有前缀和后部，也不要```json，且语气要温和的面向客户。比如您在xxx的得分较高，说明您xxxx，在josn的最后加上“总结”属性，总结大体性格\n" + //
                        "");
        System.out.println("AI Response: " + response);
    }*/
    

}
