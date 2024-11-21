package br.ucsal.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.ucsal.domain.logs.Action;
import br.ucsal.domain.logs.ActionLog;
import br.ucsal.infrastructure.IActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ucsal.domain.users.*;
import br.ucsal.dto.users.*;
import br.ucsal.infrastructure.IUserRepository;
import br.ucsal.service.interfaces.IEncryptionService;
import br.ucsal.service.interfaces.IUserService;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUserRepository repository;

	@Autowired
	private IActionLogRepository actionLogRepository;

	@Autowired
	private IEncryptionService encryptor;

	@Override
	public AddUserResponse add(UserRequest request) {

		AddUserResponse response = new AddUserResponse(false, "Erro desconhecido", null);

		var optionalAdmin = repository.findById(request.adminId());

		if(optionalAdmin.isEmpty() || optionalAdmin.get().getRole() != UserRole.ADMINISTRADOR)
			return new AddUserResponse(false, "Usuário não encontrado ou não autorizado a realizar essa operação.", null);

		var admin = optionalAdmin.get();

		if (!isValidEmailAddress(request.email()))
			return new AddUserResponse(false, "E-mail informado não é válido", null);

		var password = encryptor.encode(request.password());
		var user = new User(request.name(), request.email(), request.username(), password, request.role());

		try {
			repository.save(user);
			var log = new ActionLog(admin, "users", user.getId(), Action.CREATE, "Usuário criado com sucesso.");
			actionLogRepository.save(log);
			response = new AddUserResponse(true, "Usuário criado com sucesso", user.getId());
		} catch (Exception ex) {
			if (ex.getMessage().contains("duplicar valor da chave") || ex.getMessage().contains("duplicated key")) {
				System.out.println(ex.getMessage());
				response = new AddUserResponse(false, "Username `" + request.username() + "` já existe.", user.getId());
			}
		}
		return response;
	}

	@Override
	public Optional<UserResponse> get(Long userId) {
		return repository.findById(userId).map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(),
				user.getUsername(), user.getRole()));
	}

	@Override
	public List<UserResponse> getAll() {
		var users = repository.findAllByOrderByIdAsc();
		return users.stream().map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(),
				user.getUsername(), user.getRole())).collect(Collectors.toList());
	}

	@Override
	public DeleteResponse delete(Long userId, DeleteRequest request) {
		var response = new DeleteResponse(false, "Erro desconhecido.");
		try {
			var optionalAdmin = repository.findById(request.adminId());

			if(optionalAdmin.isEmpty() || optionalAdmin.get().getRole() != UserRole.ADMINISTRADOR)
				return new DeleteResponse(false, "Usuário não encontrado ou não autorizado a realizar essa operação.");

			var admin = optionalAdmin.get();

			var optionalUser = repository.findById(userId);

			if (optionalUser.isEmpty())
				return new DeleteResponse(false, "Usuário não encontrado.");

			var user = optionalUser.get();

			repository.delete(user);

			var log = new ActionLog(admin, "users", userId, Action.DELETE, "Usuário excluído com sucesso");
			actionLogRepository.save(log);

			return new DeleteResponse(true, "Usuário excluído com sucesso.");
		}
		catch (Exception ex) {
			if (ex.getMessage().contains("restrição de chave estrangeira") || ex.getMessage().contains("foreign key")) {
				System.out.println(ex.getMessage());
				return new DeleteResponse(false, "O usuário não pode ser excluído porque há uma solicitação associada.");
			}
		}
		return response;
	}

	@Override
	public UpdateResponse update(Long userId, UserRequest request) {

		var response = new UpdateResponse(false, "Erro desconhecido");

		var optionalAdmin = repository.findById(request.adminId());

		if(optionalAdmin.isEmpty() || optionalAdmin.get().getRole() != UserRole.ADMINISTRADOR)
			return new UpdateResponse(false, "Usuário não encontrado ou não autorizado a realizar essa operação.");

		var admin = optionalAdmin.get();

		var optionalUser = repository.findById(userId);

		if (optionalUser.isEmpty())
			return new UpdateResponse(false, "Usuário não encontrado.");

		if (request.email() != null && !isValidEmailAddress(request.email()))
			return new UpdateResponse(false, "E-mail informado não é válido");

		var user = optionalUser.get();
		var userBefore = user.copy();

		try {
			user.setEmail(request.email());
			user.setName(request.name());
			user.setRole(request.role());
			user.setUsername(request.username());
			if(request.password() != null) {
				user.setPassword(encryptor.encode(request.password()));
			}
			repository.save(user);

			var log = new ActionLog(admin, "users", user.getId(), Action.UPDATE, "Usuário atualizado com sucesso. Antes: "+userBefore.toString()+" Depois: "+user.toString());
			actionLogRepository.save(log);

			response = new UpdateResponse(true, "Usuário atualizado com sucesso");
		} catch (Exception ex) {
			if (ex.getMessage().contains("duplicar valor da chave") || ex.getMessage().contains("duplicated key")) {
				System.out.println(ex.getMessage());
				response = new UpdateResponse(false, "Username `" + request.username() + "` já existe.");
			} else {
				response = new UpdateResponse(false, "Erro desconhecido: " + ex.getMessage());
			}
		}

		return response;

	}

	public static boolean isValidEmailAddress(String email) {

		if (email == null)
			return false;
		String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
		return EMAIL_PATTERN.matcher(email).matches();
	}
}
