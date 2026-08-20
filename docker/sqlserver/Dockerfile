FROM mcr.microsoft.com/mssql/server:2022-latest
USER root
COPY docker/sqlserver/entrypoint.sh /entrypoint.sh
COPY docker/sqlserver/init/01_sqlserver-init.sql /init/01_sqlserver-init.sql
RUN sed -i 's/\r//' /entrypoint.sh && chmod +x /entrypoint.sh
USER mssql
ENTRYPOINT ["/entrypoint.sh"]