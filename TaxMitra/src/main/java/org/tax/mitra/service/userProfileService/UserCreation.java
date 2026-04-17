package org.tax.mitra.service.userProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.tax.mitra.constants.ErrorCodes;
import org.tax.mitra.entity.User;
import org.tax.mitra.exception.OtpException;
import org.tax.mitra.repository.UserRepository;

@Component
public class UserCreation {
    @Autowired
    UserRepository repository;

    public User createUser(String input) {
        try {
            User user = new User();
            user.setInput(input);
            user.setIsActive(true);
            user.setIsPhoneVerified(true);
            user.setOnboardingStatus("VERIFIED");

            return repository.save(user);
        } catch (DataIntegrityViolationException ex){
            return repository.findByInput(input)
                    .orElseThrow(() -> new OtpException("User creation failed", ErrorCodes.BAD_REQUEST));
        }
    }
}
