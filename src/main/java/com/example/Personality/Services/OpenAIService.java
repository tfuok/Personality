package com.example.Personality.Services;

import com.example.Personality.Responses.AnswerReviewResponse;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {
    @Value("${openai.api.key}")
    private String apiKey;

    public String analyzePersonality(List<AnswerReviewResponse> answers) {
        String prompt = convertJsonToPrompt(answers);

        OpenAiService service = new OpenAiService(apiKey);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(
                        new ChatMessage("system", "Bạn là chuyên gia tư vấn hướng nghiệp và phân tích tâm lý."),
                        new ChatMessage("user", prompt)
                ))
                .temperature(0.7)
                .maxTokens(600) // Tăng nếu cần kết quả chi tiết
                .topP(1.0)
                .n(1)
                .build();

        ChatCompletionResult result = service.createChatCompletion(request);
        return result.getChoices().get(0).getMessage().getContent().trim();
    }

    public String convertJsonToPrompt(List<AnswerReviewResponse> responses) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Bạn là một chuyên gia tư vấn tâm lý và hướng nghiệp.\n");
        prompt.append("Tôi có kết quả bài trắc nghiệm tính cách từ một người dùng.\n");
        prompt.append("Mỗi câu hỏi đo một khía cạnh hành vi, người dùng chọn mức độ đồng ý từ 1 đến 5:\n");
        prompt.append("- 1: Rất không đồng ý\n");
        prompt.append("- 2: Không đồng ý\n");
        prompt.append("- 3: Trung lập\n");
        prompt.append("- 4: Đồng ý\n");
        prompt.append("- 5: Rất đồng ý\n\n");

        prompt.append("Dưới đây là câu hỏi cùng phản hồi của người dùng:\n\n");

        int i = 1;
        for (AnswerReviewResponse review : responses) {
            prompt.append(i++)
                    .append(". \"")
                    .append(review.getQuestionContent())
                    .append("\" → ")
                    .append(review.getSelectedRating())
                    .append("\n");
        }

        prompt.append("\n🧠 Hãy phân tích dựa trên từng câu trả lời và tổng thể điểm số.\n");
        prompt.append("✍️ Trả về kết quả theo ĐÚNG định dạng sau:\n");
        prompt.append("Tính cách: (viết một đoạn mô tả tâm lý khoảng 100 từ)\n");
        prompt.append("Ngành nghề phù hợp: (liệt kê 2–4 ngành nghề phù hợp nhất, cách nhau bởi dấu phẩy)\n");
        prompt.append("\nNgôn ngữ trả lời: Tiếng Việt.\n");

        return prompt.toString();
    }
}
