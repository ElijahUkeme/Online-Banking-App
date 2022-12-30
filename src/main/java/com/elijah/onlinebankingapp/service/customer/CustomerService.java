package com.elijah.onlinebankingapp.service.customer;

import com.elijah.onlinebankingapp.dto.customer.CustomerDto;
import com.elijah.onlinebankingapp.dto.customer.CustomerUpdateDto;
import com.elijah.onlinebankingapp.dto.customer.SignInDto;
import com.elijah.onlinebankingapp.exception.DataAlreadyExistException;
import com.elijah.onlinebankingapp.exception.DataNotAcceptableException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.model.customer.Customer;
import com.elijah.onlinebankingapp.model.token.CustomerAuthenticationToken;
import com.elijah.onlinebankingapp.repository.customer.CustomerRepository;
import com.elijah.onlinebankingapp.response.customer.SignInResponse;
import com.elijah.onlinebankingapp.service.token.CustomerTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerTokenService customerTokenService;

    @Transactional
    public Customer signUpCustomer(CustomerDto customerDto, String profileImageUrl) throws DataAlreadyExistException, NoSuchAlgorithmException, DataNotAcceptableException {
        if (!checkInputFields(customerDto)){
            throw new DataNotAcceptableException("There is an input field that is missing, try and fill all fields");
        }

        if (Objects.nonNull(customerRepository.findByEmail(customerDto.getEmail()))){
            throw new DataAlreadyExistException("Email Address Already Taken By Another Customer");
        }
        if (Objects.nonNull(customerRepository.findByPhoneNumber(customerDto.getPhoneNumber()))){
            throw new DataAlreadyExistException("Phone Number Already been used by another Customer");
        }
        String encryptedCustomerPassword = customerDto.getPassword();
        try {
            encryptedCustomerPassword = hashPassword(customerDto.getPassword());
        }catch (Exception e) {
            e.printStackTrace();
        }
            Customer customer = new Customer();
            customer.setAddress(customerDto.getAddress());
            customer.setDateOfBirth(customerDto.getDateOfBirth());
            customer.setEmail(customerDto.getEmail());
            customer.setGender(customerDto.getGender());
            customer.setLocalGovernment(customerDto.getLocalGovernment());
            customer.setMaritalStatus(customerDto.getMaritalStatus());
            customer.setName(customerDto.getName());
            customer.setOccupation(customerDto.getOccupation());
            customer.setPassword(encryptedCustomerPassword);
            customer.setPhoneNumber(customerDto.getPhoneNumber());
            customer.setStateOfOrigin(customerDto.getStateOfOrigin());
            customer.setProfileImage(profileImageUrl);
            customerRepository.save(customer);
            final CustomerAuthenticationToken authenticationToken = new CustomerAuthenticationToken(customer);
            customerTokenService.saveCustomerAuthenticationToken(authenticationToken);
            return customer;

    }

        public Customer getCustomerByEmail(String email) throws DataNotFoundException {
        Customer customer = customerRepository.findByEmail(email);
        if (Objects.isNull(customer)){
            throw new DataNotFoundException("There is no customer with this emil address");
        }
        return customer;
        }
        public Customer getCustomerByPhoneNumber(String phoneNumber) throws DataNotFoundException {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
            if (Objects.isNull(customer)){
                throw new DataNotFoundException("There is no customer with this phone Number");
            }
            return customer;
            }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toLowerCase().substring(0,12);
        return hash;
    }
    public SignInResponse signInUser(SignInDto signInDto) throws DataNotFoundException, NoSuchAlgorithmException {
        Customer customer = customerRepository.findByEmail(signInDto.getEmail());
            if (Objects.isNull(customer)){
                throw new DataNotFoundException("Invalid Email or password");
            }
            if (!customer.getPassword().equals(hashPassword(signInDto.getPassword()))){
                throw new DataNotFoundException("Invalid Email or Password");
            }
            final CustomerAuthenticationToken authenticationToken = customerTokenService.getTokenByCustomer(customer);
            if (Objects.isNull(authenticationToken)){
                throw new DataNotFoundException("There is no token for this Customer");
            }
            return new SignInResponse("Success",authenticationToken.getToken());
        }
        public Customer updateCustomerInformation(CustomerUpdateDto customerUpdateDto,String email) throws DataNotFoundException, NoSuchAlgorithmException {
        Customer customer = customerRepository.findByEmail(email);
        if (Objects.isNull(customer)){
            throw new DataNotFoundException("Invalid Email Address");
        }
        if (Objects.nonNull(customerUpdateDto.getAddress())&& !"".equals(customerUpdateDto.getAddress())){
            customer.setAddress(customerUpdateDto.getAddress());
        }if (Objects.nonNull(customerUpdateDto.getDateOfBirth())&& !"".equals(customerUpdateDto.getDateOfBirth())){
            customer.setDateOfBirth(customerUpdateDto.getDateOfBirth());
            }if (Objects.nonNull(customerUpdateDto.getEmail())&& !"".equals(customerUpdateDto.getEmail())){
            customer.setEmail(customerUpdateDto.getEmail());
            }if (Objects.nonNull(customerUpdateDto.getMaritalStatus())&& !"".equals(customerUpdateDto.getMaritalStatus())){
            customer.setMaritalStatus(customerUpdateDto.getMaritalStatus());
            }if (Objects.nonNull(customerUpdateDto.getName())&& !"".equals(customerUpdateDto.getName())){
            customer.setName(customerUpdateDto.getName());
            }if (Objects.nonNull(customerUpdateDto.getOccupation())&& !"".equals(customerUpdateDto.getOccupation())){
            customer.setOccupation(customerUpdateDto.getOccupation());
            }if (Objects.nonNull(customerUpdateDto.getPassword())&& !"".equals(customerUpdateDto.getPassword())){
            customer.setPassword(hashPassword(customerUpdateDto.getPassword()));
            }if (Objects.nonNull(customerUpdateDto.getPhoneNumber())&& !"".equals(customerUpdateDto.getPhoneNumber())){
            customer.setPhoneNumber(customerUpdateDto.getPhoneNumber());
            }
        return customerRepository.save(customer);
        }
        public boolean checkInputFields(CustomerDto customerDto) {
            return !customerDto.getEmail().isEmpty() && !customerDto.getPassword().isEmpty() && !customerDto.getAddress().isEmpty() &&
                    !customerDto.getDateOfBirth().toString().isEmpty() && !customerDto.getGender().isEmpty() && !customerDto.getLocalGovernment().isEmpty() &&
                    !customerDto.getMaritalStatus().isEmpty() && !customerDto.getName().isEmpty() && !customerDto.getOccupation().isEmpty() &&
                    !customerDto.getPhoneNumber().isEmpty() && !customerDto.getStateOfOrigin().isEmpty();
        }

        public List<Customer> getAllCustomers(){
        List<Customer> customerList = customerRepository.findAll();
        return customerList;
        }
    }

