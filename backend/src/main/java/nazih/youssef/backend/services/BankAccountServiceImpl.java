package nazih.youssef.backend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nazih.youssef.backend.dtos.*;
import nazih.youssef.backend.entities.*;
import nazih.youssef.backend.enums.OperationType;
import nazih.youssef.backend.exceptions.BalanceNotSufficientException;
import nazih.youssef.backend.exceptions.BankAccountNotFoundException;
import nazih.youssef.backend.exceptions.CustomerNotFoundException;
import nazih.youssef.backend.mappers.BankAccountMapperImpl;
import nazih.youssef.backend.repositories.AccountOperationRepository;
import nazih.youssef.backend.repositories.BankAccountRepository;
import nazih.youssef.backend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        return dtoMapper.fromCustomer(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        return dtoMapper.fromCustomer(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
                .map(dtoMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        return customerRepository.searchCustomer("%" + keyword + "%").stream()
                .map(dtoMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO saveBankAccount(BankAccountDTO bankAccountDTO, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        BankAccount account;
        if (bankAccountDTO instanceof CurrentBankAccountDTO) {
            CurrentBankAccountDTO dto = (CurrentBankAccountDTO) bankAccountDTO;
            CurrentAccount current = new CurrentAccount();
            current.setOverDraft(dto.getOverDraft());
            account = current;
        } else if (bankAccountDTO instanceof SavingBankAccountDTO) {
            SavingBankAccountDTO dto = (SavingBankAccountDTO) bankAccountDTO;
            SavingAccount saving = new SavingAccount();
            saving.setInterestRate(dto.getInterestRate());
            account = saving;
        } else {
            throw new IllegalArgumentException("Unknown account type");
        }
        account.setId(UUID.randomUUID().toString());
        account.setCreatedAt(new Date());
        account.setBalance(bankAccountDTO.getBalance());
        account.setStatus(AccountStatus.CREATED);
        account.setCustomer(customer);
        BankAccount saved = bankAccountRepository.save(account);
        return mapAccount(saved);
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        return mapAccount(bankAccount);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        return bankAccountRepository.findAll().stream()
                .map(this::mapAccount)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        bankAccountRepository.delete(bankAccount);
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation op = new AccountOperation();
        op.setType(OperationType.DEBIT);
        op.setAmount(amount);
        op.setDescription(description);
        op.setOperationDate(new Date());
        op.setBankAccount(bankAccount);
        accountOperationRepository.save(op);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        AccountOperation op = new AccountOperation();
        op.setType(OperationType.CREDIT);
        op.setAmount(amount);
        op.setDescription(description);
        op.setOperationDate(new Date());
        op.setBankAccount(bankAccount);
        accountOperationRepository.save(op);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not Found"));
        Page<AccountOperation> accountOperations = accountOperationRepository
                .findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO dto = new AccountHistoryDTO();
        dto.setAccountOperationDTOS(accountOperations.getContent().stream()
                .map(dtoMapper::fromAccountOperation)
                .collect(Collectors.toList()));
        dto.setAccountId(bankAccount.getId());
        dto.setBalance(bankAccount.getBalance());
        dto.setCurrentPage(page);
        dto.setPageSize(size);
        dto.setTotalPages(accountOperations.getTotalPages());
        return dto;
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        long currentCount = accounts.stream().filter(a -> a instanceof CurrentAccount).count();
        long savingCount = accounts.stream().filter(a -> a instanceof SavingAccount).count();
        double totalBalance = accounts.stream().mapToDouble(BankAccount::getBalance).sum();
        long customerCount = customerRepository.count();

        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalCustomers(customerCount);
        stats.setTotalAccounts(accounts.size());
        stats.setCurrentAccounts(currentCount);
        stats.setSavingAccounts(savingCount);
        stats.setTotalBalance(totalBalance);
        return stats;
    }

    private BankAccountDTO mapAccount(BankAccount bankAccount) {
        if (bankAccount instanceof SavingAccount)
            return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);
        return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
    }
}