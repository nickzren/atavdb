# Backend

ATAV data browser API (backend/server-side).

#### Build and Run
```
# build frontend first
cd backend
mvn clean install

# export environment variables to set database connection
export DB_URL="jdbc:mysql://URL:PORT/atavdb?serverTimezone=UTC"
export DB_USER="atavdb"
export DB_PASSWORD="atavdb"

# run
java -jar target/atavdb-1.0.jar
```

#### Example
```
http://atavdb.org/api/variant/21-33040861-G-C
http://atavdb.org/api/gene/TBK1
http://atavdb.org/api/region/2:166892071-166893071
```
