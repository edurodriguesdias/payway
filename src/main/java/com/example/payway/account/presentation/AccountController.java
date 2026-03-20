package com.example.payway.account.presentation;

import com.example.payway.account.domain.useCase.AccountUseCase;
import com.example.payway.account.presentation.dto.CreateAccountRequestDTO;
import com.example.payway.account.presentation.dto.AccountResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountUseCase accountUseCase;

    AccountController(AccountUseCase accountUseCase) {
        this.accountUseCase = accountUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountResponseDTO> createAccount(
            @Valid
            @RequestBody CreateAccountRequestDTO request
    ) {
        var account = this.accountUseCase.createAccount(request.toVO());

        return ResponseEntity.ok(
                AccountResponseDTO.build(
                        account.id(),
                        request.documentNumber()
                )
        );
    }

    @GetMapping("/{documentNumber}")
    public ResponseEntity<AccountResponseDTO> getAccountByDocumentNumber(
            @PathVariable String documentNumber
    ) {
        var account = this.accountUseCase.getAccount(documentNumber);

        return ResponseEntity.ok(
            AccountResponseDTO.build(
                account.id(),
                account.documentNumber()
            )
        );
    }
}
