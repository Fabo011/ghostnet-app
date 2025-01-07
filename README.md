# GhostNet App Dev Readme

### Start Project with Docker Compose
Make sure you have Docker installed.
Start the project with just one command.
```
docker-compose build --no-cache && docker-compose up -d
```

When the database is created, execute GRANT SELECT, UPDATE, DELETE ON TABLE "appUser" TO ghostnet; in pgAdmin query tool.

Open in Browser
http://localhost:8070/ghostnet-app-1.0-SNAPSHOT/index.xhtml

---


### Central Maven Repository
https://central.sonatype.com/

---

### Docker Things
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






