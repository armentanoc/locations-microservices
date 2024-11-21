package br.ucsal.domain.logs;

import br.ucsal.domain.BaseEntity;
import br.ucsal.domain.users.User;
import jakarta.persistence.*;

@Entity
@Table (name = "action_logs")
public class ActionLog extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String table_name;

    @Column(nullable = false)
    private Long table_id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action action;

    @Column(length = 1000)
    private String description;

    protected ActionLog(){
        // Default for JPA
    }

    public ActionLog(User user, String table_name, Long table_id, Action action, String description) {
        this.user = user;
        this.table_name = table_name;
        this.table_id = table_id;
        this.action = action;
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public Long getTable_id() {
        return table_id;
    }

    public void setTable_id(Long table_id) {
        this.table_id = table_id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    @Column(length = 1000)
    public void setDescription(String description) {
        this.description = description;
    }
}
