package core.contest5.member.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import core.contest5.admin.domain.SuspensionStatus;
import core.contest5.member.domain.memberinfo.*;
import core.contest5.member.service.MemberDomain;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record MemberResponse(
        Long id,
        String email,
        String name,
        String profileImage,
        MemberRole role,
        String memberField,
        String memberDuty,
        Grade grade,
        String school,
        String major,
        Set<String> techStackNames,
        Set<String> certificateNames,
        Set<String> contestEntryNames,
        Set<String> awards,
        int warningCount,
        SuspensionStatus suspensionStatus,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        LocalDateTime suspensionEndTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        LocalDateTime updatedAt


) {
    public static MemberResponse from(MemberDomain domain) {
        return new MemberResponse(
                domain.getId(),
                domain.getMemberInfo().email(),
                domain.getMemberInfo().name(),
                domain.getMemberInfo().profileImage() != null ? domain.getMemberInfo().profileImage() : null,
                domain.getMemberInfo().memberRole(),
                Optional.ofNullable(domain.getMemberInfo().memberField()).map(MemberField::getFieldName).orElse(null),
                Optional.ofNullable(domain.getMemberInfo().memberDuty()).map(MemberDuty::getDutyName).orElse(null),
                /*domain.getMemberInfo().memberField().getFieldName(),
                domain.getMemberInfo().memberDuty().getDutyName(),*/
                domain.getMemberInfo().grade(),
                domain.getMemberInfo().school(),
                domain.getMemberInfo().major(),
                Optional.ofNullable(domain.getMemberInfo().techStacks()).map(techStacks ->
                        techStacks.stream().map(TechStack::getName).collect(Collectors.toSet())
                ).orElse(Collections.emptySet()),
                Optional.ofNullable(domain.getMemberInfo().certificates()).map(certificates ->
                        certificates.stream().map(Certificate::getName).collect(Collectors.toSet())
                ).orElse(Collections.emptySet()),
                Optional.ofNullable(domain.getMemberInfo().contestEntries()).map(contestEntries ->
                        contestEntries.stream().map(ContestEntry::getName).collect(Collectors.toSet())
                ).orElse(Collections.emptySet()),
                Optional.ofNullable(domain.getMemberInfo().awards()).orElse(Collections.emptySet()),
//                Optional.ofNullable(domain.getMemberInfo().awards()).map(awards ->
//                        awards.stream().map(Award::getName).collect(Collectors.toSet())
//                ).orElse(Collections.emptySet()),
                /*domain.getMemberInfo().techStacks().stream().map(TechStack::getName).collect(Collectors.toSet()),
                domain.getMemberInfo().certificates().stream().map(Certificate::getName).collect(Collectors.toSet()),
                domain.getMemberInfo().contestEntries().stream().map(ContestEntry::getName).collect(Collectors.toSet()),
                domain.getMemberInfo().awards().stream().map(Award::getName).collect(Collectors.toSet()),*/
                domain.getWarningCount(),
                domain.getStatus(),
                domain.getSuspensionEndTime(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
