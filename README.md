# Baozi Store — API REST (UNINTER)

Projeto de Desenvolvimento Web Back-End — Bacharelado em Engenharia de Software, UNINTER.

**Tecnologias:** Java 21, Spring Boot 3.5.7, Spring Data JPA, H2, Bean Validation, JUnit, MockMvc. API JSON com padrão MVC.

## Funcionalidades

- Clientes: nome e data de cadastro.
- Produtos: nome, preço e disponibilidade (estoque booleano).
- Pedidos: cliente, produto e quantidade; impede pedidos de produtos indisponíveis.
- Regras de integridade: para excluir clientes ou produtos com pedidos, exclua primeiro os pedidos.

## Execução

Pré-requisitos: JDK 21 e Maven 3.9 ou superior.

```sh
mkdir -p data
mvn clean verify
mvn spring-boot:run
```

API disponível em `http://localhost:8080`. No Windows, crie a pasta `data` pelo Explorador de Arquivos se necessário. O H2 usa banco local `./data/baozi-store`, não precisa de servidor ou senha externa.

## Endpoints

| Recurso | POST | GET geral | GET por ID | PUT (extra) | DELETE |
|---|---|---|---|---|---|
| Clientes | `/api/clientes` | `/api/clientes` | `/api/clientes/{id}` | `/api/clientes/{id}` | `/api/clientes/{id}` |
| Produtos | `/api/produtos` | `/api/produtos` | `/api/produtos/{id}` | `/api/produtos/{id}` | `/api/produtos/{id}` |
| Pedidos | `/api/pedidos` | `/api/pedidos` | `/api/pedidos/{id}` | `/api/pedidos/{id}` | `/api/pedidos/{id}` |

**Payload de cliente:** `{"nome":"Renan5026727","clienteDesde":"2026-11-19"}`

**Payload de produto:** `{"nome":"Baozi tradicional","preco":12.5,"estoque":true}`

**Payload de pedido:** `{"clienteId":1,"produtoId":1,"quantidade":3}`

Os IDs são gerados pelo banco. O Postman guarda os IDs dos cadastros automaticamente. Retornos: 201 (criado), 200 (consultado/atualizado), 204 (excluído), 400 (inválido), 404 (não encontrado), 409 (conflito).

## Postman e documentação

- Importe `postman/Baozi-Store.postman_collection.json`, execute a sequência de 12 operações e capture telas **reais** dos resultados.
- `docs/DIAGRAMA_CASOS_DE_USO.puml`: diagrama UML em PlantUML.
- `docs/GUIA_VALIDACAO.md`: passo a passo para execução, prints e relatório.
- `.github/workflows/testes.yml`: compila e executa testes no GitHub Actions.

**Autor:** Renan Estevan Delfrate — RU 5026727 — Palmeira/PR — 2026.
