# Palaemon-DB-PROXY

---

The "PALAEMON-DB-PROXY" is a microservice part of the cluster of microservices that constitute
the PaMEAS Smart Evacuation Management and Rules system (PaMEAS-A). Specifically, this microservice
is responsible the integration of PaMEAS-A with the core PALAEMON Data Fusion Bus (DFB), implemented 
over [Kafka](https://kafka.apache.org/), 
and the PALAEMON Data Storage Facility, implemented over [Elastisearch](https://www.elastic.co/) (ES). 
This service exposes a REST API that enables the rest of the PaMEAS-A microservices to implement 
the necessary messaging and persistence functions without themselves managing a connection to the 
aforementioned data sources. 

Furthermore, the PALAEMON-DB-PROXY handles the protection of the attributes of the users from 
unauthorized access by enforcing encryption to the profile attributes of the users. It is only via 
this microservice that access to the plain text attribute profile of a PaMEAS-A user can be achieved. 

The security of PaMEAS-A is further enhanced by this microservice since the  PALAEMON-DB-PROXY service implements a
strict attribute based access control (ABAC) policy by  acting as an OAUTH2.0 Resource protected by an
OAUTH2.0 authorization server (implemented via [Keycloak](https://www.keycloak.org/)). As a result
only authorized PALAEMON (including PaMEAS-A) services can gain access to 
the unencrypted profiles of the users. 


# Further Reading and Documentation

---
If you want to learn more about the "PALAEMON-DB-PROXY" microservice please read our
[data model description](https://docs.google.com/document/d/1qF6E00BL59Vmq2RB3FgAqPYd94YKSKZK/edit?usp=sharing&ouid=101096721707031783382&rtpof=true&sd=true)
that is implemented and managed by the PALAEMON-DB-PROXY microservice. Additionally, the following 
[presentation](https://docs.google.com/presentation/d/16W8H_h-qz2HTbRwcXpGJ9RnrYqZxCAZ8/edit#slide=id.g109536bd6dd_2_24) describes in detail the role of the PALAEMON-DB-PROXY in the data flows for the creation
of the users profiles while this [working document](https://docs.google.com/document/d/1ljmMZdKuIWhcCmA4jlquAxP8VplmNn1SXZUCWVLKp2o/edit?usp=sharing) presents the interactions of the rest of the
PaMEAS-A microservice with the PALAEMON-DB-PROXY. 

To gain a better understanding of the overall functionality of PaMEAS the following presentations are
helpful:
- [PaMEAS (and PALAEMON Pilots) ICT Integration](https://docs.google.com/presentation/d/1ni99nXpgV1XGvfo6XNaR3cbe4MRncCj3/edit?usp=sharing&ouid=101096721707031783382&rtpof=true&sd=true)
- [PaMEAS Evacuation Messaging Policy](https://docs.google.com/presentation/d/1uxZ4Hoah89qz3MuUqt1RmGY8Dxf0upC6/edit?usp=sharing&ouid=101096721707031783382&rtpof=true&sd=true)
- [PaMEAS-A integration](https://docs.google.com/presentation/d/1cRt34HpJzM55kundaGE65re5CHmTzsvp/edit?usp=sharing&ouid=101096721707031783382&rtpof=true&sd=true)
- [PaMEAS-N and PaMEAS-Cell](https://docs.google.com/presentation/d/1xnB5cOLFCL9GC1_jkzBss-vrYs6-Vv5h/edit?usp=sharing&ouid=101096721707031783382&rtpof=true&sd=true)
- [PaMEAS-A Testing Scenarios](https://docs.google.com/presentation/d/178G2WV1pbgP8KswFuqrGacF0mGM67ERetdLD67w74MU/edit?usp=sharing)

# Code

---

*Disclaimer: Although we tested the code extensively, the "PALAEMON-DB-PROXY" is a research
prototype that may contain bugs. We take no responsibility for and give no warranties in respect of using the code.*

## Layout

The "PALAEMON-DB-PROXY" microservice is implemented
via a Spring boot application.  As a result it adheres to the typical Spring boot structure:
- `src/main` contains the main logic of the application
- `src/test` contains the executed unit tests


# Deployment

---
The "PALAEMON-DB-PROXY" microservice is implemented via Spring Boot and is Dockerized in order to
facilitate its deployment. As a result this microservice can be easily deployed using:
```
docker run --name endimion13/palaemon-db-proxy:0.0.1a -p 8090:8080-d 
```
Additionally, a typical Docker-compose file for its deployment would look as follows:
```
 
version: '2'
services:
  palaemon-db-proxy:
    image:  endimion13/palaemon-db-proxy:0.0.1a
    environment:
      - DFB_URI=dfb.palaemon.itml.gr
      - DFB_PORT=443
      - ES_USER=esuser
      - ES_PASS=kyroCMA2081!
      - KAFKA_URI_WITH_PORT=dfb.palaemon.itml.gr:30093
      - KAFKA_TRUST_STORE_LOCATION=/store/truststore.jks
      - KAFKA_TRUST_STORE_PASSWORD=****
      - KAFKA_KEYSTORE_LOCATION=/store/keystore.jks
      - KAFKA_KEY_STORE_PASSWORD=***
      - SSL_KEYSTORE_PASS=***
      - SSL_ROOT_CERTIFICATE=/store/dfb.palaemon.itml.crt
      - OAUTH_ISSUER_URI=https://dss1.aegean.gr/auth/realms/palaemon
      - PUBLIC_ENCRYPTION_KEY_PATH=/store/public.key
      - PRIVATE_ENCRYPTION_KEY_PATH=/store/private.key
    ports:
      - 8090:8080
    volumes:
      - /home/ni/code/java/palaemon-db-proxy/:/store
```
# API

---

The PALAEMON-DB-PROXY service exposes a protected REST API. For easier integration this API is defined
using the OpenAPI notation. In order to gain access to a deployed instance API OpenAPI documentation 
navigate to `http://localhost:8090/swagger-ui/index.html`