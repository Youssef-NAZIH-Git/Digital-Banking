package nazih.youssef.backend.services;

import nazih.youssef.backend.dtos.*;
import nazih.youssef.backend.exceptions.BalanceNotSufficientException;
import nazih.youssef.backend.exceptions.BankAccountNotFoundException;
import nazih.youssef.backend.exceptions.CustomerNotFoundException;

import java.util.List;
public interface BankAccountService {
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    List<BankAccountDTO> getBankAccountsByCustomerId(Long customerId) throws CustomerNotFoundException;
    List<BankAccountDTO> bankAccountList();
//    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
//    List<AccountOperationDTO> accountHistory(String accountId);
//    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

}
