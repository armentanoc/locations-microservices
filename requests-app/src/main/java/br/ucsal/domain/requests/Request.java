package br.ucsal.domain.requests;

import br.ucsal.domain.BaseEntity;
import br.ucsal.domain.locations.Location;
import br.ucsal.domain.users.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "requests")
public class Request extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private Timestamp startTime;

    @Column(nullable = false)
    private Timestamp endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    private String reason;

    protected Request() {
        // default for JPA
    }

    public Request(User user, Location location, Timestamp startTime, Timestamp endTime, RequestStatus status, String reason) {
        this.user = user;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.reason = reason;
    }

    public Request(Request request) {
        super();
        this.user = request.user;
        this.location = request.location;
        this.startTime = request.startTime;
        this.endTime = request.endTime;
        this.status = request.status;
        this.reason = request.reason;
    }

    public Request copy() {
        return new Request(this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location != null) {
            this.location = location;
        }
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        if (startTime != null) {
            this.startTime = startTime;
        }
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        if (endTime != null) {
            this.endTime = endTime;
        }
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        if (status != null) {
            this.status = status;
        }
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        if (reason != null) {
            this.reason = reason;
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "user=" + user +
                ", location=" + location +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}
