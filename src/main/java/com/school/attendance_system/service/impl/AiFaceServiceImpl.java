package com.school.attendance_system.service.impl;

import com.school.attendance_system.dto.response.AiRecognizeFaceResponse;
import com.school.attendance_system.dto.response.AiRegisterFaceResponse;
import com.school.attendance_system.service.AiFaceService;
import com.school.attendance_system.util.MultipartInputStreamFileResource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@Data
@RequiredArgsConstructor(onConstructor = @__(@Autowired))


public class AiFaceServiceImpl implements AiFaceService {
    @Value("${app.ai.base-url}")
    private String aiBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public AiRegisterFaceResponse registerFace(String studentCode, MultipartFile file) {

        try {
            String url = aiBaseUrl + "/ai/register-face";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("studentCode", studentCode);
            body.add("file", fileResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<AiRegisterFaceResponse> response =
                    restTemplate.postForEntity(
                            url,
                            requestEntity,
                            AiRegisterFaceResponse.class
                    );

            return response.getBody();

        } catch (Exception e) {
            throw new RuntimeException("Failed to call Python AI register-face API: " + e.getMessage());
        }
    }

    @Override
    public AiRecognizeFaceResponse recognizeFace(MultipartFile file) {

        try {
            String url = aiBaseUrl + "/ai/recognize-face";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<AiRecognizeFaceResponse> response =
                    restTemplate.postForEntity(
                            url,
                            requestEntity,
                            AiRecognizeFaceResponse.class
                    );

            return response.getBody();

        } catch (Exception e) {
            throw new RuntimeException("Failed to call Python AI recognize-face API: " + e.getMessage());
        }
    }
}
