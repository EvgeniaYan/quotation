package com.testtask.quotation.service;

import com.testtask.quotation.event.AddToQueueEvent;
import com.testtask.quotation.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ProcessingService implements ApplicationEventPublisherAware {
    @Autowired
    ElvlService elvlService;

    ExecutorService executorService = Executors.newFixedThreadPool(1);
    Queue<Quote> queue = new LinkedList<>();
    private ApplicationEventPublisher publisher;

    public void startProcessing(){
        if (executorService.isShutdown()) {
            executorService.execute(() -> {
                Quote quote;
                while ((quote = queue.poll()) != null) {
                    elvlService.createElvl(quote);
                }
            });
            executorService.shutdown();
        }
    }

    public void addToQueue(Quote quote) {
        queue.add(quote);
        AddToQueueEvent addToQueueEvent = new AddToQueueEvent(this);
        publisher.publishEvent(addToQueueEvent);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }
}
