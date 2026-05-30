package nazih.youssef.backend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nazih.youssef.backend.dtos.*;
import nazih.youssef.backend.exceptions.BankAccountNotFoundException;
import nazih.youssef.backend.services.BankAccountService;
import nazih.youssef.backend.services.BankOperationService;
import nazih.youssef.backend.services.CustomerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
@RestController
@Slf4j
public class BankAccountAPI {
    private BankAccountService bankAccountService;
    private CustomerService customerService;
    private BankOperationService bankOperationService;



    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }



    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();
    }
}
