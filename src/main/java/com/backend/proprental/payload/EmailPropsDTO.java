package com.backend.proprental.payload;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailPropsDTO {
    private String subject;
    private String btnText;
    private String btnLink;
    private String bodyText;
}
