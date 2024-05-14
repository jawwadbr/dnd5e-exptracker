package com.jawbr.dnd5e.exptracker.util;

import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Component
@EnableAsync
public class UserAccountCleanupScheduler {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserAccountCleanupScheduler.class);

    public UserAccountCleanupScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void cleanupDeactivatedAccountsOnStart() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        logger.info("Performing deactivated accounts cleanup. TIME: {}", currentTime);
        List<User> deactivatedAccountsToDelete = userRepository.findByDeactivationExpirationDateBefore(currentTime);
        deactivatedAccountsToDelete.forEach(user -> user.getJoinedCampaigns().forEach(c -> c.getPlayers().remove(user)));
        userRepository.deleteAll(deactivatedAccountsToDelete);
    }

    @Transactional
    @Async("accCleanup")
    @Scheduled(cron = "0 0 0 * * *") // daily at 12 AM
    public void cleanupDeactivatedAccountsCodes() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        logger.info("Performing deactivated accounts cleanup. TIME: {}", currentTime);
        List<User> deactivatedAccountsToDelete = userRepository.findByDeactivationExpirationDateBefore(currentTime);
        deactivatedAccountsToDelete.forEach(user -> user.getJoinedCampaigns().forEach(c -> c.getPlayers().remove(user)));
        userRepository.deleteAll(deactivatedAccountsToDelete);
    }
}
