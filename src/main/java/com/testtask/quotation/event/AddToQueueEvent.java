package com.testtask.quotation.event;

import org.springframework.context.ApplicationEvent;

public class AddToQueueEvent extends ApplicationEvent {
    public AddToQueueEvent(Object source) {
        super(source);
    }

    @Override
    public String toString() {
        return "AddToQueueEvent occurred";
    }
}
