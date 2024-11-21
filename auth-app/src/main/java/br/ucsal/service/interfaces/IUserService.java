package br.ucsal.service.interfaces;

import br.ucsal.dto.users.*;

public interface IUserService {
    LoginResponse login(LoginRequest request);
}
