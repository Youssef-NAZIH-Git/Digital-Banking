package nazih.youssef.backend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nazih.youssef.backend.dtos.CustomerDTO;
import nazih.youssef.backend.entities.Customer;
import nazih.youssef.backend.exceptions.CustomerNotFoundException;
import nazih.youssef.backend.mappers.BankAccountMapperImpl;
import nazih.youssef.backend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService{
    private CustomerRepository customerRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer: [ {} ]", customerDTO.toString());
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }



    @Override
    public List<CustomerDTO> listCustomers() {
        log.info("Fetching all customers");
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(dtoMapper::fromCustomer)
                .collect(Collectors.toList());
    }



    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        log.info("Fetching customer info with keyword in name: [ {} ]", keyword);
        List<Customer> customers=customerRepository.searchCustomer(keyword);
        return customers.stream().map(c -> dtoMapper.fromCustomer(c)).collect(Collectors.toList());
    }



    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        log.info("Fetching customer info with ID: [ {} ]", customerId.toString());
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not found"));
        return dtoMapper.fromCustomer(customer);
    }



    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating new Customer: [{}]", customerDTO.toString());
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }



    @Override
    public void deleteCustomer(Long customerId){
        log.info("Deleting Customer with ID: [ {} ]", customerId.toString());
        customerRepository.deleteById(customerId);
    }
}
