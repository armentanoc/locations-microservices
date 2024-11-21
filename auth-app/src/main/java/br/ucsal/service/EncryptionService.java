package br.ucsal.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import br.ucsal.service.interfaces.IEncryptionService;

@Service
public class EncryptionService implements IEncryptionService {

	@Override
	public String encode(String originalString) {
		return BCrypt.hashpw(originalString, BCrypt.gensalt());
	}

	@Override
	public boolean verifyPassword(String originalString, String hashedString) {
		return BCrypt.checkpw(originalString, hashedString);
	}

}
