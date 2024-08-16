package com.backend.postofficeapi.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.postofficeapi.entities.PostOffice;
import com.backend.postofficeapi.model.CircleResponse;
import com.backend.postofficeapi.model.DivisionResponse;
import com.backend.postofficeapi.model.RegionResponse;
import com.backend.postofficeapi.repository.PostOfficeRepository;

@Service
public class PostOfficeService {
    private static final int CHUNK_SIZE = 1024 * 1024; // 1 MB

    private PostOfficeRepository postOfficeRepository;
    private ExecutorService executorService;

    public PostOfficeService(PostOfficeRepository postOfficeRepository, ExecutorService executorService) {
        this.postOfficeRepository = postOfficeRepository;
        this.executorService = executorService;
    }

    public void savePostOfficeCsv(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] chunk = Arrays.copyOfRange(buffer, 0, bytesRead);
                processChunk(chunk);
            }
        }
    }

    void processChunk(byte[] chunk) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(chunk))) {
            try (CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                    .setHeader("officename", "pincode", "officeType", "Deliverystatus", "divisionname", "regionname",
                            "circlename", "Taluk", "Districtname", "statename", "Telephone", "Related Suboffice",
                            "Related Headoffice", "longitude", "latitude")
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .setRecordSeparator("\r\n")
                    .build())) {
                for (CSVRecord csvRecord : parser) {
                    executorService.submit(() -> {
                        try {
                            PostOffice record = processPostOfficeCsv(csvRecord);
                            postOfficeRepository.save(record);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
        }
    }

    private PostOffice processPostOfficeCsv(CSVRecord csvRecord) {
        PostOffice postOffice = new PostOffice();
        postOffice.setOfficeName(csvRecord.get("officename"));
        postOffice.setPincode(Integer.parseInt(csvRecord.get("pincode")));
        postOffice.setOfficeType(csvRecord.get("officeType"));
        postOffice.setDeliveryStatus(csvRecord.get("DeliveryStatus"));
        postOffice.setDivisionName(csvRecord.get("divisionname"));
        postOffice.setRegionName(csvRecord.get("regionname"));
        postOffice.setCircleName(csvRecord.get("circlename"));
        if (!"NA".equals(csvRecord.get("Taluk"))) {
            postOffice.setTaluk(csvRecord.get("Taluk"));
        }
        postOffice.setDistrictName(csvRecord.get("Districtname"));
        postOffice.setStateName(csvRecord.get("statename"));
        if (!"NA".equals(csvRecord.get("Telephone"))) {
            postOffice.setTelephone(csvRecord.get("Telephone"));
        }
        if (!"NA".equals(csvRecord.get("Related Suboffice"))) {
            postOffice.setRelatedSuboffice(csvRecord.get("Related Suboffice"));
        }
        if (!"NA".equals(csvRecord.get("Related Headoffice"))) {
            postOffice.setRelatedHeadoffice(csvRecord.get("Related Headoffice"));
        }
        if (!"NA".equals(csvRecord.get("longitude"))) {
            postOffice.setLongitude(csvRecord.get("longitude"));
        }
        if (!"NA".equals(csvRecord.get("latitude"))) {
            postOffice.setLatitude(csvRecord.get("latitude"));
        }
        return postOffice;
    }

    public List<PostOffice> getPostOfficesByPincode(Integer pincode) {
        return postOfficeRepository.findByPincodeAndDeliveryStatus(pincode, "Delivery");
    }

    public List<PostOffice> getPostOffices(String circleName, String regionName, String divisionName, int pageNo,
            int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<PostOffice> postOfficePage = postOfficeRepository.findByCircleNameAndRegionNameAndDivisionName(circleName,
                regionName, divisionName, pageRequest);
        return postOfficePage.getContent();
    }

    public Map<String, Object> getCircles(String circleName, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo, pageSize);
        List<Object[]> rawData = postOfficeRepository.findCircles(circleName, pageable);

        Map<String, CircleResponse> circlesMap = new HashMap<>();

        for (Object[] row : rawData) {
            String circleNameResult = (String) row[0];
            String regionName = (String) row[1];
            String divisionName = (String) row[2];

            CircleResponse circleDto = circlesMap.computeIfAbsent(circleNameResult,
                    name -> new CircleResponse(name, new ArrayList<>()));

            RegionResponse regionDto = circleDto.getRegions().stream()
                    .filter(r -> r.getRegionName().equals(regionName))
                    .findFirst()
                    .orElseGet(() -> {
                        RegionResponse newRegion = new RegionResponse(regionName, new ArrayList<>());
                        circleDto.getRegions().add(newRegion);
                        return newRegion;
                    });

            if (regionDto.getDivisions().stream().noneMatch(d -> d.getDivisionName().equals(divisionName))) {
                regionDto.getDivisions().add(new DivisionResponse(divisionName));
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("circles", new ArrayList<>(circlesMap.values()));
        response.put("page", pageNo);
        response.put("size", pageSize);
        response.put("totalElements", rawData.size());

        return response;
    }

    public Map<String, Object> getRegions(String regionName, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo, pageSize);
        List<Object[]> rawData = postOfficeRepository.findRegions(regionName, pageable);

        Map<String, RegionResponse> regionsMap = new HashMap<>();

        for (Object[] row : rawData) {
            String regionNameResult = (String) row[0];
            String divisionName = (String) row[1];

            RegionResponse regionDto = regionsMap.computeIfAbsent(regionNameResult,
                    name -> new RegionResponse(name, new ArrayList<>()));

            if (regionDto.getDivisions().stream().noneMatch(d -> d.getDivisionName().equals(divisionName))) {
                regionDto.getDivisions().add(new DivisionResponse(divisionName));
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("regions", new ArrayList<>(regionsMap.values()));
        response.put("page", pageNo);
        response.put("size", pageSize);
        response.put("totalElements", rawData.size());

        return response;
    }

}
