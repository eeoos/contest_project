package core.contest5.member.service;

import core.contest5.member.domain.memberinfo.MemberField;
import core.contest5.member.repository.MemberFieldRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberFieldService {
    private final MemberFieldRepository memberFieldRepository;

    public MemberFieldService(MemberFieldRepository memberFieldRepository) {
        this.memberFieldRepository = memberFieldRepository;
    }

    public MemberField findOrCreateMemberField(String fieldName) {
        // 기존 MemberField 조회
        return memberFieldRepository.findByFieldName(fieldName)
                .orElseGet(() -> {
                    // 존재하지 않으면 새로 생성
                    MemberField newMemberField = new MemberField(fieldName);
                    log.info("orElseGet -> id of the newMemberField = {}", newMemberField.getId());
                    return memberFieldRepository.save(newMemberField);
                });
    }
}