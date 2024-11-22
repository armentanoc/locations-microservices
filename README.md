# Como Executar 🧰

Com o Rancher ou Docker Desktop abertos, abra o terminal na pasta raiz e execute:

```bash
docker compose up --build
```

# Acessando os apps 🌐

## pgadmin

```bash
http://localhost:16543/login
```

```bash
email: admin@admin.com
senha: admin
```

Ao abrir pela primeira vez, clicar em Add New Server 

```bash
Name: locations-management-system
Host: db_postgres 
Port: 5432 
Maintenance Database: postgres
Username: postgres
Password: postgres
Save password ✔
```


## eureka-server-app

```bash
http://localhost:8761/health
```

## auth-app

```bash
http://localhost:8181/swagger-ui/index.html
```

## locations-app

```bash
http://localhost:8182/swagger-ui/index.html
```

## requests-app

```bash
http://localhost:8183/swagger-ui/index.html
```

## users-app

```bash
http://localhost:8184/swagger-ui/index.html
```
