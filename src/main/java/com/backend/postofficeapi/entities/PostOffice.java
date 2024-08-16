package com.backend.postofficeapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class PostOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Post Office name is mandatory")
    private String officeName;

    @NotNull
    @Max(value = 999999, message = "Pin code cannot have more than 6 digits")
    private Integer pincode;

    private String officeType;

    private String deliveryStatus;

    private String divisionName;

    private String regionName;

    private String circleName;

    private String taluk;

    private String districtName;

    private String stateName;

    private String telephone;

    private String relatedSuboffice;

    private String relatedHeadoffice;

    private String longitude;

    private String latitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getOfficeType() {
        return officeType;
    }

    public void setOfficeType(String officeType) {
        this.officeType = officeType;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRelatedSuboffice() {
        return relatedSuboffice;
    }

    public void setRelatedSuboffice(String relatedSuboffice) {
        this.relatedSuboffice = relatedSuboffice;
    }

    public String getRelatedHeadoffice() {
        return relatedHeadoffice;
    }

    public void setRelatedHeadoffice(String relatedHeadoffice) {
        this.relatedHeadoffice = relatedHeadoffice;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

	@Override
	public String toString() {
		return "PostOffice [id=" + id + ", officeName=" + officeName + ", pincode=" + pincode + ", officeType="
				+ officeType + ", deliveryStatus=" + deliveryStatus + ", divisionName=" + divisionName + ", regionName="
				+ regionName + ", circleName=" + circleName + ", taluk=" + taluk + ", districtName=" + districtName
				+ ", stateName=" + stateName + ", telephone=" + telephone + ", relatedSuboffice=" + relatedSuboffice
				+ ", relatedHeadoffice=" + relatedHeadoffice + ", longitude=" + longitude + ", latitude=" + latitude
				+ "]";
	}
}