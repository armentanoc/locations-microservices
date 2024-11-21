package br.ucsal.dto.locations;

import br.ucsal.domain.locations.LocationType;

public record LocationRequest(LocationType locationType, String description, Long capacity, String location, String resources, Long adminId) {
}