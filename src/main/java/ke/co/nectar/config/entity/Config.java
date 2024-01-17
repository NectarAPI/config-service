package ke.co.nectar.config.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@DynamicUpdate
@Table(name = "configs")
@Entity(name = "Configs")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    public enum ConfigType {
        NATIVE, PRISM_THRIFT
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "name")
    private String name;

    @JsonProperty("ref")
    @Column(name = "ref")
    private String ref;

    @Column(name = "user_ref")
    @JsonProperty("user_ref")
    private String userRef;

    @JsonProperty("activated")
    @Column(name = "activated")
    private boolean activated;

    @JsonProperty("config_type")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ConfigType configType;

    @JsonProperty("created_at")
    @Column(name = "created_at")
    private Instant createdAt;

    public Config() {}

    public Config(String name, String ref, String userRef,
                 ConfigType configType, boolean activated, Instant createdAt)
            {
        setName(name);
        setRef(ref);
        setUserRef(userRef);
        setConfigType(configType);
        setActivated(activated);
        setCreatedAt(createdAt);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
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

}
