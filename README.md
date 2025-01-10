# GhostNet App Dev Readme

## Start Project with Docker Compose
Make sure you have Docker installed.
Start the project with just one command.
```
docker-compose up --build -d
```

When the database is created, execute GRANT SELECT, UPDATE, DELETE ON TABLE "appuser" TO ghostnet; in pgAdmin query tool.

Execute TestDatabaseConnection.java

Open the application in Browser
http://localhost:8070/index.xhtml

---

## Remove All
```
docker-compose down -v
docker system prune -a
```


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






