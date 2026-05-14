package nazih.youssef.backend.web;

import lombok.AllArgsConstructor;
import nazih.youssef.backend.dtos.*;
import nazih.youssef.backend.exceptions.BalanceNotSufficientException;
import nazih.youssef.backend.exceptions.BankAccountNotFoundException;
import nazih.youssef.backend.exceptions.CustomerNotFoundException;
import nazih.youssef.backend.services.BankAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
@RestController
@AllArgsConstructor
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.bankAccountList();
    }

    @PostMapping("/accounts/{customerId}")
    public BankAccountDTO saveAccount(@PathVariable Long customerId, @RequestBody BankAccountDTO dto) throws CustomerNotFoundException {
        return bankAccountService.saveBankAccount(dto, customerId);
    }

    @DeleteMapping("/accounts/{accountId}")
    public void deleteAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        bankAccountService.deleteBankAccount(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());
    }

    @GetMapping("/dashboard/stats")
    public DashboardStatsDTO getDashboardStats() {
        return bankAccountService.getDashboardStats();
    }
}