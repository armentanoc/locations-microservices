package br.ucsal.dto.requests;

public class RequestNotFoundResponse {

    private String message;
    private Long requestId;

    public RequestNotFoundResponse(Long requestId) {
        this.requestId = requestId;
        this.message = "Solicitação não encontrada.";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long locationId) {
        this.requestId = locationId;
    }

}