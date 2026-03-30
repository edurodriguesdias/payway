package com.example.payway.transaction.presentation;

import com.example.payway.transaction.domain.usecases.TransactionUseCase;
import com.example.payway.transaction.presentation.dto.CreateTransactionRequestDTO;
import com.example.payway.transaction.presentation.dto.TransactionResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionUseCase transactionUseCase;

    TransactionController(TransactionUseCase transactionUseCase) {
        this.transactionUseCase = transactionUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResponseDTO> createTransaction(
        @Valid
        @RequestBody CreateTransactionRequestDTO request
    ) {
        var transaction = transactionUseCase.createTransaction(request.toVO());

        return ResponseEntity.ok(
            TransactionResponseDTO.build(
                transaction.id(),
                transaction.accountId(),
                transaction.operationTypeId(),
                transaction.amount(),
                transaction.eventDate()
            )
        );
    }
}
