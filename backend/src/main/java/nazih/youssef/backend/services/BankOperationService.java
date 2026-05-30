package nazih.youssef.backend.services;

import nazih.youssef.backend.exceptions.BalanceNotSufficientException;
import nazih.youssef.backend.exceptions.BankAccountNotFoundException;

public interface BankOperationService {
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
}
