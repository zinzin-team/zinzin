package com.fanclub.zinzin.global.scheduling;

import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.Status;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final MemberRepository memberRepository;
    private final PersonRepository personRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldDeletedMembers() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(15);

        List<Member> oldDeletedMembers = memberRepository.findAllByStatusAndDeletedAtBefore(Status.DELETED, cutoffDate);

        for (Member member : oldDeletedMembers) {
            personRepository.withdraw(member.getSub());
            memberRepository.delete(member);
        }
        log.info("탈퇴 계정 삭제 완료");
    }
}
