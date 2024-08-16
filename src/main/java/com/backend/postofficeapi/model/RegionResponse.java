package com.backend.postofficeapi.model;

import java.util.List;

public class RegionResponse {
    private String regionName;
    private List<DivisionResponse> divisions;

    public RegionResponse(String regionName, List<DivisionResponse> divisions) {
		super();
		this.regionName = regionName;
		this.divisions = divisions;
	}

	public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<DivisionResponse> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<DivisionResponse> divisions) {
        this.divisions = divisions;
    }
}