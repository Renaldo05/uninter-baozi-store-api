# Guia de validação — Baozi Store

1. Instale JDK 21 e Maven 3.9+ e confira com `java -version` e `mvn -version`.
2. Na raiz do projeto, execute `mvn clean verify`.
3. Crie a pasta `data` se não existir, e inicie a API com `mvn spring-boot:run`.
4. Importe a coleção `postman/Baozi-Store.postman_collection.json`.
5. Execute os POSTs de cliente, produto e pedido nessa ordem (nome `Renan5026727`, produto `Baozi tradicional`, quantidade 3).
6. Faça os GETs gerais e GETs por ID, com capturas reais mostrando método, URL, JSON e status.
7. Faça os DELETEs na ordem: pedido, produto e cliente, comprovando o status 204.
8. Coloque os prints no capítulo 4 do relatório em Word e exporte novamente para PDF.
9. Confirme o link público e resultado de Actions antes da entrega.

**Não utilize imagens simuladas como evidência do Postman.**
