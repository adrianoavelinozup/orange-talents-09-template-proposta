# orange-talents-09-template-proposta/projects
Projeto desenvolvido durante o treinamento Orange Talents 9


## Comandos
```bash
# cria uma cópia das variáveis de ambiente
# Obs: o docker-compose precisa do arquivo .env com os valores das variáveis de ambiente
cp .env-example .env2

# inicia a infra para aplicação
docker-compose -f docker-compose-infra.yaml up -d

# gera jar e inicia aplicação com docker
./mvnw clean package -Dspring.profiles.active=test; docker-compose up --build

# inicia somente o banco de dados pra executar a aplicação em localhost
docker-compose up mysql

# acessa banco de dados
docker-compose exec mysql mysql -u <USUARIO> -p <BANCO_DE_DADOS>
```
