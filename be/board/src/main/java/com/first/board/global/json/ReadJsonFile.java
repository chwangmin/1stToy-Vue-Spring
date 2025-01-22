package com.first.board.global.json;

import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class ReadJsonFile {
    public JSONArray readArrays(MultipartFile jsonFile) {
        try {
            // MultipartFile에서 BufferedReader로 읽기
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(jsonFile.getInputStream())
            );

            // JSON 내용을 문자열로 변환
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                jsonContent.append(line);
            }
            reader.close();

            // 문자열을 JSONArray로 변환
            return new JSONArray(jsonContent.toString());

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.FILE_READ_ERROR);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.INVALID_JSON_FORMAT);
        }
    }
}

