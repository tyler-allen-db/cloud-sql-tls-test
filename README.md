# cloud-sql-tls-test

Testing using TLS 1.2 to encrypt in transit database connection.

# Instructions
Before running tests infra must first be provisioned using terrafrom. Once Cloud SQL has been configured you can run the tests with the following

```bash
mvn clean package
java -jar target/cloud-sql-demo-0.0.1-SNAPSHOT.jar
```

## Provision Infrastructure

### Create GCP Project
This only needs to be run if you do not have an existing project. You may need to enable billing on the account after it has been created

```bash
export PROJECT_ID=iap-test-9kx82
gcloud projects create $PROJECT_ID
gcloud config set project $PROJECT_ID
gcloud services enable sqladmin.googleapis.com
```

### Create Service account for terraform
You will need a service account and key in order to run terraform commands, follow the steps to create a service account, download its key and update the `GOOGLE_APPLICATION_CREDENTIALS` env var:

``````bash
export SERVICE_ACCOUNT_ID=cloudsql-terraform
gcloud iam service-accounts create $SERVICE_ACCOUNT_ID --display-name="CloudSQL Terraform"
gcloud projects add-iam-policy-binding $PROJECT_ID --member="serviceAccount:$SERVICE_ACCOUNT_ID@$PROJECT_ID.iam.gserviceaccount.com" --role="roles/owner"
gcloud iam service-accounts keys create key.json --iam-account "$SERVICE_ACCOUNT_ID@$PROJECT_ID.iam.gserviceaccount.com"
export GOOGLE_APPLICATION_CREDENTIALS=$PWD/key.json
```

### Run terraform
Now that the account has been configured we can run terrafrom to create the cluster:

```bash
terraform init
terraform apply
```

### Update Application.yml
You must generate an application.yml file, this should contain the login details for the new cloudsql instance. The location of this file should be: src/main/resources/application.yml and content should be

```yml
spring:  
  datasource:  
    url: "jdbc:postgresql://< your IP address >:5432/test"  
    username: < your username >
    password: < your password >
```