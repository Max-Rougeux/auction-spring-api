#!/bin/bash

/opt/mssql/bin/sqlservr &

echo "⏳ Attente de SQL Server..."
until /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$SA_PASSWORD" -Q "SELECT 1" -C &>/dev/null; do
  sleep 2
done

echo "✅ SQL Server prêt"

# Vérifie si la DB a déjà été initialisée
INITIALIZED=$(/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$SA_PASSWORD" \
  -Q "SET NOCOUNT ON; SELECT COUNT(*) FROM sys.databases WHERE name='AUCTION'" \
  -C -h -1 2>/dev/null | tr -d ' \r\n')

if [ "$INITIALIZED" = "1" ]; then
  echo "✅ DB déjà initialisée, skip"
else
  echo "🔧 Initialisation de la DB..."
  /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$SA_PASSWORD" -i /init/01_sqlserver-init.sql -C
  echo "✅ Base initialisée"
fi

wait