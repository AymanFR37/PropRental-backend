package com.backend.proprental.configuration;

import com.backend.proprental.payload.EmailPropsDTO;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Configuration
@ConfigurationProperties(prefix = "email.props")
public class EmailProps {
    private EmailPropsDTO sendResetPasswordEmail;
}
