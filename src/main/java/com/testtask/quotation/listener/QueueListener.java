package com.testtask.quotation.listener;

import com.testtask.quotation.event.AddToQueueEvent;
import com.testtask.quotation.service.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class QueueListener {
    @Autowired
    ProcessingService processingService;

    @EventListener
    @Async
    void handleReturnedEvent(AddToQueueEvent event) {
        processingService.startProcessing();
    }
}
