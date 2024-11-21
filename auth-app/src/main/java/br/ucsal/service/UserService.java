package br.ucsal.service;

import java.util.Optional;
import java.util.regex.Pattern;

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
	private IEncryptionService encryptor;

	@Override
	public LoginResponse login(LoginRequest request) {
		var optionalUser = repository.findByusername(request.username());

		if (optionalUser.isEmpty())
			return new LoginResponse(Optional.empty(), false, "Usuário não encontrado");

		User user = optionalUser.get();
		boolean isPasswordCorrect = encryptor.verifyPassword(request.password(), user.getPassword());
		String message = isPasswordCorrect ? "Login realizado com sucesso." : "Senha incorreta.";
		Optional<UserResponse> userResponse = Optional.of(
				new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getUsername(), user.getRole()));

		return new LoginResponse(userResponse, isPasswordCorrect, message);
	}

	public static boolean isValidEmailAddress(String email) {

		if (email == null)
			return false;
		String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
		return EMAIL_PATTERN.matcher(email).matches();
	}
}
