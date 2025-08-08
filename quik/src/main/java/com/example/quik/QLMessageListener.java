package com.example.quik;

import com.example.quik.adapter.messages.QLMessage;

public interface QLMessageListener {
    void onMessageReceived(QLMessage message);
}
