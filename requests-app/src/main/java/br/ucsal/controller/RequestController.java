package br.ucsal.controller;

import br.ucsal.domain.requests.RequestStatus;
import br.ucsal.dto.requests.*;
import br.ucsal.service.interfaces.IRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("requests")
@Tag(name = "Requests", description = "Operations related to requests management, including retrieval, creation, and updates of request information.")
public class RequestController {

	@Autowired
	private IRequestService requestService;

	@GetMapping("/{id}")
	@Operation(summary = "Get request by ID", description = "Retrieve a request by their unique ID")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		var request = requestService.get(id);
		if (request.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(request);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestNotFoundResponse(id));
		}
	}

	@GetMapping
	@Operation(summary = "Get requests", description = "Retrieve all requests")
	public ResponseEntity<List<RequestResponse>> getAll() {
		var response = requestService.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/pending")
	@Operation(summary = "Get requests with pending status", description = "Retrieve requests with RequestStatus = PENDENTE")
	public ResponseEntity<List<RequestResponse>> getAllPending() {
		var response = requestService.getAllByStatus(RequestStatus.PENDENTE);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/approved")
	@Operation(summary = "Get requests with pending status", description = "Retrieve requests with RequestStatus = APROVADO")
	public ResponseEntity<List<RequestResponse>> getAllApproved() {
		var response = requestService.getAllByStatus(RequestStatus.APROVADO);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/rejected")
	@Operation(summary = "Get requests with pending status", description = "Retrieve requests with RequestStatus = RECUSADO")
	public ResponseEntity<List<RequestResponse>> getAllRejected() {
		var response = requestService.getAllByStatus(RequestStatus.RECUSADO);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	@Operation(summary = "Create a request", description = "Create a request passing all request information")
	public ResponseEntity<AddRequestResponse> create(@RequestBody RequestRequest dto) {
		var response = requestService.add(dto);

		if (response.success())
			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update a request", description = "Update a request based by their unique ID passing only the new information")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RequestRequest request) {
		var response = requestService.update(id, request);

		if (response.success())
			return ResponseEntity.status(HttpStatus.OK).body(response);

		if(response.message().contains("autorizado"))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PostMapping("/{id}")
	@Operation(summary = "Delete a request", description = "Delete a request with the given unique ID")
	public ResponseEntity<?> delete(@PathVariable Long id, @RequestBody DeleteRequest request) {
		var response = requestService.delete(id, request);

		if (response.success())
			return ResponseEntity.status(HttpStatus.OK).body(response);

		if(response.message().contains("autorizado"))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

		if(response.message().contains("não encontrada"))
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PostMapping("{id}/approval")
	@Operation(summary = "Approve or reject a request", description = "Approve or reject a request passing requestId, a boolean and userId of manager.")
	public ResponseEntity<ApprovalResponse> approveOrReject(@PathVariable Long id, @RequestBody ApprovalRequest dto) {
		var response = requestService.approveOrReject(id, dto);

		if (response.success())
			return ResponseEntity.status(HttpStatus.OK).body(response);

		if(response.message().contains("Somente usuários com o papel GESTOR estão autorizados"))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

		if(response.message().contains("não encontrada") || response.message().contains("não encontrado"))
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}