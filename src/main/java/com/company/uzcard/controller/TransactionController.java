package com.company.uzcard.controller;

import com.company.uzcard.dto.TransactionDTO;
import com.company.uzcard.dto.TransactionFilterDTO;
import com.company.uzcard.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> makeTransaction(
            @RequestBody TransactionDTO dto){
        return ResponseEntity.ok(transactionService.makeTransaction(dto));
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<TransactionDTO>> filter(Pageable pageable,
                                                       @RequestBody TransactionFilterDTO dto){
        return ResponseEntity.ok(transactionService.filter(dto, pageable));
    }

    @GetMapping("/profile/all/{profileId}")
    public ResponseEntity<List<TransactionDTO>> findByProfileId(@PathVariable Long profileId){
        return ResponseEntity.ok(transactionService.findByProfileId(profileId));
    }

    @GetMapping("/profile/credit/{profileId}")
    public ResponseEntity<List<TransactionDTO>> findCreditByProfileId(@PathVariable Long profileId){
        return ResponseEntity.ok(transactionService.findCreditByProfileId(profileId));
    }

    @GetMapping("/profile/debit/{profileId}")
    public ResponseEntity<List<TransactionDTO>> findDebitByProfileId(@PathVariable Long profileId){
        return ResponseEntity.ok(transactionService.findDebitByProfileId(profileId));
    }

    @GetMapping("/card/all/{cardId}")
    public ResponseEntity<List<TransactionDTO>> findByCardId(@PathVariable Long cardId){
        return ResponseEntity.ok(transactionService.findByCardId(cardId));
    }

    @GetMapping("/card/credit/{cardId}")
    public ResponseEntity<List<TransactionDTO>> findCreditByCardId(@PathVariable Long cardId){
        return ResponseEntity.ok(transactionService.findCreditByCardId(cardId));
    }

    @GetMapping("/card/debit/{cardId}")
    public ResponseEntity<List<TransactionDTO>> findDebitByCardId(@PathVariable Long cardId){
        return ResponseEntity.ok(transactionService.findDebitByCardId(cardId));
    }


}
