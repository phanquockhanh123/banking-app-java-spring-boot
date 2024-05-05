package com.example.bankingapp.service.impl;

import com.example.bankingapp.dto.AccountInfo;
import com.example.bankingapp.dto.BankResponse;
import com.example.bankingapp.dto.EmailDetails;
import com.example.bankingapp.dto.UserRequest;
import com.example.bankingapp.entity.UserEntity;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.EmailService;
import com.example.bankingapp.service.UserService;
import com.example.bankingapp.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;
    /**
     * Create account - save a new user in db
     * check if user has db
     * @param userRequest
     * @return
     */
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNTS_EXSITS_CODE)
                    .responseMessage(AccountUtils.ACCOUNTS_EXSITS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        UserEntity newUser = UserEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        UserEntity saveUser = userRepository.save(newUser);

        // send mail to alert user
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Welcome to LG. You best!\nYour account Details:\n"
                        + "Account name: " + saveUser.getLastName() + " Email: " + saveUser.getEmail()
                        + " Account number: " + saveUser.getAccountNumber())

                .build();

        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNTS_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNTS_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(saveUser.getAccountBalance())
                        .accountNumber(saveUser.getAccountNumber())
                        .accountName(saveUser.getFirstName() + " " + saveUser.getLastName() + " " + saveUser.getOtherName())
                        .build())
                .build();
    }

    // balance Enquiry, name Enquiry, credit, debit, transfer

}
