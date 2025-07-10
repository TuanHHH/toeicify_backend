#!/bin/bash

# Cài đặt PostgreSQL
echo "Installing PostgreSQL..."
sudo apt-get update
sudo apt-get install -y postgresql postgresql-contrib

# Khởi động dịch vụ
sudo service postgresql start

# Tạo user, database
sudo -u postgres psql -c "CREATE USER dev WITH PASSWORD 'dev';"
sudo -u postgres psql -c "CREATE DATABASE toeicify WITH OWNER dev;"

# Cập nhật quyền
sudo -u postgres psql -c "ALTER USER dev WITH SUPERUSER;"

echo "PostgreSQL setup complete ✅"
