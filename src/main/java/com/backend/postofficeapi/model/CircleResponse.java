package com.backend.postofficeapi.model;

import java.util.List;

public class CircleResponse {
    private String circleName;
    private List<RegionResponse> regions;

    public CircleResponse(String circleName, List<RegionResponse> regions) {
		super();
		this.circleName = circleName;
		this.regions = regions;
	}

	public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public List<RegionResponse> getRegions() {
        return regions;
    }

    public void setRegions(List<RegionResponse> regions) {
        this.regions = regions;
    }
}
