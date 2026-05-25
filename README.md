# README

A mock of spotify.

## API Docs
- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- [Open API JSON](http://localhost:8080/v3/api-docs)
- [Open API YAML](http://localhost:8080/v3/api-docs.yaml)

## Connect to database
password: `admin123`
```bash
psql -h localhost -p 5432 -d db -U admin
```

## Buld & run
```bash
gradlew build
java -jar build/libs/MusicPlay-0.0.1-SNAPSHOT.jar
```
