package nazih.youssef.backend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nazih.youssef.backend.dtos.AccountHistoryDTO;
import nazih.youssef.backend.dtos.AccountOperationDTO;
import nazih.youssef.backend.dtos.CreditDTO;
import nazih.youssef.backend.dtos.DebitDTO;
import nazih.youssef.backend.exceptions.BalanceNotSufficientException;
import nazih.youssef.backend.exceptions.BankAccountNotFoundException;
import nazih.youssef.backend.services.BankAccountService;
import nazih.youssef.backend.services.BankOperationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
public class BankOperationsAPI {
    BankOperationService bankOperationService;
    BankAccountService bankAccountService;

//        @GetMapping("/accounts/{accountId}/operations")
//    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
//        return bankAccountService.(accountId);
//    }
//
//
//        @GetMapping("/accounts/{accountId}/pageOperations")
//    public AccountHistoryDTO getAccountHistory(
//            @PathVariable String accountId,
//            @RequestParam(name="page",defaultValue = "0") int page,
//            @RequestParam(name="size",defaultValue = "5")int size) throws BankAccountNotFoundException {
//        return bankAccountService.getAccountHistory(accountId,page,size);
//    }


        @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankOperationService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }
    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.bankOperationService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }
}
