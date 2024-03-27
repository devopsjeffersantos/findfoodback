package com.techchallenge.restaurant.api.findfood;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextException;

import java.net.ServerSocket;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8080"})
class FindfoodApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void deveSubirAplicacaoNaPortaEsperada() {

        int expectedPort = 8080;
        assertThat(this.port).isEqualTo(expectedPort);

        String url = "http://localhost:" + this.port + "/";
        String response = this.restTemplate.getForObject(url, String.class);

        assertThat(response).isNotNull();
    }

    @Test
    public void deveLancarExceptionFalhaPortaUtilizada() throws Exception {
        assertThatThrownBy(() -> new SpringApplication(FindfoodApplication.class).run())
            .isInstanceOf(ApplicationContextException.class)
            .hasMessage("Failed to start bean 'webServerStartStop'");
    }

    @Test
    public void deveLancarExceptionConfiguracaoBancoDeDadosInvalido() throws Exception {
        System.setProperty("spring.datasource.url", "jdbc:mysql://localhost:3306/invalid-database");

        assertThatThrownBy(() -> new SpringApplication(FindfoodApplication.class).run())
            .isInstanceOf(BeanCreationException.class)
            .hasMessage("Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: [PersistenceUnit: default] Unable to build Hibernate SessionFactory; nested exception is java.lang.RuntimeException: Driver org.postgresql.Driver claims to not accept jdbcUrl, jdbc:mysql://localhost:3306/invalid-database");

        System.clearProperty("spring.datasource.url");
    }

    @Test
    public void deveLancarExceptionConfiguracaoBancoDeDadosNaoExisteInvalida() throws Exception {

        System.setProperty("spring.datasource.url", "jdbc:mysql://localhost:3306/non-existent-database");

        assertThatThrownBy(() -> new SpringApplication(FindfoodApplication.class).run())
            .isInstanceOf(BeanCreationException.class)
            .hasMessage("Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: [PersistenceUnit: default] Unable to build Hibernate SessionFactory; nested exception is java.lang.RuntimeException: Driver org.postgresql.Driver claims to not accept jdbcUrl, jdbc:mysql://localhost:3306/non-existent-database");

        System.clearProperty("spring.datasource.url");
    }
}
