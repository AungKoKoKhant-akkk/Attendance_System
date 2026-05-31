package com.school.attendance_system.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;



public class MultipartInputStreamFileResource extends InputStreamResource {

    private final String fileName;
    private final long contentLength;

    public MultipartInputStreamFileResource (MultipartFile multipartFile) throws IOException {
        super(multipartFile.getInputStream());
        this.fileName = multipartFile.getOriginalFilename();
        this.contentLength = multipartFile.getSize();
    }

    @Override
    public @Nullable String getFilename() {
        return getFilename();
    }

    @Override
    public long contentLength() {

        return contentLength();
    }
}
