package com.chrisjansen.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import static javax.persistence.GenerationType.IDENTITY;
import java.io.Serializable;

/**
 * @author Chris Jansen
 */
@Entity
public class MessageTrack implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageTrack{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
