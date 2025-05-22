package com.TrungTinhBackend.codearena_backend.Service.CodeExecution;

import com.TrungTinhBackend.codearena_backend.DTO.CodeExecutionDTO;
import com.TrungTinhBackend.codearena_backend.Entity.CodeExecution;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.CodeExecutionRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CodeExecutionServiceImpl implements CodeExecutionService {

    private final CodeExecutionRepository codeExecutionRepository;
    private final UserRepository userRepository;
    private final String apiKey;
    private final String apiHost;

    @Autowired
    public CodeExecutionServiceImpl(CodeExecutionRepository codeExecutionRepository,
                                    UserRepository userRepository,
                                    @Value("${apiKey}") String apiKey,
                                    @Value("${apiHost}") String apiHost) {
        this.codeExecutionRepository = codeExecutionRepository;
        this.userRepository = userRepository;
        this.apiKey = apiKey;
        this.apiHost = apiHost;
    }

    @Override
    public APIResponse executeCode(CodeExecutionDTO dto) throws JsonProcessingException {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found!"));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-RapidAPI-Host", apiHost);
        headers.set("X-RapidAPI-Key", apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("language_id", Integer.parseInt(dto.getLanguage()));
        body.put("source_code", dto.getCode());
        body.put("stdin", "");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=false&wait=true",
                entity,
                String.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Judge0 API error: " +
                    response.getStatusCode().value() + " - " + response.getBody());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), new TypeReference<>() {});

        String output = (String) responseBody.get("stdout");
        String stderr = (String) responseBody.get("stderr");
        String compileOutput = (String) responseBody.get("compile_output");

        String finalOutput = output != null ? output :
                (stderr != null ? stderr :
                        (compileOutput != null ? compileOutput : "No output"));

        CodeExecution execution = CodeExecution.builder()
                .language(dto.getLanguage())
                .code(dto.getCode())
                .output(finalOutput)
                .executedAt(LocalDateTime.now())
                .user(user)
                .build();

        codeExecutionRepository.save(execution);

        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Code executed successfully");
        apiResponse.setData(finalOutput);
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
