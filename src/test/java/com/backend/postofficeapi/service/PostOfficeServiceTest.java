package com.backend.postofficeapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.backend.postofficeapi.entities.PostOffice;
import com.backend.postofficeapi.repository.PostOfficeRepository;

public class PostOfficeServiceTest {

    @InjectMocks
    private PostOfficeService postOfficeService;

    @Mock
    private PostOfficeRepository postOfficeRepository;

    @Mock
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePostOfficeCsv() throws Exception {
        String csvContent = "officename,pincode,officeType,Deliverystatus,divisionname,regionname,circlename,Taluk,Districtname,statename,Telephone,Related Suboffice,Related Headoffice,longitude,latitude\n"
                            + "Achalapur B.O,504273,B.O,Delivery,Adilabad,Hyderabad,Andhra Pradesh,Asifabad,Adilabad,TELANGANA,NA,Rechini S.O,Mancherial H.O,NA,NA\n"
                            + "Ada B.O,504293,B.O,Delivery,Adilabad,Hyderabad,Andhra Pradesh,Asifabad,Adilabad,TELANGANA,NA,Asifabad S.O,Mancherial H.O,NA,NA";

        MockMultipartFile file = new MockMultipartFile("file", "postoffices.csv", "text/csv", csvContent.getBytes());

        ExecutorService executorServiceMock = mock(ExecutorService.class);
        when(executorServiceMock.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();  
            return null;
        });

        postOfficeService = new PostOfficeService(postOfficeRepository, executorServiceMock);

        postOfficeService.savePostOfficeCsv(file);

        verify(postOfficeRepository, times(2)).save(any(PostOffice.class));
    }
    @Test
    void testGetPostOfficesByPincode() {
        PostOffice postOffice = new PostOffice();
        postOffice.setPincode(504273);
        List<PostOffice> postOffices = List.of(postOffice);

        when(postOfficeRepository.findByPincodeAndDeliveryStatus(anyInt(), anyString()))
                .thenReturn(postOffices);

        List<PostOffice> result = postOfficeService.getPostOfficesByPincode(504273);

        verify(postOfficeRepository).findByPincodeAndDeliveryStatus(504273, "Delivery");
        assertEquals(postOffices, result);
    }

    @Test
    void testGetPostOffices() {
        PostOffice postOffice = new PostOffice();
        List<PostOffice> postOffices = List.of(postOffice);
        Page<PostOffice> page = new PageImpl<>(postOffices);

        when(postOfficeRepository.findByCircleNameAndRegionNameAndDivisionName(anyString(), anyString(), anyString(),
                any(PageRequest.class)))
                .thenReturn(page);

        List<PostOffice> result = postOfficeService.getPostOffices("circle", "region", "division", 0, 10);

        verify(postOfficeRepository).findByCircleNameAndRegionNameAndDivisionName("circle", "region", "division",
                PageRequest.of(0, 10));
        assertEquals(postOffices, result);
    }

    @Test
    void testGetCircles() {
        List<Object[]> rawData = new ArrayList<>();
        rawData.add(new Object[] { "circle1", "region1", "division1" });

        when(postOfficeRepository.findCircles(anyString(), any(PageRequest.class)))
                .thenReturn(rawData);

        Map<String, Object> result = postOfficeService.getCircles("circle1", 0, 10);

        assertTrue(result.containsKey("circles"));
        assertEquals(1, ((List<?>) result.get("circles")).size());
        assertEquals(0, result.get("page"));
        assertEquals(10, result.get("size"));
        assertEquals(1, result.get("totalElements"));
    }

    @Test
    void testGetRegions() {
        List<Object[]> rawData = new ArrayList<>();
        rawData.add(new Object[] { "region1", "division1" });

        when(postOfficeRepository.findRegions(anyString(), any(PageRequest.class)))
                .thenReturn(rawData);

        Map<String, Object> result = postOfficeService.getRegions("region1", 0, 10);

        assertTrue(result.containsKey("regions"));
        assertEquals(1, ((List<?>) result.get("regions")).size());
        assertEquals(0, result.get("page"));
        assertEquals(10, result.get("size"));
        assertEquals(1, result.get("totalElements"));
    }
}
