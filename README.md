# GhostNet App Dev Readme

## Start Project with Docker Compose
Make sure you have Docker installed.
Start the project with just one command.
```
docker-compose up --build -d
```

Open the application in Browser
http://localhost:8070/index.xhtml

## Access PgAdmin
```
http://localhost:8080
```
Login to PgAdmin <br>
Email: admin@ghostnet.com <br>
Password: test <br>

<br>

Connect PgAdmin to Database <br>
Host: ghostnetdatabasecontainer <br>
Port: 5432 <br>
Maintainance database: ghostnetdatabase <br>
Username: ghostnet <br>
Password: test <br>

### Docker Things
Check if the containers are running
```bash
docker ps
```

<br>

For shutting down all services but keep database data
```
docker-compose down
```

---

## For creating javadoc code documentation
Execute following command in terminal
```
mvn javadoc:javadoc
```
Open index.html in Browser, see target/reports/apidocs/index.html

### Central Maven Repository
https://central.sonatype.com/

---

## Remove All
!!! With these two commands you delete the database data and remove all containers and images.
```
docker-compose down -v
docker system prune -a
```








