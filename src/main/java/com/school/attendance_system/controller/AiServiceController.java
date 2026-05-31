package com.school.attendance_system.controller;

import com.school.attendance_system.dto.response.AiRecognizeFaceResponse;
import com.school.attendance_system.dto.response.AiRegisterFaceResponse;
import com.school.attendance_system.service.AiFaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai-test")
@RequiredArgsConstructor
public class AiServiceController {
    private final AiFaceService aiFaceService;

    @PostMapping(value = "/register-face",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AiRegisterFaceResponse testRegisterFace(@RequestParam("studentCode") String studentCode,
                                                   @RequestParam("file") MultipartFile file) {
        return aiFaceService.registerFace(studentCode, file);
    }

    @PostMapping(value = "/recognize-face", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AiRecognizeFaceResponse testRecognizeFace(@RequestParam("file") MultipartFile file) {
        return aiFaceService.recognizeFace(file);
    }
}
