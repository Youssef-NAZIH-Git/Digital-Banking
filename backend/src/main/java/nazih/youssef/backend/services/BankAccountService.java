package nazih.youssef.backend.services;

import nazih.youssef.backend.dtos.*;
import nazih.youssef.backend.entities.BankAccount;
import nazih.youssef.backend.entities.CurrentAccount;
import nazih.youssef.backend.entities.Customer;
import nazih.youssef.backend.entities.SavingAccount;
import nazih.youssef.backend.exceptions.BalanceNotSufficientException;
import nazih.youssef.backend.exceptions.BankAccountNotFoundException;
import nazih.youssef.backend.exceptions.CustomerNotFoundException;

import java.util.List;
public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    List<CustomerDTO> listCustomers();
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> searchCustomers(String keyword);

    BankAccountDTO saveBankAccount(BankAccountDTO bankAccountDTO, Long customerId) throws CustomerNotFoundException;
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    List<BankAccountDTO> bankAccountList();
    void deleteBankAccount(String accountId) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    DashboardStatsDTO getDashboardStats();
}