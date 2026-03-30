package com.example.payway.account.presentation;

import com.example.payway.account.infrastructure.repository.AccountEntityRepository;
import com.example.payway.account.presentation.dto.CreateAccountRequestDTO;
import com.example.payway.support.base.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends BaseIntegrationTest {

  @Autowired
  private AccountEntityRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void shouldCreateAccountSuccessfully() throws Exception {
    var request = new CreateAccountRequestDTO("26781184016");

    var result = this.executePost("/accounts", request);

    result.andExpect(status().isOk())
      .andExpect(jsonPath("$.id").isNumber())
      .andExpect(jsonPath("$.document_number")
        .value("26781184016"));
  }

  @Test
  void shouldGetAccountByDocumentNumber() throws Exception {
    var request = new CreateAccountRequestDTO("26781184016");
    this.executePost("/accounts", request)
      .andExpect(status().isOk());

    this.executeGet("/accounts/26781184016")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").isNumber())
      .andExpect(jsonPath("$.document_number").value("26781184016"));
  }

  @Test
  void shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception {
    this.executeGet("/accounts/99999999999")
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value("Document number not found!"))
      .andExpect(jsonPath("$.errors").isArray())
      .andExpect(jsonPath("$.errors").isEmpty());
  }
}
