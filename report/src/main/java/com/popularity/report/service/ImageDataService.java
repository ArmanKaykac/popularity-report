package com.popularity.report.service;

import com.popularity.report.model.ImageData;
import com.popularity.report.model.PDFModel;
import com.popularity.report.model.Vote;
import com.popularity.report.repository.ImageDataRepository;
import com.popularity.report.repository.VoteRepository;
import com.popularity.report.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ImageDataService {

    @Autowired
    private ImageDataRepository imageDataRepository;

    @Autowired
    private VoteRepository voteRepository;

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
                .id(dbImage.get().getId())
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
                   .id(imageData.getId())
                   .name(imageData.getName())
                   .type(imageData.getType())
                   .imageData(ImageUtil.decompressImage(imageData.getImageData())).build();
            response.add(image);
        }

        return response;
    }

    @Transactional
    public String addVote(Long imageId){
        Vote vote = new Vote();
        vote.setVoteDate(new Date());
        vote.setImageData(imageDataRepository.findById(imageId).get());
        voteRepository.save(vote);
        return "OK";
    }

    @Transactional
    public List<PDFModel> getMostVotedByInterval(String interval) {
        List<PDFModel> pdfModels = new ArrayList<>();

        // Determine the date range for the given interval
        Date startDate;
        Date endDate = new Date();
        switch (interval) {
            case "daily":
                startDate = Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case "weekly":
                startDate = Date.from(LocalDateTime.now().minusWeeks(1).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case "monthly":
                startDate = Date.from(LocalDateTime.now().minusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
                break;
            default:
                throw new IllegalArgumentException("Invalid interval: " + interval);
        }

        // Get the vote counts for each image within the date range
        List<ImageData> imageDataList = imageDataRepository.findAll();
        for (ImageData imageData : imageDataList) {
            int voteCount = Math.toIntExact(voteRepository.countByImageDataAndVoteDateAfter(imageData, startDate));
            if (voteCount > 0) {
                PDFModel pdfModel = new PDFModel();
                pdfModel.setImageData(imageData);
                pdfModel.setCount(voteCount);
                pdfModels.add(pdfModel);
            }
        }

        // Sort the PDF models by vote count (in descending order)
        pdfModels.sort(Comparator.comparing(PDFModel::getCount).reversed());

        return pdfModels;
    }



}