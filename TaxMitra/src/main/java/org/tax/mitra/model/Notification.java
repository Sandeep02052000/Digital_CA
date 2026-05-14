package org.tax.mitra.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Notification {

    @NotBlank(message = "Notification type cannot be blank")
    private final NotificationType notificationType;

    @Email(message = "Invalid email format")
    private final String email;

    private final ChannelType type;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid mobile number")
    private final String msisdn;

    @NotBlank(message = "Message cannot be blank")
    private final String message;

    public enum NotificationType {
        OTP,
        TRANSACTIONAL,
        ALERT,
        PROMOTIONAL
    }
    public enum ChannelType {
        EMAIL,
        SMS,
        URL
    }
    public boolean isValid() {
        return notificationType != null
                && message != null
                && !message.trim().isEmpty()
                && (email != null || msisdn != null);
    }
}