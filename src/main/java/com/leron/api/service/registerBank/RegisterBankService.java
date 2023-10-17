package com.leron.api.service.registerBank;

import com.leron.api.mapper.bank.RegisterBankMapper;
import com.leron.api.mapper.member.MemeberMapper;
import com.leron.api.model.DTO.registerBank.RegisterBankRequest;
import com.leron.api.model.DTO.registerBank.RegisterBankResponse;
import com.leron.api.model.DTO.user.MemberResponse;
import com.leron.api.model.entities.Account;
import com.leron.api.model.entities.Bank;
import com.leron.api.model.entities.Card;
import com.leron.api.repository.AccountRepository;
import com.leron.api.repository.CardRepository;
import com.leron.api.repository.RegisterBankRepository;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.catalog.Catalog;
import java.util.List;
import java.util.stream.Collectors;

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

    public DataResponse<RegisterBankResponse> createBank(RegisterBankRequest requestDTO, String locale, String authorization) {
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

        return bankMapper.toResponseDTO(bank);
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
        return bankMapper.toResponseDTOList(bankData);
    }

    public DataResponse<RegisterBankResponse> getBankByIdAndUserAuthId(Long userAuthId, Long id) {
        Bank bank = bankRepository.findBankByUserAuthIdAndId(userAuthId, id);
        if (bank != null) {
            return bankMapper.toResponseDTO(bank);
        } else {
            return null;
        }
    }
}
