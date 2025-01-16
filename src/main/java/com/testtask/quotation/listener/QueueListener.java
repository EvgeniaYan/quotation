package com.testtask.quotation.listener;

import com.testtask.quotation.event.AddToQueueEvent;
import com.testtask.quotation.service.ProcessingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class QueueListener {
    ProcessingService processingService;

    @EventListener
    @Async
    void handleReturnedEvent(AddToQueueEvent event) {
        processingService.startProcessing();
    }
}
