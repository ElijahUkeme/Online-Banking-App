package com.elijah.onlinebankingapp.service.image;

import com.elijah.onlinebankingapp.model.image.ImageModel;
import com.elijah.onlinebankingapp.repository.image.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    public ImageModel saveProfileImage(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (fileName.contains("..")){
            throw new Exception ("File Name Contains invalid character");
        }
        ImageModel imageModel = new ImageModel(file.getContentType(),fileName,file.getBytes());
        return imageRepository.save(imageModel);
    }
    public ImageModel getImage(String imageId) throws Exception {
        return imageRepository.findById(imageId)
                .orElseThrow(()->new Exception("Image Id not found"));
    }
}
