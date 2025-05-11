package quiz;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChatBotService {
	 // OpenAI API 키
    final static String apiKey = "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";
    final static String endpoint = "https://api.openai.com/v1/chat/completions";

    final QuizDao quizDao;
    
    public ChatBotService()
    {
    	quizDao = new QuizDao();
    }
    
    public String getQuiz(int cnt)
    {
    	
        // HTTP 클라이언트 생성
        HttpClient client = HttpClient.newHttpClient();

        // 요청 본문 생성
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-4-turbo-preview"); // 사용할 모델 지정
        requestBody.add("messages", new Gson().toJsonTree(new Message[]{
        	    new Message("system", "유사어 퀴즈에 대해 설명해줄게. JSONobject를 만들어줘."
        	             + "하나의 한국어 단어 질문과 네 개의 제시어가 있어. 제시어 중 하나는 단어 유사도가 80%가 넘어야하고, 나머지는 30%보다 낮아야 해."
        	    		 + "reason에는 해당 정답이 유사도가 높게 나온 이유를 넣어줘." 
        	             + "answer에는 해당 문제에서 유사도가 가장 높게 나온 제시어의 번호를 넣어줘."
        	    		 + "{word:'', reason:'', answer:, choices:[{option:'',similarity:},{option:'',similarity:},{option:'',similarity:},{option:'',similarity:}]} 이 형식의 JSONObject에 담아주면 돼."),
        	    new Message("user", "유사어 퀴즈 " + Integer.toString(cnt) + "개 만들어서 유사도와 함께 JSONArray에 넣어줘. 유사도는 숫자만 넣어줘.")
        	}));

            // HTTP 요청 생성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Authorization", "Bearer " + apiKey) // 인증 헤더 추가
                    .header("Content-Type", "application/json") // JSON 형식 명시
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString())) // 요청 본문 추가
                    .build();

            try {
                // 요청 전송 및 응답 수신
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String jsonString = response.body(); // 응답 출력
                
                // Gson 객체 생성
                Gson gson = new Gson();
                
                // JSON 문자열을 파싱하여 JsonObject로 변환
                JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
                
                // choices 배열 추출
                JsonArray choicesArray = jsonObject.getAsJsonArray("choices");
                
                // 첫 번째 choices 객체의 message 객체에서 content 필드 추출
                JsonObject firstChoice = choicesArray.get(0).getAsJsonObject();
                JsonObject message = firstChoice.getAsJsonObject("message");
                String content = message.get("content").getAsString();
                
                content = content.replaceAll("^```json\\n|\\n```$", "");
                
                // JSON 문자열을 JsonArray로 변환
                JsonArray jsonArray = JsonParser.parseString(content).getAsJsonArray();

                // 배열 내 각 객체 순회 및 출력
                for (JsonElement element : jsonArray) {
                    System.out.println(element.getAsJsonObject());
                	quizDao.saveQuiz(element.getAsJsonObject());
                }
                
                return jsonString;
                
            }catch(Exception e) {
            	return "Failed";
            }
    	
    }
    
    // ChatGPT와의 대화를 구성하는 메시지 클래스
    static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

}
