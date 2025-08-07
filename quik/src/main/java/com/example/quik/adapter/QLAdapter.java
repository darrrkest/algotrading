package com.example.quik.adapter;

import com.example.abstractions.connector.Adapter;
import com.example.abstractions.connector.AdapterMessageListener;
import com.example.quik.adapter.messages.QLMessage;

public interface QLAdapter extends Adapter<QLMessage, AdapterMessageListener<QLMessage>> {
}
