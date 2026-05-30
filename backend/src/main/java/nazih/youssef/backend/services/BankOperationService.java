package nazih.youssef.backend.services;

import nazih.youssef.backend.dtos.AccountOperationDTO;
import nazih.youssef.backend.dtos.BankAccountDTO;
import nazih.youssef.backend.exceptions.BalanceNotSufficientException;
import nazih.youssef.backend.exceptions.BankAccountNotFoundException;

import java.util.List;

public interface BankOperationService {
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    List<AccountOperationDTO> getAccountOperationsByAccountId(String accountId) throws BankAccountNotFoundException;
}
