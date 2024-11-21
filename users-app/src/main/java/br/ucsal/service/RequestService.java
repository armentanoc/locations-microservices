package br.ucsal.service;

import br.ucsal.dto.requests.*;
import br.ucsal.infrastructure.*;
import br.ucsal.service.interfaces.IRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService implements IRequestService {

    @Autowired
    private IRequestRepository requestRepository;

    @Override
    public List<RequestResponse> getAllByUserId(Long userId) {
        var requests = requestRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return requests.stream().map(request -> new RequestResponse(request.getId(),
                request.getUser().getId(), request.getLocation().getId(), request.getStatus(),
                request.getStartTime(), request.getEndTime(), request.getReason())).collect(Collectors.toList());
    }

}
