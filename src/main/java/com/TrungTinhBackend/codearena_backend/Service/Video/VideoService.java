package com.TrungTinhBackend.codearena_backend.Service.Video;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class VideoService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadVideo(MultipartFile video) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(video.getBytes(), ObjectUtils.asMap("resource_type", "video"));
        return uploadResult.get("url").toString();
    }

    public String updateVideo(String oldVideoUrl, MultipartFile video) throws IOException {
        String publicId = extractPublicIdFromUrl(oldVideoUrl);
        cloudinary.uploader().destroy(publicId,ObjectUtils.asMap("resource_type", "video"));

        Map uploadResult = cloudinary.uploader().upload(video.getBytes(), ObjectUtils.asMap("resource_type", "video"));
        return uploadResult.get("url").toString();
    }

    private String extractPublicIdFromUrl(String videoUrl) {
        String[] parts = videoUrl.split("/");
        String lastPart = parts[parts.length - 1]; // Lấy phần cuối cùng (chứa public_id)
        return lastPart.split("\\.")[0]; // Loại bỏ phần mở rộng (vd: .mp4)
    }
}
