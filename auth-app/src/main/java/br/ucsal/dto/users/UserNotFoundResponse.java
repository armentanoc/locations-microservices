package br.ucsal.dto.users;

public class UserNotFoundResponse {

    private String message;
    private Long userId;

    public UserNotFoundResponse(Long userId) {
        this.userId = userId;
        this.message = "Usuário não encontrado.";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}