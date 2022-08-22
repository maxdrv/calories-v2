package com.home.calories;

import com.home.calories.util.Repo;
import com.home.calories.util.WithDataBase;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SuggestApiTest extends WithDataBase {

    @Test
    void suggestBaseProductByName() {
        var request1 = Repo.CREATE_PROTEIN_REQUEST.get();
        request1.name("ABC");
        caller.create(request1).andExpect(status().isOk());

        var request2 = Repo.CREATE_PROTEIN_REQUEST.get();
        request2.name("ABCD");
        caller.create(request2).andExpect(status().isOk());

        var request3 = Repo.CREATE_PROTEIN_REQUEST.get();
        request3.name("ABCDE");
        caller.create(request3).andExpect(status().isOk());


        caller.suggest("BCD")
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "content": [
                                { "id": 10001, "name": "ABCD" },
                                { "id": 10002, "name": "ABCDE" }
                            ]
                        }
                        """));
    }

}
