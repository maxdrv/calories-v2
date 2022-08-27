package com.home.calories;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.home.calories.openapi.model.BaseProductDto;
import com.home.calories.openapi.model.UpdateBaseProductRequest;
import com.home.calories.util.Repo;
import com.home.calories.util.WithDataBase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BaseProductApiTest extends WithDataBase {

    @SneakyThrows
    @Test
    @ExpectedDatabase(
            value = "/repository/base_product/after/milk.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    void createBaseProduct() {
        caller.create(Repo.CREATE_MILK_REQUEST.get())
                .andExpect(status().isCreated())
                .andExpect(content().json(
                """
                   {
                     "id": 10000,
                     "name": "milk",
                     "nutrients": {
                       "kcal": 11.0,
                       "proteins": 14.0,
                       "fats": 13.0,
                       "carbs": 12.0
                     }
                   }
                   """,
                        true
                ));
    }

    @ExpectedDatabase(
            value = "/repository/base_product/after/several_products.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @Test
    void createSeveralBaseProducts() {
        caller.create(Repo.CREATE_MILK_REQUEST.get())
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        """
                           {
                             "id": 10000,
                             "name": "milk",
                             "nutrients": {
                               "kcal": 11.0,
                               "proteins": 14.0,
                               "fats": 13.0,
                               "carbs": 12.0
                             }
                           }
                           """,
                        true
                ));
        caller.create(Repo.CREATE_PROTEIN_REQUEST.get())
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        """
                           {
                             "id": 10001,
                             "name": "protein",
                             "nutrients": {
                               "kcal": 150.0,
                               "proteins": 23.0,
                               "fats": 4.0,
                               "carbs": 10.0
                             }
                           }
                           """,
                        true
                ));
    }

    @ExpectedDatabase(
            value = "/repository/base_product/after/milk_updated.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @Test
    void updateBaseProduct() {
        var baseProduct = caller.create(Repo.CREATE_MILK_REQUEST.get())
                .andExpect(status().isCreated())
                .andReturnAs(BaseProductDto.class);

        var nutrients = baseProduct.getNutrients()
                .kcal(1000.0)
                .proteins(1001.0)
                .fats(1002.0)
                .carbs(1003.0);

        caller.update(
                        baseProduct.getId(),
                        new UpdateBaseProductRequest()
                                .name(baseProduct.getName() + "_updated")
                                .nutrients(nutrients)
                ).andExpect(status().isOk())
                .andExpect(content().json(
                        """
                          {
                             "id": 10000,
                             "name": "milk_updated",
                             "nutrients": {
                               "kcal": 1000.0,
                               "proteins": 1001.0,
                               "fats": 1002.0,
                               "carbs": 1003.0
                             }
                           }
                          """,
                        true
                ));

    }

    @ExpectedDatabase(
            value = "/repository/base_product/after/products_after_delete.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @DatabaseSetup("/repository/base_product/before/products_before_delete.xml")
    @Test
    void deleteBaseProduct() {
        caller.delete(2L).andExpect(status().isOk());
    }

    @Test
    void emptyPage() {
        caller.pageOfProducts("?page=0&size=3")
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "size": 3,
                          "number": 0,
                          "totalElements": 0,
                          "totalPages": 0,
                          "content": []
                        }
                        """));
    }

    @Test
    void queryBaseProductPage() {
        for (int i = 1; i < 110; i++) {
            var request = Repo.CREATE_PROTEIN_REQUEST.get();
            request.name(request.getName() + i);
            caller.create(request).andExpect(status().isCreated());
        }

        caller.pageOfProducts("?page=0&size=3")
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "size": 3,
                          "number": 0,
                          "totalElements": 109,
                          "totalPages": 37,
                          "content": [
                            {
                              "id": 10000,
                              "name": "protein1",
                              "nutrients": {
                                "kcal": 150.0,
                                "proteins": 23.0,
                                "fats": 4.0,
                                "carbs": 10.0
                              }
                            },
                            {
                              "id": 10001,
                              "name": "protein2",
                              "nutrients": {
                                "kcal": 150.0,
                                "proteins": 23.0,
                                "fats": 4.0,
                                "carbs": 10.0
                              }
                            },
                            {
                              "id": 10002,
                              "name": "protein3",
                              "nutrients": {
                                "kcal": 150.0,
                                "proteins": 23.0,
                                "fats": 4.0,
                                "carbs": 10.0
                              }
                            }
                          ]
                        }""", true));

        caller.pageOfProducts("?page=1&size=3")
                .andExpect(content().json("""
                        {
                          "size": 3,
                          "number": 1,
                          "totalElements": 109,
                          "totalPages": 37,
                          "content": [
                            {
                              "id": 10003,
                              "name": "protein4",
                              "nutrients": {
                                "kcal": 150.0,
                                "proteins": 23.0,
                                "fats": 4.0,
                                "carbs": 10.0
                              }
                            },
                            {
                              "id": 10004,
                              "name": "protein5",
                              "nutrients": {
                                "kcal": 150.0,
                                "proteins": 23.0,
                                "fats": 4.0,
                                "carbs": 10.0
                              }
                            },
                            {
                              "id": 10005,
                              "name": "protein6",
                              "nutrients": {
                                "kcal": 150.0,
                                "proteins": 23.0,
                                "fats": 4.0,
                                "carbs": 10.0
                              }
                            }
                          ]
                        }""", true));

        caller.pageOfProducts("?page=36&size=3")
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "size": 3,
                          "number": 36,
                          "totalElements": 109,
                          "totalPages": 37,
                          "content": [
                            {
                              "id": 10108,
                              "name": "protein109",
                              "nutrients": {
                                "kcal": 150.0,
                                "proteins": 23.0,
                                "fats": 4.0,
                                "carbs": 10.0
                              }
                            }
                          ]
                        }""", true));
    }

    @Test
    void filterByName() {
        var request1 = Repo.CREATE_PROTEIN_REQUEST.get();
        request1.name("ABC");
        caller.create(request1).andExpect(status().isCreated());

        var request2 = Repo.CREATE_PROTEIN_REQUEST.get();
        request2.name("ABCD");
        caller.create(request2).andExpect(status().isCreated());

        var request3 = Repo.CREATE_PROTEIN_REQUEST.get();
        request3.name("ABCDE");
        caller.create(request3).andExpect(status().isCreated());

        caller.pageOfProducts("?name=BCD&page=0&size=10")
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "size": 10,
                          "number": 0,
                          "totalElements": 2,
                          "totalPages": 1,
                          "content": [
                            {
                              "id": 10001,
                              "name": "ABCD",
                              "nutrients": {
                                "kcal": 150.0,
                                "proteins": 23.0,
                                "fats": 4.0,
                                "carbs": 10.0
                              }
                            },
                            {
                              "id": 10002,
                              "name": "ABCDE",
                              "nutrients": {
                                "kcal": 150.0,
                                "proteins": 23.0,
                                "fats": 4.0,
                                "carbs": 10.0
                              }
                            }
                          ]
                        }""", true));

    }

    @Test
    void filterByNameLowercase() {
        var request1 = Repo.CREATE_PROTEIN_REQUEST.get();
        request1.name("ABC");
        caller.create(request1).andExpect(status().isCreated());

        caller.pageOfProducts("?name=abc&page=0&size=10")
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "size": 10,
                          "number": 0,
                          "totalElements": 1,
                          "totalPages": 1,
                          "content": [
                            {
                              "id": 10000,
                              "name": "ABC",
                              "nutrients": {
                                "kcal": 150.0,
                                "proteins": 23.0,
                                "fats": 4.0,
                                "carbs": 10.0
                              }
                            }
                          ]
                        }""", true));
    }

    @Test
    void filterByNameLowercaseCyrillic() {
        var request1 = Repo.CREATE_PROTEIN_REQUEST.get();
        request1.name("АБС");
        caller.create(request1).andExpect(status().isCreated());

        caller.pageOfProducts("?name=абс&page=0&size=10")
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "size": 10,
                          "number": 0,
                          "totalElements": 1,
                          "totalPages": 1,
                          "content": [
                            {
                              "id": 10000,
                              "name": "АБС",
                              "nutrients": {
                                "kcal": 150.0,
                                "proteins": 23.0,
                                "fats": 4.0,
                                "carbs": 10.0
                              }
                            }
                          ]
                        }""", true));
    }

    @Test
    void sorting() {
        var request = Repo.CREATE_PROTEIN_REQUEST.get();
        request.name(request.getName() + 4);
        var id = caller.create(request).andExpect(status().isCreated()).andReturnAs(BaseProductDto.class).getId();
        var request1 = Repo.CREATE_PROTEIN_REQUEST.get();
        request1.name(request1.getName() + 3);
        var id1 = caller.create(request1).andExpect(status().isCreated()).andReturnAs(BaseProductDto.class).getId();;
        var request2 = Repo.CREATE_PROTEIN_REQUEST.get();
        request2.name(request2.getName() + 3);
        var id2 = caller.create(request2).andExpect(status().isCreated()).andReturnAs(BaseProductDto.class).getId();;
        var request3 = Repo.CREATE_PROTEIN_REQUEST.get();
        request3.name(request3.getName() + 2);
        var id3 = caller.create(request3).andExpect(status().isCreated()).andReturnAs(BaseProductDto.class).getId();
        var request4 = Repo.CREATE_PROTEIN_REQUEST.get();
        request4.name(request4.getName() + 1);
        var id4 = caller.create(request4).andExpect(status().isCreated()).andReturnAs(BaseProductDto.class).getId();

        int intId = Math.toIntExact(id);
        int intId1 = Math.toIntExact(id1);
        int intId2 = Math.toIntExact(id2);
        int intId3 = Math.toIntExact(id3);
        int intId4 = Math.toIntExact(id4);

        caller.pageOfProducts("?page=0&size=5&sort=id,asc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id").value(contains(intId, intId1, intId2, intId3, intId4)));

        caller.pageOfProducts("?page=0&size=5&sort=id,desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id").value(contains(intId4, intId3, intId2, intId1, intId)));

        caller.pageOfProducts("?page=0&size=5&sort=name,asc,id,asc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id").value(contains(intId4, intId3, intId1, intId2, intId)));

        caller.pageOfProducts("?page=0&size=5&sort=name,asc,id,desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id").value(contains(intId4, intId3, intId2, intId1, intId)));
    }

}
