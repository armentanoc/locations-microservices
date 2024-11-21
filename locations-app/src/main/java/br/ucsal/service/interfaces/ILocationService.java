package br.ucsal.service.interfaces;

import br.ucsal.dto.locations.*;

import java.util.List;
import java.util.Optional;

public interface ILocationService {
    AddLocationResponse add(LocationRequest request);
    Optional<LocationResponse> get(Long locationId);
    List<LocationResponse> getAll();
    DeleteResponse delete(Long locationId, DeleteRequest request);
    UpdateResponse update(Long locationId, LocationRequest request);
}
