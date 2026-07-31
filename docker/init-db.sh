#!/bin/bash
set -e

POSTGRES_USER="${POSTGRES_USER:-findingjob}"
IFS=',' read -ra DATABASES <<< "${POSTGRES_MULTIPLE_DATABASES:-findingjob_auth,findingjob_profile,findingjob_company,findingjob_rating,findingjob_resume,findingjob_storage,findingjob_notification}"

for db in "${DATABASES[@]}"; do
  echo "Creating database: $db"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" <<-EOSQL
    CREATE DATABASE $db;
    GRANT ALL PRIVILEGES ON DATABASE $db TO $POSTGRES_USER;
EOSQL
done

echo "Databases created successfully"
