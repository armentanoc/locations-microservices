package br.ucsal.dto.locations;

import br.ucsal.domain.locations.LocationType;

public record LocationResponse(Long id, LocationType locationType, String description, Long capacity, String location, String resources) {
}
