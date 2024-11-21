package br.ucsal.controller;

import br.ucsal.dto.locations.AddLocationResponse;
import br.ucsal.dto.locations.LocationNotFoundResponse;
import br.ucsal.dto.locations.LocationRequest;
import br.ucsal.dto.locations.LocationResponse;
import br.ucsal.dto.locations.DeleteRequest;
import br.ucsal.service.interfaces.ILocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("locations")
@Tag(name = "Locations", description = "Operations related to locations management, including retrieval, creation, and updates of location information.")
public class LocationController {

	@Autowired
	private ILocationService locationService;

	@GetMapping("/{id}")
	@Operation(summary = "Get location by ID", description = "Retrieve a location by their unique ID")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		var location = locationService.get(id);
		if (location.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(location);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LocationNotFoundResponse(id));
		}
	}

	@GetMapping
	@Operation(summary = "Get locations", description = "Retrieve all locations")
	public ResponseEntity<List<LocationResponse>> getAll() {
		var response = locationService.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	@Operation(summary = "Create a location", description = "Create a location passing all location information")
	public ResponseEntity<AddLocationResponse> create(@RequestBody LocationRequest request) {
		var response = locationService.add(request);

		if (response.success())
			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update a location", description = "Update a location based by their unique ID passing only the new information")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody LocationRequest request) {
		var response = locationService.update(id, request);

		if (response.success())
			return ResponseEntity.status(HttpStatus.OK).body(response);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PostMapping("/{id}")
	@Operation(summary = "Delete a location", description = "Delete a location with the given unique ID")
	public ResponseEntity<?> delete(@PathVariable Long id, @RequestBody DeleteRequest request) {

		var response = locationService.delete(id, request);

		if (response.success())
			return ResponseEntity.status(HttpStatus.OK).body(response);

		if(response.message().contains("não encontrado"))
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}