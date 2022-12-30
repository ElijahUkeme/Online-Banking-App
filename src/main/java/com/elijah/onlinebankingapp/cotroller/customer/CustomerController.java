package com.elijah.onlinebankingapp.cotroller.customer;

import com.elijah.onlinebankingapp.dto.customer.CustomerDto;
import com.elijah.onlinebankingapp.dto.customer.CustomerUpdateDto;
import com.elijah.onlinebankingapp.dto.customer.SignInDto;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.model.customer.Customer;
import com.elijah.onlinebankingapp.model.image.ImageModel;
import com.elijah.onlinebankingapp.response.customer.SignInResponse;
import com.elijah.onlinebankingapp.response.customer.SignUpResponse;
import com.elijah.onlinebankingapp.service.customer.CustomerService;
import com.elijah.onlinebankingapp.service.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class CustomerController {

    private ImageService imageService;

    @Autowired
    private CustomerService customerService;

    public CustomerController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/customer/register")
    public ResponseEntity<SignUpResponse>uploadUserInfo(@RequestParam("file")MultipartFile file, @RequestPart("customer") CustomerDto customerDto) throws Exception {
        ImageModel imageModel = imageService.saveProfileImage(file);
        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(imageModel.getId())
                .toUriString();
        customerService.signUpCustomer(customerDto,downloadUrl);

        return  new ResponseEntity<>(new SignUpResponse(true,downloadUrl),HttpStatus.CREATED);
    }
    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("imageId") String imageId) throws Exception {
        ImageModel imageModel = imageService.getImage(imageId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageModel.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"imageModel; filename=\""+imageModel.getFileName()
                        +"\"")
                .body(new ByteArrayResource(imageModel.getData()));

    }
    @PostMapping("/customer/getByEmail")
    public ResponseEntity<Customer> getCustomerByEmail(@RequestParam("email") String email) throws DataNotFoundException {
        return new ResponseEntity<>(customerService.getCustomerByEmail(email),HttpStatus.OK);
    }
    @PostMapping("/customer/getByPhone")
    public ResponseEntity<Customer> getCustomerByPhone(@RequestParam("phone") String phoneNumber) throws DataNotFoundException {
        return new ResponseEntity<>(customerService.getCustomerByPhoneNumber(phoneNumber),HttpStatus.OK);
    }
    @PostMapping("/customer/signIn")
    public ResponseEntity<SignInResponse> signInCustomer(@RequestBody SignInDto signInDto) throws NoSuchAlgorithmException, DataNotFoundException {
        customerService.signInUser(signInDto);
        return new ResponseEntity<>(new SignInResponse(),HttpStatus.OK);
    }
    @PutMapping("/customer/info/update")
    public ResponseEntity<SignUpResponse> updateCustomerInfo(@RequestBody CustomerUpdateDto customerUpdateDto, @RequestParam("email") String email) throws NoSuchAlgorithmException, DataNotFoundException {
        customerService.updateCustomerInformation(customerUpdateDto,email);
        return new ResponseEntity<>(new SignUpResponse(true,"Customer Information Updated Successfully"),HttpStatus.OK);
    }
    @GetMapping("/customers/all")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return new ResponseEntity<>(customerService.getAllCustomers(),HttpStatus.OK);
    }
}
