package com.leron.api.validator.user;

import com.leron.api.model.DTO.user.MemberRequest;
import com.leron.api.model.DTO.user.MemberResponse;
import com.leron.api.model.entities.Member;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class MemberValidator {
    public static void validateUser(List<MemberRequest> userRequest, List<Member> members) throws ApplicationBusinessException {
        AtomicReference<Boolean> isEmpty = new AtomicReference<>(Boolean.FALSE);

        userRequest.forEach(user -> {
            if (user.getName() == null || user.getName().isEmpty()) {
                isEmpty.set(Boolean.TRUE);
            }
        });

        if (isEmpty.get()) {
            throw new ApplicationBusinessException("ERROR", "NOME_IS_EMPTY");
        }

        if (hasDuplicateNames(userRequest)) {
            throw new ApplicationBusinessException("ERROR", "DUPLICATE_NAMES_FOUND");
        }

        if (hasSameNameAsExistingMember(userRequest, members)) {
            throw new ApplicationBusinessException("ERROR", "NAME_ALREADY_EXISTS");
        }
    }

    private static boolean hasDuplicateNames(List<MemberRequest> userRequest) {
        Set<String> namesSet = new HashSet<>();
        for (MemberRequest user : userRequest) {
            if (!namesSet.add(user.getName())) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSameNameAsExistingMember(List<MemberRequest> userRequest, List<Member> members) {
        Set<String> memberNames = members.stream()
                .map(member -> member.getName().replaceAll("\\s+", "").toLowerCase())
                .collect(Collectors.toSet());

        for (MemberRequest user : userRequest) {
            String userName = user.getName().replaceAll("\\s+", "").toLowerCase();
            if (memberNames.contains(userName)) {
                return true;
            }
        }
        return false;
    }

    public static void validateMember(MemberResponse user, List<Member> members, Member currentMember) throws ApplicationBusinessException {
        AtomicReference<Boolean> colorIsEmpty = new AtomicReference<>(Boolean.FALSE);
        AtomicReference<Boolean> isSAME = new AtomicReference<>(Boolean.FALSE);

        members.forEach(member -> {
            String userName = user.getName().replace(" ", "");
            String memberName = member.getName().replace(" ", "");
            if (memberName.equalsIgnoreCase(userName)) {
                isSAME.set(Boolean.TRUE);
            }
        });

        if (Objects.isNull(user.getColor())) {
            throw new ApplicationBusinessException("ERROR", "COLOR_IS_EMPTY");
        }

        String status = "";

        if(user.getStatus() == 1L) {
            status = "ACTIVE";
        }

        if(user.getStatus() == 2L) {
            status = "INACTIVE";
        }

        if (isSAME.get() && Objects.equals(currentMember.getColor(), user.getColor()) && Objects.equals(currentMember.getStatus(), status)) {
            throw new ApplicationBusinessException("ERROR", "NAME_ALREADY_EXISTS");
        }
    }
}
