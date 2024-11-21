package br.ucsal.service.interfaces;

public interface IEncryptionService {
    String encode(String originalString);
    boolean verifyPassword(String originalString, String hashedString);
}
