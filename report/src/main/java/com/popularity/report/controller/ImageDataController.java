package com.popularity.report.controller;

import com.popularity.report.model.ImageData;
import com.popularity.report.model.PDFModel;
import com.popularity.report.service.ImageDataService;
import com.popularity.report.service.PDFExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageDataController {

    @Autowired
    private ImageDataService imageDataService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String response = imageDataService.uploadImage(file);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/info/{name}")
    public ResponseEntity<?>  getImageInfoByName(@PathVariable("name") String name){
        ImageData image = imageDataService.getInfoByImageByName(name);

        return ResponseEntity.status(HttpStatus.OK)
                .body(image);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?>  getImageByName(@PathVariable("name") String name){
        byte[] image = imageDataService.getImage(name);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @GetMapping("/getRandomImages")
    public Object getRandomImages(){
        Object response = imageDataService.getRandomImages();
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }


    @GetMapping("/addVote/{imageId}")
    public String addVote(@PathVariable("imageId") Long imageId){
        return imageDataService.addVote(imageId);
    }


    @GetMapping("/export")
    public void exportToPdf(HttpServletResponse response){
        response.setContentType("application/pdf");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=images_"+currentDate+"_.pdf";

        response.setHeader(headerKey, headerValue);

        List<PDFModel> images = imageDataService.getMostVotedByInterval("daily");

        PDFExporter pdfExporter = new PDFExporter(images);

        pdfExporter.export(response);
    }

    @GetMapping("/getCounts/{interval}")
    public Object gettest(@PathVariable("interval") String interval){
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageDataService.getMostVotedByInterval(interval));
    }


}