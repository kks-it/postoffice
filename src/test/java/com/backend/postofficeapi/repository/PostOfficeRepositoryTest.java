package com.backend.postofficeapi.repository;

import com.backend.postofficeapi.entities.PostOffice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostOfficeRepositoryTest {

    @Mock
    private PostOfficeRepository postOfficeRepository;

    private PostOffice postOffice1;
    private PostOffice postOffice2;
    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        postOffice1 = new PostOffice();
        postOffice1.setPincode(123456);
        postOffice1.setDeliveryStatus("Delivery");
        postOffice1.setCircleName("Circle A");
        postOffice1.setRegionName("Region X");
        postOffice1.setDivisionName("Division 1");

        postOffice2 = new PostOffice();
        postOffice2.setPincode(654321);
        postOffice2.setDeliveryStatus("Non-Delivery");
        postOffice2.setCircleName("Circle B");
        postOffice2.setRegionName("Region Y");
        postOffice2.setDivisionName("Division 2");

        pageable = Pageable.unpaged(); // or any specific pageable object
    }

    @Test
    public void testFindByPincodeAndDeliveryStatus() {
        Integer pincode = 123456;
        String deliveryStatus = "Delivery";
        List<PostOffice> postOffices = Arrays.asList(postOffice1);

        when(postOfficeRepository.findByPincodeAndDeliveryStatus(pincode, deliveryStatus))
                .thenReturn(postOffices);

        List<PostOffice> result = postOfficeRepository.findByPincodeAndDeliveryStatus(pincode, deliveryStatus);

        assertEquals(1, result.size());
        assertEquals(123456, result.get(0).getPincode());
        assertEquals("Delivery", result.get(0).getDeliveryStatus());
    }

    @Test
    public void testFindByCircleNameAndRegionNameAndDivisionName() {
        String circleName = "Circle A";
        String regionName = "Region X";
        String divisionName = "Division 1";
        Page<PostOffice> page = new PageImpl<>(Arrays.asList(postOffice1), pageable, 1);

        when(postOfficeRepository.findByCircleNameAndRegionNameAndDivisionName(circleName, regionName, divisionName, pageable))
                .thenReturn(page);

        Page<PostOffice> result = postOfficeRepository.findByCircleNameAndRegionNameAndDivisionName(circleName, regionName, divisionName, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(circleName, result.getContent().get(0).getCircleName());
        assertEquals(regionName, result.getContent().get(0).getRegionName());
        assertEquals(divisionName, result.getContent().get(0).getDivisionName());
    }

    @Test
    public void testFindCircles() {
        String circleName = "Circle A";
        List<Object[]> resultList = Arrays.asList(
                new Object[]{circleName, "Region X", "Division 1"},
                new Object[]{circleName, "Region Y", "Division 2"}
        );

        when(postOfficeRepository.findCircles(circleName, pageable)).thenReturn(resultList);

        List<Object[]> result = postOfficeRepository.findCircles(circleName, pageable);

        assertEquals(2, result.size());
        assertEquals(circleName, result.get(0)[0]);
        assertEquals("Region X", result.get(0)[1]);
        assertEquals("Division 1", result.get(0)[2]);
    }

    @Test
    public void testFindRegions() {
        String regionName = "Region X";
        List<Object[]> resultList = Arrays.asList(
                new Object[]{regionName, "Division 1"},
                new Object[]{regionName, "Division 2"}
        );

        when(postOfficeRepository.findRegions(regionName, pageable)).thenReturn(resultList);

        List<Object[]> result = postOfficeRepository.findRegions(regionName, pageable);

        assertEquals(2, result.size());
        assertEquals(regionName, result.get(0)[0]);
        assertEquals("Division 1", result.get(0)[1]);
    }
}
