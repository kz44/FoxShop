package com.greenfoxacademy.foxshopnullpointerninjasotocyon.utils;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class UnverifiedUserRemove {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final UserRepository userRepository;

    public void deleteUnverifiedUser(User user) {
        final Runnable runRemoveMethod = () -> {
            if (!user.isVerified()) {
                userRepository.delete(user);
            }
        };
        final ScheduledFuture<?> removeTimer = scheduler.schedule(runRemoveMethod, 24, TimeUnit.HOURS);
    }


}
