package ke.co.nectar.config.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;

@DynamicUpdate
@Table(name = "prism_thrift_configs")
@Entity(name = "PrismThriftConfigs")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrismThriftConfig extends STSConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "config_id")
    private Config config;

    @JsonProperty("host")
    @Column(name = "host")
    private String host;

    @JsonProperty("port")
    @Column(name = "port")
    private String port;

    @JsonProperty("realm")
    @Column(name = "realm")
    private String realm;

    @JsonProperty("username")
    @Column(name = "username")
    private String username;

    @JsonProperty("password")
    @Column(name = "password")
    private String password;

    @JsonProperty("encryption_algorithm")
    @Column(name = "encryption_algorithm")
    private String encryptionAlgorithm;

    @JsonProperty("token_carrier_type")
    @Column(name = "token_carrier_type")
    private String tokenCarrierType;

    @JsonProperty("supply_group_code")
    @Column(name = "supply_group_code")
    private String supplyGroupCode;

    @JsonProperty("key_revision_no")
    @Column(name = "key_revision_no")
    private String keyRevisionNo;

    @JsonProperty("key_expiry_no")
    @Column(name = "key_expiry_no")
    private String keyExpiryNo;

    @JsonProperty("tariff_index")
    @Column(name = "tariff_index")
    private String tariffIndex;

    public PrismThriftConfig() {}

    public PrismThriftConfig(Config config, String host, String port, String realm,
                             String username, String password, String encryptionAlgorithm,
                             String tokenCarrierType, String supplyGroupCode, String keyRevisionNo,
                             String keyExpiryNo, String tariffIndex) {
        setConfig(config);
        setHost(host);
        setPort(port);
        setRealm(realm);
        setUsername(username);
        setPassword(password);
        setEncryptionAlgorithm(encryptionAlgorithm);
        setTokenCarrierType(tokenCarrierType);
        setSupplyGroupCode(supplyGroupCode);
        setKeyRevisionNo(keyRevisionNo);
        setKeyExpiryNo(keyExpiryNo);
        setTariffIndex(tariffIndex);
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public String getTokenCarrierType() {
        return tokenCarrierType;
    }

    public void setTokenCarrierType(String tokenCarrierType) {
        this.tokenCarrierType = tokenCarrierType;
    }

    public String getSupplyGroupCode() {
        return supplyGroupCode;
    }

    public void setSupplyGroupCode(String supplyGroupCode) {
        this.supplyGroupCode = supplyGroupCode;
    }

    public String getKeyRevisionNo() {
        return keyRevisionNo;
    }

    public void setKeyRevisionNo(String keyRevisionNo) {
        this.keyRevisionNo = keyRevisionNo;
    }

    public String getKeyExpiryNo() {
        return keyExpiryNo;
    }

    public void setKeyExpiryNo(String keyExpiryNo) {
        this.keyExpiryNo = keyExpiryNo;
    }

    public String getTariffIndex() {
        return tariffIndex;
    }

    public void setTariffIndex(String tariffIndex) {
        this.tariffIndex = tariffIndex;
    }
}
