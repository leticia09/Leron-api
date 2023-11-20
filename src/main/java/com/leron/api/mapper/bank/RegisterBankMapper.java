package com.leron.api.mapper.bank;

import com.leron.api.model.DTO.registerBank.*;
import com.leron.api.model.entities.*;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RegisterBankMapper {

    public DataResponse<RegisterBankResponse> toResponseDTO(Bank bank, List<Member> members, List<Score> programs) {
        DataResponse<RegisterBankResponse> response = new DataResponse<>();
        RegisterBankResponse registerBankResponse = new RegisterBankResponse();

        registerBankResponse.setId(bank.getId());
        registerBankResponse.setName(bank.getName());
        registerBankResponse.setUserAuthId(bank.getUserAuthId());

        List<Account> accounts = bank.getAccounts();
        List<AccountResponse> accountResponses = new ArrayList<>();
        for (Account account : accounts) {
            AccountResponse accountResponse = mapAccountToResponse(account, members, programs);
            accountResponses.add(accountResponse);
        }
        registerBankResponse.setAccounts(accountResponses);

        response.setData(registerBankResponse);
        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }

    public AccountResponse mapAccountToResponse(Account account, List<Member> members, List<Score> programs) {
        AccountResponse accountResponse = new AccountResponse();
        if(Objects.nonNull(account.getId())) {
            accountResponse.setId(account.getId());
        }
        accountResponse.setStatus(account.getStatus());
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setOwner(account.getOwner());

        List<Card> cards = account.getCards();
        List<CardResponse> cardResponses = getCardResponses(cards, members, programs);
        accountResponse.setCards(cardResponses);
        return accountResponse;
    }

    private static List<CardResponse> getCardResponses(List<Card> cards, List<Member> members, List<Score> programs) {
        List<CardResponse> cardResponses = new ArrayList<>();
        for (Card card : cards) {
            CardResponse cardResponse = new CardResponse();
            if(Objects.nonNull(card.getId())) {
                cardResponse.setId(card.getId());
            }
            cardResponse.setStatus(card.getStatus());
            cardResponse.setName(card.getName());
            cardResponse.setFinalNumber(card.getFinalNumber());
            cardResponse.setModality(card.getModality());
            cardResponse.setClosingDate(card.getClosingDate());
            cardResponse.setDueDate(card.getDueDate());
            cardResponse.setPoint(card.getPoint());
            cardResponse.setCurrency(card.getCurrency());

            Optional<Member> ownerMember = members.stream()
                    .filter(member -> member.getId().equals(card.getOwner()))
                    .findFirst();
            ownerMember.ifPresent(cardResponse::setOwner);

            Optional<Score> ownerScore = programs.stream()
                    .filter(program -> program.getId().equals(card.getProgram()))
                    .findFirst();
            ownerScore.ifPresent(cardResponse::setProgram);

            cardResponses.add(cardResponse);
        }
        return cardResponses;
    }

    public DataListResponse<RegisterBankResponse> toResponseDTOList(List<Bank> banks, List<Member> members, List<Score> programs) {
        DataListResponse<RegisterBankResponse> response = new DataListResponse<>();
        List<RegisterBankResponse> responseList = new ArrayList<>();

        for (Bank bank : banks) {
            RegisterBankResponse dto = new RegisterBankResponse();

            dto.setName(bank.getName());
            dto.setId(bank.getId());
            dto.setUserAuthId(bank.getUserAuthId());

            List<Account> accounts = bank.getAccounts();
            List<AccountResponse> accountResponses = new ArrayList<>();
            for (Account account : accounts) {
                AccountResponse accountResponse = mapAccountToResponse(account, members, programs);
                accountResponses.add(accountResponse);
            }
            dto.setAccounts(accountResponses);

            responseList.add(dto);
        }
        response.setData(responseList);
        return response;
    }

    public Bank toEntity(RegisterBankRequest requestDTO) {
        Bank bank = new Bank();
        bank.setName(requestDTO.getName().substring(0, 1).toUpperCase() + requestDTO.getName().substring(1).toLowerCase());
        bank.setUserAuthId(requestDTO.getUserAuthId());
        bank.setDeleted(false);
        bank.setCreatedIn(new Date());

        List<Account> accounts = new ArrayList<>();
        for (AccountRequest accountRequest : requestDTO.getAccounts()) {
            Account account = new Account();
            account.setStatus("ACTIVE");
            account.setAccountNumber(accountRequest.getAccountNumber());
            account.setOwner(accountRequest.getOwner());
            account.setUserAuthId(bank.getUserAuthId());
            account.setValue(accountRequest.getValue());
            account.setCurrency(accountRequest.getCurrency());
            account.setDeleted(false);
            account.setCreatedIn(new Date());

            List<Card> cards = getCards(accountRequest, bank.getUserAuthId());
            account.setCards(cards);
            accounts.add(account);
        }
        bank.setAccounts(accounts);

        return bank;
    }

    private static List<Card> getCards(AccountRequest accountRequest, Long authId) {
        List<Card> cards = new ArrayList<>();
        for (CardRequest cardRequest : accountRequest.getCards()) {
            Card card = new Card();
            card.setStatus("ACTIVE");
            card.setUserAuthId(authId);
            card.setName(cardRequest.getName().substring(0, 1).toUpperCase() + cardRequest.getName().substring(1).toLowerCase());
            card.setFinalNumber(cardRequest.getFinalNumber());
            card.setModality(cardRequest.getModality());
            card.setClosingDate(cardRequest.getClosingDate());
            card.setDueDate(cardRequest.getDueDate());
            card.setOwner(cardRequest.getOwner());
            card.setPoint(cardRequest.getPoints());
            card.setProgram(cardRequest.getProgram());
            card.setCurrency(cardRequest.getCurrency());
            card.setCreatedIn(new Date());
            card.setDeleted(false);
            cards.add(card);

        }
        return cards;
    }

    public DataResponse<CardResponse> toResponseDTOCard(Card card, List<Member> members) {
        DataResponse<CardResponse> response = new DataResponse<>();
        CardResponse cardResponse = new CardResponse();
        cardResponse.setPoint(card.getPoint());
        cardResponse.setStatus(card.getStatus());
        cardResponse.setId(card.getId());
        cardResponse.setFinalNumber(card.getFinalNumber());
        cardResponse.setModality(card.getModality());
        cardResponse.setClosingDate(card.getClosingDate());
        cardResponse.setName(card.getName());
        cardResponse.setCurrency(card.getCurrency());

        Optional<Member> ownerMember = members.stream()
                .filter(member -> member.getId().equals(card.getOwner()))
                .findFirst();
        ownerMember.ifPresent(cardResponse::setOwner);

        cardResponse.setPoint(card.getPoint());
        cardResponse.setDueDate(card.getDueDate());

        return response;
    }
}
