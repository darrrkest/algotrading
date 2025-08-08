package com.example.quik;

import com.example.abstractions.connector.OrderRouter;
import com.example.quik.adapter.messages.QLMessage;

public interface QLRouter extends OrderRouter, QLMessageListener {
}
