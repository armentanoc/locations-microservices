package br.ucsal.controller;

import java.util.List;

import br.ucsal.dto.requests.RequestResponse;
import br.ucsal.service.interfaces.IRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ucsal.dto.users.*;
import br.ucsal.service.interfaces.IUserService;

@RestController
@RequestMapping("users")
@Tag(name = "Users", description = "Operations related to user management, including retrieval, creation, and updates of user information.")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IRequestService requestService;

	@GetMapping("/{id}")
	@Operation(summary = "Get user by ID", description = "Retrieve a user by their unique ID")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		var user = userService.get(id);
		if (user.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserNotFoundResponse(id));
		}
	}

	@GetMapping
	@Operation(summary = "Get users", description = "Retrieve all users")
	public ResponseEntity<List<UserResponse>> getAll() {
		var response = userService.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{id}/requests")
	@Operation(summary = "Get requests associated to this specific user", description = "Retrieve all request associated with this specific user")
	public ResponseEntity<List<RequestResponse>> getAllRequestsByUser(@PathVariable Long id) {
		var response = requestService.getAllByUserId(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	@Operation(summary = "Create a user", description = "Create a user passing all user information")
	public ResponseEntity<AddUserResponse> create(@RequestBody UserRequest dto) {
		var response = userService.add(dto);

		if (response.success())
			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update a user", description = "Update a user based by their unique ID passing only the new information")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserRequest request) {
		var response = userService.update(id, request);

		if (response.success())
			return ResponseEntity.status(HttpStatus.OK).body(response);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PostMapping("/{id}")
	@Operation(summary = "Delete a user", description = "Delete a user with the given unique ID")
	public ResponseEntity<?> delete(@PathVariable Long id, @RequestBody DeleteRequest request) {

		var response = userService.delete(id, request);

		if (response.success())
			return ResponseEntity.status(HttpStatus.OK).body(response);

		if(response.message().contains("não encontrado"))
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}