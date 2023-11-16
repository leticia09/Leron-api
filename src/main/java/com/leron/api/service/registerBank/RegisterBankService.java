package com.leron.api.service.registerBank;

import com.leron.api.mapper.bank.RegisterBankMapper;
import com.leron.api.model.DTO.registerBank.CardRequest;
import com.leron.api.model.DTO.registerBank.CardResponse;
import com.leron.api.model.DTO.registerBank.RegisterBankRequest;
import com.leron.api.model.DTO.registerBank.RegisterBankResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.registerBank.RegisterBankValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public RegisterBankService(RegisterBankRepository bankRepository, AccountRepository accountRepository, CardRepository cardRepository, RegisterBankMapper bankMapper, MemberRepository memberRepository, PointsRepository pointsRepository) {
        this.bankRepository = bankRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.bankMapper = bankMapper;
        this.memberRepository = memberRepository;
        this.pointsRepository = pointsRepository;
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
}
