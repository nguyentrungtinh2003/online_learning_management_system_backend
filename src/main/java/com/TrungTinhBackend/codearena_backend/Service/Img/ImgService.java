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

    public ImgService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImg(MultipartFile img) throws IOException {

            Map<?,?> uploadResult = cloudinary.uploader().upload(img.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
    }

    public String updateImg(String oldImgUrl, MultipartFile newImg) throws IOException {

            String publicID = extractPublicID(oldImgUrl);

            if (publicID != null) {
                cloudinary.uploader().destroy(publicID, ObjectUtils.emptyMap());
            }

            Map<?,?> uploadResult = cloudinary.uploader().upload(newImg.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
    }

    private String extractPublicID(String imgUrl) {
           return (imgUrl == null || imgUrl.isEmpty()) ? null :
                   imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.lastIndexOf("."));
    }
}
