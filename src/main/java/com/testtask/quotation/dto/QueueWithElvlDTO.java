package com.testtask.quotation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter @Setter
@RequiredArgsConstructor
public class QueueWithElvlDTO {
    private Date CREATE_WHEN;
    private String status;
    private String isin;
    private Double value;

    public QueueWithElvlDTO(Date createWhen, String status, String isin, Double value) {
        this.CREATE_WHEN = createWhen;
        this.status = status;
        this.isin = isin;
        this.value = value;
    }
}
