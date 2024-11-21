package br.ucsal.service.interfaces;

import br.ucsal.dto.requests.*;

import java.util.List;

public interface IRequestService {
    List<RequestResponse> getAllByUserId(Long userId);
}
