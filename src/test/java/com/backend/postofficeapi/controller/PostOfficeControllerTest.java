

package com.backend.postofficeapi.controller;

import com.backend.postofficeapi.entities.PostOffice;
import com.backend.postofficeapi.service.PostOfficeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PostOfficeControllerTest {

    @InjectMocks
    private PostOfficeController postOfficeController;

    @Mock
    private PostOfficeService postOfficeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postOfficeController).build();
    }

    private MockMultipartFile createMockMultipartFile() throws IOException {
        String csvContent = "officeName,pincode,officeType,deliveryStatus,divisionName,regionName,circleName,taluk,districtName,stateName,telephone,relatedSuboffice,relatedHeadoffice,longitude,latitude\n" +
                "Achalapur B.O,504273,B.O,Delivery,Adilabad,Hyderabad,Andhra Pradesh,Asifabad,Adilabad,TELANGANA,NA,Rechini S.O,Mancherial H.O,,\n" +
                "Ada B.O,504293,B.O,Delivery,Adilabad,Hyderabad,Andhra Pradesh,Asifabad,Adilabad,TELANGANA,NA,Asifabad S.O,Mancherial H.O,,\n" +
                "Adegaon B.O,504307,B.O,Delivery,Adilabad,Hyderabad,Andhra Pradesh,Boath,Adilabad,TELANGANA,NA,Echoda S.O,Adilabad H.O,,\n" +
                "Adilabad Collectorate S.O,504001,S.O,Non-Delivery,Adilabad,Hyderabad,Andhra Pradesh,Adilabad,Adilabad,TELANGANA,08732-226703,NA,Adilabad H.O,,";
        return new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                new ByteArrayInputStream(csvContent.getBytes())
        );
    }

    private PostOffice createPostOffice() {
        PostOffice postOffice = new PostOffice();
        postOffice.setOfficeName("Achalapur B.O");
        postOffice.setPincode(504273);
        postOffice.setOfficeType("B.O");
        postOffice.setDeliveryStatus("Delivery");
        postOffice.setDivisionName("Adilabad");
        postOffice.setRegionName("Hyderabad");
        postOffice.setCircleName("Andhra Pradesh");
        postOffice.setTaluk("Asifabad");
        postOffice.setDistrictName("Adilabad");
        postOffice.setStateName("TELANGANA");
        postOffice.setTelephone("NA");
        postOffice.setRelatedSuboffice("Rechini S.O");
        postOffice.setRelatedHeadoffice("Mancherial H.O");
        return postOffice;
    }

    @Test
    void testUploadPostOffices_FileIsEmpty() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        mockMvc.perform(multipart("/api/postoffices").file(emptyFile))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("File is empty"));
    }

    @Test
    void testUploadPostOffices_FileProcessingSuccess() throws Exception {
        MockMultipartFile validFile = createMockMultipartFile();

        mockMvc.perform(multipart("/api/postoffices").file(validFile))
                .andExpect(status().isCreated())
                .andExpect(content().string("File uploaded and data saved successfully"));
    }

    @Test
    void testUploadPostOffices_FileProcessingFailure() throws Exception {
        MockMultipartFile validFile = createMockMultipartFile();

        doThrow(new RuntimeException("Processing error")).when(postOfficeService).savePostOfficeCsv(any());

        mockMvc.perform(multipart("/api/postoffices").file(validFile))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to process the file"));
    }

    @Test
    void testGetPostOfficesByPincode_PostOfficesFound() throws Exception {
        List<PostOffice> postOffices = Arrays.asList(createPostOffice());

        when(postOfficeService.getPostOfficesByPincode(anyInt())).thenReturn(postOffices);

        mockMvc.perform(get("/api/postoffices/504273"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"officeName\":\"Achalapur B.O\"," +
                        "\"pincode\":504273," +
                        "\"officeType\":\"B.O\"," +
                        "\"deliveryStatus\":\"Delivery\"," +
                        "\"divisionName\":\"Adilabad\"," +
                        "\"regionName\":\"Hyderabad\"," +
                        "\"circleName\":\"Andhra Pradesh\"," +
                        "\"taluk\":\"Asifabad\"," +
                        "\"districtName\":\"Adilabad\"," +
                        "\"stateName\":\"TELANGANA\"," +
                        "\"telephone\":\"NA\"," +
                        "\"relatedSuboffice\":\"Rechini S.O\"," +
                        "\"relatedHeadoffice\":\"Mancherial H.O\"," +
                        "\"longitude\":null," +
                        "\"latitude\":null" +
                        "}]"));
    }

    @Test
    void testGetPostOfficesByPincode_NoPostOfficesFound() throws Exception {
        when(postOfficeService.getPostOfficesByPincode(anyInt())).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/postoffices/504273"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPostOffices_WithParameters() throws Exception {
        List<PostOffice> postOffices = Arrays.asList(createPostOffice());

        when(postOfficeService.getPostOffices("circle", "region", "division", 0, 50)).thenReturn(postOffices);

        mockMvc.perform(get("/api/postoffices")
                .param("circle", "circle")
                .param("region", "region")
                .param("division", "division")
                .param("page_no", "0")
                .param("page_size", "50"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"officeName\":\"Achalapur B.O\"," +
                        "\"pincode\":504273," +
                        "\"officeType\":\"B.O\"," +
                        "\"deliveryStatus\":\"Delivery\"," +
                        "\"divisionName\":\"Adilabad\"," +
                        "\"regionName\":\"Hyderabad\"," +
                        "\"circleName\":\"Andhra Pradesh\"," +
                        "\"taluk\":\"Asifabad\"," +
                        "\"districtName\":\"Adilabad\"," +
                        "\"stateName\":\"TELANGANA\"," +
                        "\"telephone\":\"NA\"," +
                        "\"relatedSuboffice\":\"Rechini S.O\"," +
                        "\"relatedHeadoffice\":\"Mancherial H.O\"," +
                        "\"longitude\":null," +
                        "\"latitude\":null" +
                        "}]"));
    }

    @Test
    void testGetCircles_WithParameters() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("circles", Arrays.asList("Circle1", "Circle2"));
        response.put("page", 0);
        response.put("size", 50);
        response.put("totalElements", 2);

        when(postOfficeService.getCircles("circle", 0, 50)).thenReturn(response);

        mockMvc.perform(get("/api/postoffices/circles")
                .param("circle", "circle")
                .param("page_no", "0")
                .param("page_size", "50"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"circles\":[\"Circle1\",\"Circle2\"],\"page\":0,\"size\":50,\"totalElements\":2}"));
    }

    @Test
    void testGetCircles_DefaultParameters() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("circles", Arrays.asList("Circle1", "Circle2"));
        response.put("page", 0);
        response.put("size", 50);
        response.put("totalElements", 2);

        when(postOfficeService.getCircles(null, 0, 50)).thenReturn(response);

        mockMvc.perform(get("/api/postoffices/circles"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"circles\":[\"Circle1\",\"Circle2\"],\"page\":0,\"size\":50,\"totalElements\":2}"));
    }

    @Test
    void testGetRegions_WithParameters() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("regions", Arrays.asList("Region1", "Region2"));
        response.put("page", 0);
        response.put("size", 50);
        response.put("totalElements", 2);

        when(postOfficeService.getRegions("region", 0, 50)).thenReturn(response);

        mockMvc.perform(get("/api/postoffices/regions")
                .param("region", "region")
                .param("page_no", "0")
                .param("page_size", "50"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"regions\":[\"Region1\",\"Region2\"],\"page\":0,\"size\":50,\"totalElements\":2}"));
    }

    @Test
    void testGetRegions_DefaultParameters() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("regions", Arrays.asList("Region1", "Region2"));
        response.put("page", 0);
        response.put("size", 50);
        response.put("totalElements", 2);

        when(postOfficeService.getRegions(null, 0, 50)).thenReturn(response);

        mockMvc.perform(get("/api/postoffices/regions"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"regions\":[\"Region1\",\"Region2\"],\"page\":0,\"size\":50,\"totalElements\":2}"));
    }

    @Test
    void testExceptionHandler() throws Exception {
        when(postOfficeService.getPostOfficesByPincode(anyInt())).thenThrow(new RuntimeException("Test exception"));

        mockMvc.perform(get("/api/postoffices/504273"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(
                        "{\"message\":\"Internal Server Error\",\"description\":\"An unexpected error occurred\"}"));
    }
}
