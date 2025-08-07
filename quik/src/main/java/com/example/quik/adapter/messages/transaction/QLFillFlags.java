package com.example.quik.adapter.messages.transaction;

public final class QLFillFlags {
    public static final int Marginal = 0x1;
    public static final int Sell = 0x4;
    public static final int Iceberg = 0x8;
    public static final int Cancelled = 0x10;
    public static final int Passive = 0x20;
    public static final int Active = 0x40;
    public static final int SwapFirstLeg = 0x80;
    public static final int SwapSecondLeg = 0x100;
}