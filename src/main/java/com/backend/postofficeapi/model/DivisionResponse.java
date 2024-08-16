package com.backend.postofficeapi.model;

public class DivisionResponse {
    private String divisionName;

    public DivisionResponse(String divisionName) {
		super();
		this.divisionName = divisionName;
	}

	public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
}
