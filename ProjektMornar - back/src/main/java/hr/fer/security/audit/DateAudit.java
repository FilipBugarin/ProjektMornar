package hr.fer.security.audit;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import java.io.Serializable;
import java.time.Instant;

@EntityListeners(AuditingEntityListener.class)
public abstract class DateAudit implements Serializable {

	private static final long serialVersionUID = -7293534770442969660L;

	@CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
        
    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}