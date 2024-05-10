package com.jawbr.dnd5e.exptracker.util;

import com.jawbr.dnd5e.exptracker.entity.InviteCode;
import com.jawbr.dnd5e.exptracker.repository.InviteCodeRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class InviteCodeCleanupScheduler {

    private final InviteCodeRepository inviteCodeRepository;

    public InviteCodeCleanupScheduler(InviteCodeRepository inviteCodeRepository) {
        this.inviteCodeRepository = inviteCodeRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void cleanupExpiredInviteCodesOnStart() {
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("Performing expired codes cleanup. TIME: " + currentTime);
        List<InviteCode> expiredCodes = inviteCodeRepository.findByExpiryDateBefore(currentTime);
        inviteCodeRepository.deleteAll(expiredCodes);
    }

    @Scheduled(cron = "0 0 * * * *") // every hour
    //@Scheduled(cron = "0 * * * * *") // every minute
    public void cleanupExpiredInviteCodes() {
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("Performing expired codes cleanup. TIME: " + currentTime);
        List<InviteCode> expiredCodes = inviteCodeRepository.findByExpiryDateBefore(currentTime);
        inviteCodeRepository.deleteAll(expiredCodes);
    }
}
