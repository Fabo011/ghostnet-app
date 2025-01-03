# GhostNet App Dev Readme

### Central Maven Repository
https://central.sonatype.com/

---

Check installation
```bash
mvn clean install
```

Run main
```bash
mvn exec:java -Dexec.mainClass="com.shea.shepherd.Main"
```

---

### Docker Compose
Make sure you have docker installed.

Start docker compose
```bash
docker-compose up -d
```

Check if the containers are running
```bash
docker ps
```

Access PgAdmin
```
http://localhost:8080
```
Login to PgAdmin <br>
Email: admin@ghostnet.com <br>
Password: test <br>

<br>

Connect PgAdmin to Database <br>
Host: postgres <br>
Username: ghostnet <br>
Password: test <br>






