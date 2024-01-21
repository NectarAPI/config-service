package ke.co.nectar.config.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "public_keys")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String name;

    @Lob
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String key;

    @Column(name = "user_ref")
    @JsonProperty("user_ref")
    private String userRef;

    private boolean activated;

    private String ref;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public PublicKey() {}

    public PublicKey(String key, String name, String userRef, boolean activated,
                     String ref, Instant createdAt, Instant updatedAt) {
        setKey(key);
        setName(name);
        setUserRef(userRef);
        setActivated(activated);
        setRef(ref);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    @Override
    public String toString() {
        return String.format("ref: %s", ref);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
