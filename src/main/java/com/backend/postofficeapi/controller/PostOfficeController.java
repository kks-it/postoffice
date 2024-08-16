package com.backend.postofficeapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.postofficeapi.entities.PostOffice;
import com.backend.postofficeapi.service.PostOfficeService;

@RestController
@RequestMapping("/api/postoffices")
public class PostOfficeController {

    private final PostOfficeService postOfficeService;

    @Autowired
    public PostOfficeController(PostOfficeService postOfficeService) {
        this.postOfficeService = postOfficeService;
    }

    @PostMapping
    public ResponseEntity<String> uploadPostOffices(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
            }

            postOfficeService.savePostOfficeCsv(file);
            return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded and data saved successfully");

        } catch (

        Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the file");
        }

    }

    @GetMapping("/{pincode}")
    public ResponseEntity<List<PostOffice>> getPostOfficesByPincode(@PathVariable String pincode) {
        List<PostOffice> postOffices = postOfficeService.getPostOfficesByPincode(pincode);
        if (postOffices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(postOffices);
    }

    @GetMapping
    public ResponseEntity<List<PostOffice>> getPostOffices(
            @RequestParam(required = false) String circle,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String division,
            @RequestParam(defaultValue = "0", value = "page_no") int pageNo,
            @RequestParam(defaultValue = "50", value = "page_size") int pageSize) {

        List<PostOffice> postOffices = postOfficeService.getPostOffices(circle, region, division, pageNo, pageSize);
        return ResponseEntity.ok(postOffices);
    }

    @GetMapping("/circles")
    public Map<String, Object> getCircles(@RequestParam(value = "circle", required = false) String circleName,
            @RequestParam(value = "page_no", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "page_size", required = false, defaultValue = "50") int pageSize) {
        return postOfficeService.getCircles(circleName, pageNo, pageSize);
    }

    @GetMapping("/regions")
    public Map<String, Object> getRegions(
            @RequestParam(value = "region", required = false) String regionName,
            @RequestParam(value = "page_no", defaultValue = "0") int pageNo,
            @RequestParam(value = "page_size", defaultValue = "50") int pageSize) {
        return postOfficeService.getRegions(regionName, pageNo, pageSize);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ex.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", "An unexpected error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

class ErrorResponse {
    private String message;
    private String description;

    public ErrorResponse(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}