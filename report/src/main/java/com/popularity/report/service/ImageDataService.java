package com.popularity.report.service;

import com.popularity.report.model.ImageData;
import com.popularity.report.repository.ImageDataRepository;
import com.popularity.report.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@Service
public class ImageDataService {

    @Autowired
    private ImageDataRepository imageDataRepository;

    public String uploadImage(MultipartFile file) throws IOException {

        imageDataRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtil.compressImage(file.getBytes())).build());

        return ("Image uploaded successfully: " +
                file.getOriginalFilename());

    }

    @Transactional
    public ImageData getInfoByImageByName(String name) {
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);

        return ImageData.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .imageData(ImageUtil.decompressImage(dbImage.get().getImageData())).build();

    }

    @Transactional
    public byte[] getImage(String name) {
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);
        byte[] image = ImageUtil.decompressImage(dbImage.get().getImageData());
        return image;
    }


    @Transactional
    public List<ImageData> getRandomImages(){
        List<ImageData> imageDataList = imageDataRepository.getRandomImages();
        List<ImageData> response = new ArrayList<>();

        for (ImageData imageData : imageDataList) {
           ImageData image =  ImageData.builder()
                   .name(imageData.getName())
                   .type(imageData.getType())
                   .imageData(ImageUtil.decompressImage(imageData.getImageData())).build();
            response.add(image);
        }

        return response;
    }


}