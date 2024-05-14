package com.example.bankingapp.service.impl;

import com.example.bankingapp.dto.*;
import com.example.bankingapp.entity.UserEntity;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.EmailService;
import com.example.bankingapp.service.TransactionService;
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

    @Autowired
     TransactionService transactionService;
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

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        // check if the provided account number exists in the db

        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNTS_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNTS_NOT_EXISTS_CODE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNTS_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNTS_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if (!isAccountExists) {
            return AccountUtils.ACCOUNTS_NOT_EXISTS_CODE_MESSAGE;
        }

        UserEntity foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
    }


    // balance Enquiry, name Enquiry, credit, debit, transfer

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNTS_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNTS_NOT_EXISTS_CODE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));

        userRepository.save(userToCredit);

        // Save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNTS_CREDIT_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNTS_CREDIT_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNTS_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNTS_NOT_EXISTS_CODE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        if (userToDebit.getAccountBalance().compareTo(request.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNTS_COMPARE_VALUE)
                    .responseMessage(AccountUtils.ACCOUNTS_COMPARE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));

        userRepository.save(userToDebit);

        // Save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("DEBIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNTS_DEBIT_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNTS_DEBIT_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(userToDebit.getAccountBalance())
                        .accountNumber(userToDebit.getAccountNumber())
                        .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                        .build())
                .build();
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        // get account to debit
        // check amount debiting not more amount => debit account
        // debit
        try {
            boolean isSourceAccountExists = userRepository.existsByAccountNumber(request.getSourceAccountNumber());

            boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
            if (!isSourceAccountExists || !isDestinationAccountExists) {
                return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNTS_NOT_EXISTS_CODE)
                        .responseMessage(AccountUtils.ACCOUNTS_NOT_EXISTS_CODE_MESSAGE)
                        .accountInfo(null)
                        .build();
            }

            UserEntity sourceUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
            if (sourceUser.getAccountBalance().compareTo(BigDecimal.valueOf(request.getAmount())) < 0) {
                return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNTS_COMPARE_VALUE)
                        .responseMessage(AccountUtils.ACCOUNTS_COMPARE_MESSAGE)
                        .accountInfo(null)
                        .build();
            }

            sourceUser.setAccountBalance(sourceUser.getAccountBalance().subtract(BigDecimal.valueOf(request.getAmount())));
            String sourceUsername = sourceUser.getFirstName() + " " + sourceUser.getLastName() + " " + sourceUser.getOtherName();
            System.out.println("source user: {}" + sourceUser.getAccountBalance());
            userRepository.save(sourceUser);

            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("DEBIT ALERT")
                    .recipient(sourceUser.getEmail())
                    .messageBody("The sum of debit account " + request.getAmount() + " has been deducted from your account!Your current balance is " + sourceUser.getAccountBalance())
                    .build();

            emailService.sendEmailAlert(debitAlert);

            UserEntity destinationUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
            String destinationUsername = destinationUser.getFirstName() + " " + destinationUser.getLastName() + " " + destinationUser.getOtherName();
            destinationUser.setAccountBalance(destinationUser.getAccountBalance().add(BigDecimal.valueOf(request.getAmount())));
            System.out.println("destination user: {}" + destinationUser.getAccountBalance());
            userRepository.save(destinationUser);

            EmailDetails creditAlert = EmailDetails.builder()
                    .subject("CREDIT ALERT")
                    .recipient(destinationUser.getEmail())
                    .messageBody("The sum of credit account " + request.getAmount() + " has been sent to your account from + " + sourceUsername + " !Your current balance is " + destinationUser.getAccountBalance())
                    .build();
            emailService.sendEmailAlert(creditAlert);

            // Save transaction
            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(sourceUser.getAccountNumber())
                    .transactionType("CREDIT")
                    .amount(BigDecimal.valueOf(request.getAmount()))
                    .build();

            transactionService.saveTransaction(transactionDto);
        } catch (Exception e) {
            throw new RuntimeException("Message" + e);
        }

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }
}
