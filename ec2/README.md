# ATAV Data Browser AWS EC2

The instruction of AWS EC2 setup for ATAV data browser.

## Requirement
* Setup ATAV database on AWS EC2, check [here](https://github.com/nickzren/atav-database/tree/main/ec2) for details. (Load testing data and restore externaldb schema)
* Current server required access (TCP) to ATAV database server or test ATAV CLI directly on db server

## Launch Amazon EC2

1. Choose an Amazon Machine Image: Amazon Linux 2 AMI (HVM)
2. Choose an Instance Type: t3.2xlarge (test/dev)
3. Configure Instance Details: default
4. Add Storage: 50GB gp3 (test/dev)

## Tool Installation

#### Install Git and download repo
```
sudo yum install git -y
git clone https://github.com/nickzren/atavdb
export ATAVDB=$(pwd)/atavdb/
```

#### Install Maven
```
sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y apache-maven
```

#### Install NPM (Node Package Manager)  
```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash
. ~/.nvm/nvm.sh
nvm install node
```

#### Install Angular CLI
```
npm install -g @angular/cli@16.1.5
```

## Compile and Run

#### Compile frontend angular app
```
cd $ATAVDB/frontend/
npm install
ng build --configuration production
```

#### Compile backend spring boot api
```
cd # back to home directory
mvn clean install -f $ATAVDB/backend/pom.xml
```

#### Run atav data browser 
```
# export environment variables to set database connection
# for local testing: URL=127.0.0.1, PORT=3306
export DB_URL="jdbc:mysql://URL:PORT/atavdb?serverTimezone=UTC"
export DB_USER="atavdb"
export DB_PASSWORD="atavdb"

java -jar $ATAVDB/backend/target/atavdb-1.0.jar
```

#### Access in web browser
Add a new rule in your security group to make sure it's accessible.
Then type in your browser:
http://ec2-1-1-1-1.compute-1.amazonaws.com:8080/ (fake)
