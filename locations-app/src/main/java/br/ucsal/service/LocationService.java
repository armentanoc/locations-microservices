package br.ucsal.service;

import br.ucsal.domain.locations.Location;
import br.ucsal.domain.logs.Action;
import br.ucsal.domain.logs.ActionLog;
import br.ucsal.domain.users.UserRole;
import br.ucsal.dto.locations.AddLocationResponse;
import br.ucsal.dto.locations.DeleteRequest;
import br.ucsal.dto.locations.DeleteResponse;
import br.ucsal.dto.locations.LocationRequest;
import br.ucsal.dto.locations.LocationResponse;
import br.ucsal.dto.locations.UpdateResponse;
import br.ucsal.infrastructure.IActionLogRepository;
import br.ucsal.infrastructure.ILocationRepository;
import br.ucsal.infrastructure.IUserRepository;
import br.ucsal.service.interfaces.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService implements ILocationService {

	@Autowired
	private ILocationRepository locationRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IActionLogRepository actionLogRepository;

	@Override
	public AddLocationResponse add(LocationRequest request) {

		var optionalAdmin = userRepository.findById(request.adminId());

		if(optionalAdmin.isEmpty() || optionalAdmin.get().getRole() != UserRole.ADMINISTRADOR)
			return new AddLocationResponse(false, "Local não encontrado ou usuário não autorizado a realizar essa operação.", null);

		var admin = optionalAdmin.get();

		var response = new AddLocationResponse(false, "Erro desconhecido", null);

		if (request.locationType() == null || request.location() == null) {
			return new AddLocationResponse(false, "Os campos locationType e location são obrigatórios", null);
		}

		var location = new Location(request.locationType(), request.description(), request.capacity(), request.location(), request.resources());

		try {
			locationRepository.save(location);
			var log = new ActionLog(admin, "locations", location.getId(), Action.CREATE, "Local criado com sucesso.");
			actionLogRepository.save(log);
			response = new AddLocationResponse(true, "Local adicionado com sucesso", location.getId());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return response;
	}

	@Override
	public Optional<LocationResponse> get(Long locationId) {
		return locationRepository.findById(locationId).map(location -> new LocationResponse(location.getId(),
				location.getLocationType(), location.getDescription(), location.getCapacity(),
				location.getLocation(), location.getResources()));
	}

	@Override
	public List<LocationResponse> getAll() {
		var locations = locationRepository.findAllByOrderByIdAsc();
		return locations.stream().map(location -> new LocationResponse(location.getId(),
				location.getLocationType(), location.getDescription(), location.getCapacity(),
				location.getLocation(), location.getResources())).collect(Collectors.toList());
	}

	@Override
	public DeleteResponse delete(Long locationId, DeleteRequest request) {
		var optionalAdmin = userRepository.findById(request.adminId());

		if(optionalAdmin.isEmpty() || optionalAdmin.get().getRole() != UserRole.ADMINISTRADOR)
			return new DeleteResponse(false, "Local não encontrado ou usuário não autorizado a realizar essa operação.");

		var admin = optionalAdmin.get();
		var response = new DeleteResponse(false, "Erro desconhecido.");
		try {
			var optionalLocation = locationRepository.findById(locationId);
			if (optionalLocation.isEmpty())
				return new DeleteResponse(false, "Local não encontrado.");
			locationRepository.delete(optionalLocation.get());
			var log = new ActionLog(admin, "locations", locationId, Action.DELETE, "Local excluído com sucesso.");
			actionLogRepository.save(log);
			return new DeleteResponse(true, "Local excluído com sucesso.");
		} catch (Exception ex) {
			if (ex.getMessage().contains("restrição de chave estrangeira") || ex.getMessage().contains("foreign key")) {
				System.out.println(ex.getMessage());
				return new DeleteResponse(false, "O local não pode ser excluído porque há uma solicitação associada.");
			}
		}
		return response;
	}

	@Override
	public UpdateResponse update(Long locationId, LocationRequest request) {
		var optionalAdmin = userRepository.findById(request.adminId());

		if(optionalAdmin.isEmpty() || optionalAdmin.get().getRole() != UserRole.ADMINISTRADOR)
			return new UpdateResponse(false, "Local não encontrado ou usuário não autorizado a realizar essa operação.");

		var admin = optionalAdmin.get();
		var response = new UpdateResponse(false, "Erro desconhecido.");
		var optionalLocation = locationRepository.findById(locationId);
		if (optionalLocation.isEmpty())
			return new UpdateResponse(false, "Local não encontrado.");
		var location = optionalLocation.get();
		var locationBefore = location.copy();
		try {
			location.setLocation(request.location());
			location.setLocationType(request.locationType());
			location.setCapacity(request.capacity());
			location.setDescription(request.description());
			location.setResources(request.resources());
			locationRepository.save(location);
			var log = new ActionLog(admin, "locations", locationId, Action.UPDATE, "Local atualizado com sucesso. Antes: "+locationBefore.toString()+" Depois: "+location.toString());
			actionLogRepository.save(log);
			response = new UpdateResponse(true, "Local atualizado com sucesso.");
		} catch (Exception ex) {
			if (ex.getMessage().contains("duplicar valor da chave") || ex.getMessage().contains("duplicated key")) {
				System.out.println(ex.getMessage());
				response = new UpdateResponse(false, "local `" + request.location() + "` já existe.");
			}
		}

		return response;
	}
}
