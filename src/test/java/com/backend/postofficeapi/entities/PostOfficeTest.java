package com.backend.postofficeapi.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostOfficeTest {

    @Test
    void testPostOfficeEntity() {
        PostOffice postOffice = new PostOffice();
        postOffice.setOfficeName("Test Office");
        postOffice.setPincode(123456);
        postOffice.setOfficeType("B.O");
        postOffice.setDeliveryStatus("Delivery");
        postOffice.setDivisionName("Test Division");
        postOffice.setRegionName("Test Region");
        postOffice.setCircleName("Test Circle");
        postOffice.setTaluk("Test Taluk");
        postOffice.setDistrictName("Test District");
        postOffice.setStateName("Test State");
        postOffice.setTelephone("1234567890");
        postOffice.setRelatedSuboffice("Test Suboffice");
        postOffice.setRelatedHeadoffice("Test Headoffice");
        postOffice.setLongitude("12.3456");
        postOffice.setLatitude("65.4321");

        assertNotNull(postOffice);
        assertEquals("Test Office", postOffice.getOfficeName());
        assertEquals(123456, postOffice.getPincode());
        assertEquals("B.O", postOffice.getOfficeType());
        assertEquals("Delivery", postOffice.getDeliveryStatus());
        assertEquals("Test Division", postOffice.getDivisionName());
        assertEquals("Test Region", postOffice.getRegionName());
        assertEquals("Test Circle", postOffice.getCircleName());
        assertEquals("Test Taluk", postOffice.getTaluk());
        assertEquals("Test District", postOffice.getDistrictName());
        assertEquals("Test State", postOffice.getStateName());
        assertEquals("1234567890", postOffice.getTelephone());
        assertEquals("Test Suboffice", postOffice.getRelatedSuboffice());
        assertEquals("Test Headoffice", postOffice.getRelatedHeadoffice());
        assertEquals("12.3456", postOffice.getLongitude());
        assertEquals("65.4321", postOffice.getLatitude());
    }
}
