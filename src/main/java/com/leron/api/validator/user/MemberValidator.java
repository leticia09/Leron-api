package com.leron.api.validator.user;

import com.leron.api.model.DTO.user.MemberRequest;
import com.leron.api.model.entities.Member;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class MemberValidator {
    public static void validateUser(List<MemberRequest> userRequest, List<Member> members) throws ApplicationBusinessException {
        AtomicReference<Boolean> isEmpty = new AtomicReference<>(Boolean.FALSE);
        AtomicReference<Boolean> isSAME = new AtomicReference<>(Boolean.FALSE);

        userRequest.forEach(user -> {
            if (user.getName() == null || user.getName().isEmpty()) {
                isEmpty.set(Boolean.TRUE);
            }

            members.forEach(member -> {
                String userName = user.getName().replace(" ", "");
                String memberName = member.getName().replace(" ", "");
                if(memberName.equalsIgnoreCase(userName)){
                    isSAME.set(Boolean.TRUE);
                }
            });
        });

        if (isEmpty.get()) {
            throw new ApplicationBusinessException("ERROR", "NOME_IS_EMPTY");
        }

        if(isSAME.get()) {
            throw new ApplicationBusinessException("ERROR", "NAME_ALREADY_EXISTS");
        }
    }
}
