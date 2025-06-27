package com.example.Personality.Services;

import com.example.Personality.Responses.AnswerReviewResponse;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenRouterService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();

    public String analyzePersonality(List<AnswerReviewResponse> answers) {
        try {
            String prompt = buildPrompt(answers);

            JSONObject payload = new JSONObject();
            payload.put("model", "mistralai/mistral-7b-instruct");

            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "system").put("content", "Bạn là chuyên gia tư vấn tâm lý và hướng nghiệp."));
            messages.put(new JSONObject().put("role", "user").put("content", prompt));
            payload.put("messages", messages);

            Request request = new Request.Builder()
                    .url("https://openrouter.ai/api/v1/chat/completions")
                    .post(RequestBody.create(payload.toString(), MediaType.parse("application/json")))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("HTTP-Referer", "https://github.com/tfuok/Personality")
                    .addHeader("X-Title", "PersonalityAI")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBodyStr = response.body() != null ? response.body().string() : "null";

                if (!response.isSuccessful()) {
                    System.out.println("==== OpenRouter Error ====");
                    System.out.println("Status: " + response.code());
                    System.out.println("Body: " + responseBodyStr);
                    return "Lỗi từ OpenRouter:\n" +
                            "Status: " + response.code() + "\n" +
                            "Body: " + responseBodyStr;
                }

                JSONObject responseJson = new JSONObject(responseBodyStr);
                String content = responseJson
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                        .trim();

                // Nếu content là JSON hợp lệ, parse và format lại
                if (content.startsWith("{")) {
                    try {
                        JSONObject resultJson = new JSONObject(content);
                        String description = resultJson.getString("description");
                        JSONArray careersArray = resultJson.getJSONArray("careers");
                        List<String> careers = new ArrayList<>();
                        for (int i = 0; i < careersArray.length(); i++) {
                            careers.add(careersArray.getString(i));
                        }
                        return description + "\n\nNgành nghề phù hợp:\n- " + String.join("\n- ", careers);
                    } catch (Exception e) {
                        // Nếu JSON lỗi, trả về content gốc
                        return content;
                    }
                } else {
                    // Nếu không phải JSON, trả về đoạn văn gốc
                    return content;
                }
            }
        } catch (Exception e) {
            return "Lỗi hệ thống AI: " + e.getMessage();
        }
    }

    private String buildPrompt(List<AnswerReviewResponse> reviews) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bạn là một chuyên gia hướng nghiệp.\n");
        sb.append("Dưới đây là danh sách câu hỏi và mức điểm người dùng chọn (1 đến 5):\n");

        int i = 1;
        for (AnswerReviewResponse r : reviews) {
            sb.append(i++)
                    .append(". \"").append(r.getQuestionContent()).append("\" → ")
                    .append(r.getSelectedRating()).append("\n");
        }

        sb.append("\n👉 Dựa vào kết quả trên, hãy trả lại phản hồi JSON đúng định dạng:\n\n");
        sb.append("{\n");
        sb.append("  \"description\": \"... (tối đa 100 từ và bằng tiếng Việt)\",\n");
        sb.append("  \"careers\": [\"Ngành 1\", \"Ngành 2\", \"Ngành 3\"]\n");
        sb.append("}\n");

        sb.append("Chỉ trả về JSON hợp lệ theo đúng format trên, không thêm giải thích.");

        return sb.toString();
    }
}