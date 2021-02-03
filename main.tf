locals {
  region    = "europe-west2"
  zone      = "europe-west2-a"
}

provider "google" {
  project = var.project_id
  region  = local.region
  zone    = local.zone
}

provider "google-beta" {
  version = "~> 3.5"
}

provider "null" {
  version = "~> 2.1"
}

provider "random" {
  version = "~> 2.2"
}

module "postgresql-db" {
  source  = "GoogleCloudPlatform/sql-db/google//modules/postgresql"
  name                 = var.db_name
  random_instance_name = true
  database_version     = "POSTGRES_9_6"
  project_id           = var.project_id
  zone                 = local.zone
  region               = local.region
  tier                 = "db-f1-micro"

  deletion_protection = false

  ip_configuration = {
    ipv4_enabled        = true
    private_network     = null  
    require_ssl         = true
    authorized_networks = var.authorized_networks
  }
}