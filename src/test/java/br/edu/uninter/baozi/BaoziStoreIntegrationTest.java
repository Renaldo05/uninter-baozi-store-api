package br.edu.uninter.baozi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:baozi-test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BaoziStoreIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    private long criar(String path, String json) throws Exception {
        MvcResult result = mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andReturn();
        JsonNode body = mapper.readTree(result.getResponse().getContentAsString());
        return body.get("id").asLong();
    }

    @Test
    void crudCompletoERegrasDeNegocio() throws Exception {
        long cliente = criar("/api/clientes", "{\"nome\":\"Renan5026727\",\"clienteDesde\":\"2026-11-19\"}");
        long produto = criar("/api/produtos", "{\"nome\":\"Baozi tradicional\",\"preco\":\"12.50\",\"estoque\":true}");
        long pedido = criar("/api/pedidos", "{\"clienteId\":" + cliente + ",\"produtoId\":" + produto + ",\"quantidade\":3}");

        mvc.perform(get("/api/clientes")).andExpect(status().isOk()).andExpect(jsonPath("$[0].nome").value("Renan5026727"));
        mvc.perform(get("/api/produtos")).andExpect(status().isOk()).andExpect(jsonPath("$[0].nome").value("Baozi tradicional"));
        mvc.perform(get("/api/pedidos")).andExpect(status().isOk()).andExpect(jsonPath("$[0].quantidade").value(3));
        mvc.perform(get("/api/clientes/{id}", cliente)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(cliente));
        mvc.perform(get("/api/produtos/{id}", produto)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(produto));
        mvc.perform(get("/api/pedidos/{id}", pedido)).andExpect(status().isOk()).andExpect(jsonPath("$.clienteId").value(cliente));

        mvc.perform(delete("/api/clientes/{id}", cliente)).andExpect(status().isConflict());
        mvc.perform(delete("/api/produtos/{id}", produto)).andExpect(status().isConflict());
        mvc.perform(delete("/api/pedidos/{id}", pedido)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/clientes/{id}", cliente)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/produtos/{id}", produto)).andExpect(status().isNoContent());
        mvc.perform(get("/api/pedidos/{id}", pedido)).andExpect(status().isNotFound());
    }

    @Test
    void validacaoEClienteInexistente() throws Exception {
        mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"\",\"clienteDesde\":\"2026-11-19\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/pedidos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"clienteId\":999999,\"produtoId\":1,\"quantidade\":1}"))
                .andExpect(status().isNotFound());
        mvc.perform(get("/api/clientes/999999")).andExpect(status().isNotFound());
    }

    @Test
    void atualizacaoOpcional() throws Exception {
        long id = criar("/api/produtos", "{\"nome\":\"Baozi\",\"preco\":\"10.00\",\"estoque\":true}");
        mvc.perform(put("/api/produtos/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Baozi especial\",\"preco\":\"15.00\",\"estoque\":false}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Baozi especial"));
        mvc.perform(delete("/api/produtos/{id}", id)).andExpect(status().isNoContent());
    }
}
