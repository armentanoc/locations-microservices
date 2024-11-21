package br.ucsal.domain.logs;


import br.ucsal.domain.BaseEntity;
import br.ucsal.domain.requests.Request;
import br.ucsal.domain.requests.RequestStatus;
import br.ucsal.domain.users.User;
import jakarta.persistence.*;


@Entity
@Table (name = "approval_logs")
public class ApprovalLog extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus oldValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus newValue;

    protected ApprovalLog(){

    }

    public ApprovalLog(User user, Request request, RequestStatus oldValue, RequestStatus newValue) {
        this.user = user;
        this.request = request;
        this.oldValue = oldValue;
        this.newValue = newValue;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public RequestStatus getOldValue() {
        return oldValue;
    }

    public void setOldValue(RequestStatus oldValue) {
        this.oldValue = oldValue;
    }

    public RequestStatus getNewValue() {
        return newValue;
    }

    public void setNewValue(RequestStatus newValue) {
        this.newValue = newValue;
    }
}
