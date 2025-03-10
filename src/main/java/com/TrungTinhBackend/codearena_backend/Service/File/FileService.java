package com.TrungTinhBackend.codearena_backend.Service.File;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileService {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {

        Map<?,?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    public String updateFile(String oldFileUrl, MultipartFile newFile) throws IOException {

        String publicID = extractPublicID(oldFileUrl);

        if (publicID != null) {
            cloudinary.uploader().destroy(publicID, ObjectUtils.emptyMap());
        }

        Map<?,?> uploadResult = cloudinary.uploader().upload(newFile.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    private String extractPublicID(String fileUrl) {
        return (fileUrl == null || fileUrl.isEmpty()) ? null :
                fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.lastIndexOf("."));
    }
}
