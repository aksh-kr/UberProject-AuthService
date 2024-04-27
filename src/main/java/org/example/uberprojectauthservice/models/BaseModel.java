package org.example.uberprojectauthservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseModel {
    @Id // this annotation makes the id property a primary key of the table
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP) // tells spring about the formats of Date object to be stored i.e. Date / Time / TimeStamp
    @CreatedDate // this annotation tells spring that only handle for object creation
    protected Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate // this annotation tells spring that only handle for object update
    protected Date updatedAt;
}
