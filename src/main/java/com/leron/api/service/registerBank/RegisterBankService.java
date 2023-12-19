package com.leron.api.service.registerBank;

import com.leron.api.mapper.bank.RegisterBankMapper;
import com.leron.api.model.DTO.registerBank.*;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.registerBank.RegisterBankValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RegisterBankService {
    @Autowired
    private RegisterBankRepository bankRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private RegisterBankMapper bankMapper;

    private final MemberRepository memberRepository;

    private final PointsRepository pointsRepository;

    private final BankMovementRepository bankMovementRepository;

    private final EntranceRepository entranceRepository;

    private final ExpenseRepository expenseRepository;

    public RegisterBankService(RegisterBankRepository bankRepository, AccountRepository accountRepository, CardRepository cardRepository, RegisterBankMapper bankMapper, MemberRepository memberRepository, PointsRepository pointsRepository, BankMovementRepository bankMovementRepository, EntranceRepository entranceRepository, ExpenseRepository expenseRepository) {
        this.bankRepository = bankRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.bankMapper = bankMapper;
        this.memberRepository = memberRepository;
        this.pointsRepository = pointsRepository;
        this.bankMovementRepository = bankMovementRepository;
        this.entranceRepository = entranceRepository;
        this.expenseRepository = expenseRepository;
    }

    public DataResponse<RegisterBankResponse> createBank(RegisterBankRequest requestDTO, String locale, String authorization) throws ApplicationBusinessException {
        List<Member> entities = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(requestDTO.getUserAuthId());
        List<Score> programs = pointsRepository.findAllByUserAuthIdAndDeletedFalse(requestDTO.getUserAuthId());
        List<Bank> bankData = bankRepository.findByUserAuthId(requestDTO.getUserAuthId());
        RegisterBankValidator.validate(requestDTO, bankData);

        Bank bank = bankMapper.toEntity(requestDTO);

        bank = saveBank(bank);
        for (Account account : bank.getAccounts()) {
            Bank bankCopy = new Bank();
            bankCopy.setId(bank.getId());
            bankCopy.setName(bank.getName());
            bankCopy.setUserAuthId(bank.getUserAuthId());
            account.setBank(bankCopy);
        }

        bank.setAccounts(saveAccount(bank.getAccounts()));
        for (Account account : bank.getAccounts()) {
            for (Card card : account.getCards()) {
                card.setAccount(account);
            }
            saveCard(account.getCards());
        }

        return bankMapper.toResponseDTO(bank, entities, programs);
    }

    private Bank saveBank(Bank bank) {
        return bankRepository.save(bank);
    }

    private List<Account> saveAccount(List<Account> account) {
        return accountRepository.saveAll(account);
    }

    private void saveCard(List<Card> card) {
        cardRepository.saveAll(card);
    }

    public DataListResponse<RegisterBankResponse> list(Long userAuthId) {
        List<Bank> bankData = bankRepository.findByUserAuthId(userAuthId);
        List<Member> entities = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);
        List<Score> programs = pointsRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);

        return bankMapper.toResponseDTOList(bankData, entities, programs);
    }

    public DataResponse<RegisterBankResponse> getBankByIdAndUserAuthId(Long userAuthId, Long id) {
        Bank bank = bankRepository.findBankByUserAuthIdAndId(userAuthId, id);
        List<Member> entities = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);
        List<Score> programs = pointsRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);

        if (bank != null) {
            return bankMapper.toResponseDTO(bank, entities, programs);
        } else {
            return null;
        }
    }

    public DataResponse<CardResponse> updateCard(Long userAuthId, Long cardId, CardRequest cardRequest) throws ApplicationBusinessException {
        Card card = cardRepository.findCardByIdAndUserAuthId(cardId, userAuthId);
        List<Member> entities = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);

        RegisterBankValidator.validateCard(card);

        card.setCurrency(cardRequest.getCurrency());
        card.setPoint(cardRequest.getPoints());
        card.setProgram(cardRequest.getProgram());
        cardRepository.save(card);

        return bankMapper.toResponseDTOCard(card, entities);
    }

    public DataResponse<RegisterBankResponse> delete(Long bankId) {
        DataResponse<RegisterBankResponse> response = new DataResponse<>();
        Bank bank = bankRepository.findById(bankId).orElse(null);
        if (bank != null) {

            List<BankMovement> bankMovementList = bankMovementRepository.findAllByUserAuthIdAndBankId(bank.getUserAuthId(),bankId);
            if(!bankMovementList.isEmpty()) {
                bankMovementList.forEach(bankMovement -> {
                    bankMovement.setDeleted(true);
                });
                bankMovementRepository.saveAll(bankMovementList);
            }

            List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndBankId(bank.getUserAuthId(),bankId);
            if(!entrances.isEmpty()) {
                entrances.forEach(entrance -> {
                    entrance.setDeleted(true);
                });
                entranceRepository.saveAll(entrances);
            }

            List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndBankId(bank.getUserAuthId(), bankId);
            if(!expenses.isEmpty()) {
                expenses.forEach(expense -> {
                    expense.setDeleted(true);
                });
                expenseRepository.saveAll(expenses);
            }


            bank.setDeleted(true);
            bank.getAccounts().forEach(account -> {
                account.setDeleted(true);
                account.getCards().forEach(card -> {
                    card.setDeleted(true);
                });
                cardRepository.saveAll(account.getCards());
            });
            accountRepository.saveAll(bank.getAccounts());
            bankRepository.save(bank);
        }

        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }

    public DataResponse<RegisterBankResponse> deleteAccount(Long accountId) {
        DataResponse<RegisterBankResponse> response = new DataResponse<>();
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            account.setDeleted(true);
            account.getCards().forEach(card -> {
                card.setDeleted(true);

            });
            cardRepository.saveAll(account.getCards());
            accountRepository.save(account);
        }

        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }

    public DataResponse<RegisterBankResponse> deleteCard(Long cardId) {
        DataResponse<RegisterBankResponse> response = new DataResponse<>();
        Card card = cardRepository.findById(cardId).orElse(null);
        if (card != null) {
            card.setDeleted(true);
            cardRepository.save(card);
        }

        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }


    public DataResponse<RegisterBankResponse> editBank(Bank registerBankRequest) throws ApplicationBusinessException {
        DataResponse<RegisterBankResponse> response = new DataResponse<>();
        Bank bank = bankRepository.findById(registerBankRequest.getId()).orElse(null);
        Long userId = registerBankRequest.getUserAuthId();

        RegisterBankValidator.validateEditBank(registerBankRequest, bank);

        Bank bankMap = bankMapper.entityToEntity(registerBankRequest);
        bank.setName(registerBankRequest.getName());
        bank.setAccounts(bankMap.getAccounts());

        Bank bankSave = saveBank(bank);
        for (Account account : bank.getAccounts()) {
            Bank bankCopy = new Bank();
            bankCopy.setId(bankSave.getId());
            bankCopy.setName(bankSave.getName());
            bankCopy.setUserAuthId(userId);
            account.setBank(bankCopy);
        }

        bank.setAccounts(saveAccount(bankSave.getAccounts()));
        for (Account account : bank.getAccounts()) {
            for (Card card : account.getCards()) {
                card.setAccount(account);
                card.setUserAuthId(userId);
            }
            saveCard(account.getCards());
        }

        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }

    public DataResponse<AccountResponse> editAccount(AccountResponse accountRequest) throws ApplicationBusinessException {
        DataResponse<AccountResponse> response = new DataResponse<>();
        Account account = accountRepository.findById(accountRequest.getId()).orElse(null);

        RegisterBankValidator.validateEditAccount(accountRequest, account);
        if (account != null) {
            account.setMemberId(accountRequest.getOwner());
            account.setValue(accountRequest.getValue());
            if (accountRequest.getStatus().equalsIgnoreCase("1")) {
                account.setStatus("ACTIVE");
            }

            if (accountRequest.getStatus().equalsIgnoreCase("2")) {
                account.setStatus("INACTIVE");

                for (Card card : account.getCards()) {
                    card.setStatus("INACTIVE");
                }
                saveCard(account.getCards());

            }

            account.setAccountNumber(accountRequest.getAccountNumber());
            accountRepository.save(account);
        }


        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }

    public DataResponse<CardResponse> editCard(CardResponse cardRequest) throws ApplicationBusinessException {
        DataResponse<CardResponse> response = new DataResponse<>();
        Card card = cardRepository.findById(cardRequest.getId()).orElse(null);

        RegisterBankValidator.validateEditCard(cardRequest, card);

        if (card != null) {
            card.setName(cardRequest.getName());
            if (cardRequest.getStatus().equalsIgnoreCase("1")) {
                card.setStatus("ACTIVE");
            }

            if (cardRequest.getStatus().equalsIgnoreCase("2")) {
                card.setStatus("INACTIVE");
            }

            if (Objects.nonNull(cardRequest.getDueDate())) {
                card.setDueDate(cardRequest.getDueDate());
            }

            if (Objects.nonNull(cardRequest.getClosingDate())) {
                card.setDueDate(cardRequest.getClosingDate());
            }

            if (Objects.nonNull(cardRequest.getProgram().getId())) {
                card.setProgram(cardRequest.getProgram().getId());
            }

            if (Objects.nonNull(cardRequest.getPoint())) {
                card.setPoint(cardRequest.getPoint());
            }

            if (Objects.nonNull(cardRequest.getCurrency())) {
                card.setCurrency(cardRequest.getCurrency());
            }

            cardRepository.save(card);
        }

        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }
}
