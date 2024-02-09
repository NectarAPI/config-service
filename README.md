
# Nectar API Config Service

NectarAPI is a microservices-based, integrated meter device management (MDM) and head-end system (HES) tool for prepaid, STSEd2 meters. It is developed to support high availability for small, medium and large utilities and is intended to be deployed on kubernetes or similar orchestrators. NectarAPI allows utilities to generate and decode IEC62055-41 tokens using its internal virtual HSM or a Prism HSM via the Prism Thrift API. In addition, it allows for subscriber, meter and utility management and multiple STS configurations can be managed using the NectarAPI. NectarAPI uses an API-first approach and exposes feature-rich, REST API endpoints that allow for token generation/decoding, subscribers/users/utility management, logging e.t.c. NectarAPI's virtual HSM is IEC62055-41:2018 (STS6) compliant and supports DES (DKGA02) and KDF-HMAC-SHA-256 (DKGA04) as well as STA (EA07) and MISTY1 (EA11).

The config-service is one of the micro-services required to run the NectarAPI. This service is responsible to the secure, encrypted, management of STS configurations used to generate and decode tokens. It supports the following types of configurations:

## Native STS configurations
These are used by the NectarAPI to generate tokens using its virtual HSM. These configurations have the following format:

---
name: example_native_config
type: native
key_expiry_no: 255
encryption_algorithm: sta
token_carrier_type: numeric
decoder_key_generation_algorithm: 04
tariff_index: 01w
key_revision_no: 1
vending_key: 0123456789abcdef
supply_group_code: 123456
key_type: 3
base_date: 2035
issuer_identification_no: 600727

# PrismThrift configurations
These are used by the NectarAPI to connect to a Prism HSM via the Thrift API and generate or decode tokens. These configurations have the following format:

---
name: example prism thrift config
type: prism-thrift
host: 194.0.0.0
port: 9443
realm: local
username: username
password: password
encryption_algorithm: sta
token_carrier_type: numeric
supply_group_code: 600675
key_revision_no: 1
key_expiry_no: 255
tariff_index: 01


# Built with

NectarAPI config-service is built using Springboot version 3, OpenJDK (Java) version 17, Redis and Gradle8 for builds. 

# Getting Started

To run the config service, first run the [nectar-db](https://github.com/NectarAPI/nectar-db) service and ensure that the credentials and port match those defined in this service's `/src/main/resources/application.yml` configurations. 

Then use gradle to deploy and run the service

`./gradlew build -x test && ./gradlew bootRun`


You should have output similar to the following:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.1)

2024-02-07T17:13:03.465Z  INFO 1 --- [           main] k.c.n.c.NectarConfigServiceApplication   : Starting NectarConfigServiceApplication using Java 17.0.10 with PID 1 (/etc/config-service/config-service.jar started by root in /etc/config-service)
2024-02-07T17:13:03.471Z  INFO 1 --- [           main] k.c.n.c.NectarConfigServiceApplication   : No active profile set, falling back to 1 default profile: "default"
2024-02-07T17:13:04.405Z  INFO 1 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2024-02-07T17:13:04.482Z  INFO 1 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 66 ms. Found 5 JPA repository interfaces.
2024-02-07T17:13:05.443Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8083 (http)
2024-02-07T17:13:05.460Z  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-02-07T17:13:05.460Z  INFO 1 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.17]
2024-02-07T17:13:05.508Z  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-02-07T17:13:05.510Z  INFO 1 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1880 ms
2024-02-07T17:13:05.699Z  INFO 1 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2024-02-07T17:13:05.792Z  INFO 1 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.4.1.Final
2024-02-07T17:13:05.828Z  INFO 1 --- [           main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2024-02-07T17:13:06.132Z  INFO 1 --- [           main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2024-02-07T17:13:06.171Z  INFO 1 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2024-02-07T17:13:06.576Z  INFO 1 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@2e29f28e
2024-02-07T17:13:06.578Z  INFO 1 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2024-02-07T17:13:06.784Z  WARN 1 --- [           main] org.hibernate.orm.deprecation            : HHH90000025: PostgreSQLDialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
2024-02-07T17:13:07.939Z  INFO 1 --- [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
Hibernate: 
    drop type if exists ConfigType cascade
Hibernate: 
    create type ConfigType as enum ('NATIVE','PRISM_THRIFT')
Hibernate: 
    create cast (varchar as ConfigType) with inout as implicit
Hibernate: 
    create cast (ConfigType as varchar) with inout as implicit
2024-02-07T17:13:08.758Z  INFO 1 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2024-02-07T17:13:09.547Z  INFO 1 --- [           main] o.s.d.j.r.query.QueryEnhancerFactory     : Hibernate is in classpath; If applicable, HQL parser will be used.
2024-02-07T17:13:10.126Z  WARN 1 --- [           main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
AuthorizationFilter@35825d43]
2024-02-07T17:13:10.935Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8083 (http) with context path ''
2024-02-07T17:13:10.948Z  INFO 1 --- [           main] k.c.n.c.NectarConfigServiceApplication   : Started NectarConfigServiceApplication in 8.087 seconds (process running for 8.66)


```

# Usage

While the `config-service` may be run independent of other NectarAPI micro-services, it is recommended that the nectar-deploy script be used to launch the tokens-service as part of NectarAPI. REST API access may then be available via the [api-gateway](https://github.com/NectarAPI/api-gateway).

# Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions are greatly appreciated.

If you have suggestions for adding or removing projects, feel free to open an issue to discuss it, or directly create a pull request after you edit the README.md file with necessary changes.

Please make sure you check your spelling and grammar.

Please create individual PRs for each suggestion.


# Creating A Pull Request
To create a PR, please use the following steps:
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

# License 

Distributed under the  AGPL-3.0 License. See LICENSE for more information
