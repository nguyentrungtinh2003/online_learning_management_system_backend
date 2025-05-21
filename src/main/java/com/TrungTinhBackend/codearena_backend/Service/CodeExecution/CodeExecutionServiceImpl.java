package com.TrungTinhBackend.codearena_backend.Service.CodeExecution;

import com.TrungTinhBackend.codearena_backend.DTO.CodeExecutionDTO;
import com.TrungTinhBackend.codearena_backend.Entity.CodeExecution;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.CodeExecutionRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CodeExecutionServiceImpl implements CodeExecutionService {

    @Autowired
    private CodeExecutionRepository codeExecutionRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${apiKey}")
    private String apiKey;

    @Value("${apiHost}")
    private String apiHost;


    @Override
    public APIResponse executeCode(CodeExecutionDTO dto) {
        User user = userRepository.findById(dto.getUser().getId()).orElseThrow(
                () -> new NotFoundException("User not found!")
        );

        // Call Judge0 API
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-RapidAPI-Key", apiKey); // Replace with your key
        headers.set("X-RapidAPI-Host", apiHost);

        Map<String, Object> request = new HashMap<>();
        request.put("source_code", dto.getCode());
        request.put("language_id", Integer.parseInt(dto.getLanguage()));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=false&wait=true",
                entity,
                Map.class
        );

        APIResponse apiResponse = new APIResponse();

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            String output = (String) responseBody.get("stdout");
            String stderr = (String) responseBody.get("stderr");
            String finalOutput = output != null ? output : (stderr != null ? stderr : "No output");

            // Save execution to DB
            CodeExecution execution = CodeExecution.builder()
                    .language(dto.getLanguage())
                    .code(dto.getCode())
                    .output(finalOutput)
                    .executedAt(LocalDateTime.now())
                    .user(user)
                    .build();

            codeExecutionRepository.save(execution);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Code executed successfully");
            apiResponse.setData(finalOutput);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
        }

        apiResponse.setStatusCode(500L);
        apiResponse.setMessage("Error executing code");
        apiResponse.setData(null);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getExecuteCodeById(Long id) {
        APIResponse apiResponse = new APIResponse();

        CodeExecution codeExecution = codeExecutionRepository.findById(id).orElseThrow(
        () -> new NotFoundException("Execute code not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get execute code success !");
        apiResponse.setData(codeExecution);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
