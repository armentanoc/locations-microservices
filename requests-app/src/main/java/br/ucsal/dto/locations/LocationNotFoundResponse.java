package br.ucsal.dto.locations;

public class LocationNotFoundResponse {

    private String message;
    private Long locationId;

    public LocationNotFoundResponse(Long userId) {
        this.locationId = userId;
        this.message = "Local não encontrado.";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

}