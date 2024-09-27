package com.leventsclone.leventsclone.service.impl;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class SocketSer {

    private  final  SimpMessageSendingOperations messagingTemplate;

    public SocketSer(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public  void  notifyNewOrder() {
        messagingTemplate.convertAndSend("/order/new", "nothing");
    }

    public void notifyCancelOrder(String codeOrder) {
        messagingTemplate.convertAndSend("/order/cancel", codeOrder);
    }
}
