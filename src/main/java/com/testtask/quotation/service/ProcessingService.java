package com.testtask.quotation.service;

import com.testtask.quotation.event.AddToQueueEvent;
import com.testtask.quotation.dto.QuoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ProcessingService implements ApplicationEventPublisherAware {
    @Autowired
    ElvlService elvlService;

    ConcurrentLinkedQueue<QuoteDTO> queue = new ConcurrentLinkedQueue<>();
    ExecutorService executorService;
    private ApplicationEventPublisher publisher;

    @Transactional
    public void startProcessing(){
        if (executorService == null || executorService.isShutdown()) {
            executorService = Executors.newFixedThreadPool(1);
            executorService.execute(() -> {
                QuoteDTO quote;
                while ((quote = queue.poll()) != null) {
                    elvlService.createElvl(quote);
                }
            });
            executorService.shutdown();
        }
    }

    public void addToQueue(QuoteDTO quote) {
        queue.add(quote);
        AddToQueueEvent addToQueueEvent = new AddToQueueEvent(this);
        publisher.publishEvent(addToQueueEvent);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }
}
