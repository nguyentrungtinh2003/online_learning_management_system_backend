package com.TrungTinhBackend.codearena_backend.Service.Img;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImgService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImg(MultipartFile img) throws IOException {
        try {
            Map uploadResult = cloudinary.uploader().upload(img.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new IOException("Image upload failed: " + e.getMessage(), e);
        }
    }

    public String updateImg(String oldImgUrl, MultipartFile newImg) throws IOException {
        try {
            String publicID = extractPublicID(oldImgUrl);

            if (publicID != null) {
                cloudinary.uploader().destroy(publicID, ObjectUtils.emptyMap());
            }

            Map uploadResult = cloudinary.uploader().upload(newImg.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new IOException("Image update failed: " + e.getMessage(), e);
        }
    }

    private String extractPublicID(String imgUrl) {
        try {
            if (imgUrl == null || imgUrl.isEmpty()) {
                return null;
            }

            return imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.lastIndexOf("."));
        } catch (Exception e) {
            return null;
        }
    }
}
