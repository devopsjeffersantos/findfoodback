package com.techchallenge.restaurant.api.findfood;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FindfoodApplication.class)
class FindFoodApplicationTest {
    @Autowired
    private FindfoodApplication application;

    @Test
    void contextLoads() {
        // Verifica se a aplicação foi iniciada corretamente
        assertThat(application).isNotNull();
    }
}
