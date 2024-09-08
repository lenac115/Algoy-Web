package com.example.algoyweb.service.allen;

import com.example.algoyweb.model.dto.allen.SolvedACResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class AllenService {
    private final HttpURLConnectionEx httpEx;

    @Autowired
    public AllenService(HttpURLConnectionEx httpEx) {
        this.httpEx = httpEx;
    }

    //사용자가 푼 문제 중 문제 수준이 높은 상위 100 문제를 가져오는 api
    @Value("${solvedac.url}")
    String solvedAcApi;

    @Value("${askallen.url}")
    String askAllenUrl;

    //userName을 이용하여 sovledAC API를 통해 푼 문제 정보를 호출한다. (현재 사용하지 않음)
    public List<String> sovledacCall(String userName) throws Exception {

        String requestUrl = solvedAcApi + userName;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try{
            //API 응답을 받는다(Json 형태)
            String result = httpEx.get(requestUrl, headers);
            System.out.println(result);

            //Json을 파싱해서 문제 제목을 list에 넣고 반환하는 메서드 호출
            List<String> titlesList = parseSolvedACTitles(result);
            System.out.println(titlesList);

            return titlesList;

        } catch (Exception e){
            throw new Exception("solvedAC 호출 실패", e); //예외 발생시 상위로 전달
        }
    }

    //호출한 정보를 리스트에 넣는다 (현재 사용하지 않음)
    public List<String> parseSolvedACTitles(String jsonResponse){
        //Gson 객체 생성
        Gson gson = new Gson();

        //Json을 SolvedACResponse 객체로 파싱
        SolvedACResponse response = gson.fromJson(jsonResponse, SolvedACResponse.class);

        //결과를 담을 리스트 생성
        List<String> titlesList = new ArrayList<>();

        //items 배열의 각 요소에서 titles의 title 값을 추출
        for (SolvedACResponse.Item item : response.getItems()){
            //titles 배열의 각 titles을 추가
            for(SolvedACResponse.Title title : item.getTitles()){
                if (title.getTitle() != null){
                    titlesList.add(title.getTitle());
                }
            }
        }
        return titlesList;
    }

    //질문을 allen API에 묻고 답변을 받아온다
    public String askAllen(String algoyUserName, String solvedACUserName) throws Exception {
        String askUrl = askAllenUrl + "?algoyusername=" + algoyUserName + "&solvedacusername=" + solvedACUserName;
        System.out.println(askUrl);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String allenResponse = ""; //앨런에게 바로 받은 답변
        String temp = ""; // 마크다운을 제거한 Json 형식 답변
        String responseToUser = ""; //Json에서 텍스트 형식으로 변환한 화면에 띄울 최종 답변
        try{
            allenResponse = httpEx.get(askUrl, headers);
            //System.out.println(allenResponse);
            temp = extractJsonFromMarkdown(allenResponse);
            responseToUser = convertJsonToFormattedString(temp);

        }catch (Exception e){
            throw new Exception("allen에게 답변 받기 실패", e);
        }
        return responseToUser;
    }

    //앨런에게 받은 답변에서 마크다운 제거하기
    public String extractJsonFromMarkdown(String markdown){
        //마트다운 블록 시작과 끝을 제거하여 순수한 Json을 추출
        String json = markdown.replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
        return json;
    }

    //Json 형식을 String(화면에 보여줄 형식)으로 변환
    public String convertJsonToFormattedString(String json){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        //필요한 형식으로 변환
        String formattedString = jsonObject.get("site").getAsString() + " - " +
                                jsonObject.get("title").getAsString() + " (" +
                                jsonObject.get("problemNo").getAsString() + ")\n" +
                                jsonObject.get("details").getAsString();
        return formattedString;
    }


}
