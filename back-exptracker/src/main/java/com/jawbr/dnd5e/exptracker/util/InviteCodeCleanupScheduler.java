package com.jawbr.dnd5e.exptracker.util;

import com.jawbr.dnd5e.exptracker.entity.InviteCode;
import com.jawbr.dnd5e.exptracker.repository.InviteCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Component
@EnableAsync
public class InviteCodeCleanupScheduler {

    private final InviteCodeRepository inviteCodeRepository;
    private static final Logger logger = LoggerFactory.getLogger(InviteCodeCleanupScheduler.class);

    public InviteCodeCleanupScheduler(InviteCodeRepository inviteCodeRepository) {
        this.inviteCodeRepository = inviteCodeRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void cleanupExpiredInviteCodesOnStart() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        logger.info("Performing expired codes cleanup. TIME: {}", currentTime);
        List<InviteCode> expiredCodes = inviteCodeRepository.findByExpiryDateBefore(currentTime);
        inviteCodeRepository.deleteAll(expiredCodes);
    }

    @Async("expInviteCleanup")
    @Scheduled(cron = "0 0 * * * *") // every hour
    //@Scheduled(cron = "0 * * * * *") // every minute
    public void cleanupExpiredInviteCodes() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        logger.info("Performing expired codes cleanup. TIME: {}", currentTime);
        List<InviteCode> expiredCodes = inviteCodeRepository.findByExpiryDateBefore(currentTime);
        inviteCodeRepository.deleteAll(expiredCodes);
    }
}
