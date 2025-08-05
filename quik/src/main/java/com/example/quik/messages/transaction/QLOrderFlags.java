package com.example.quik.messages.transaction;

public final class QLOrderFlags {
    public static final int Active = 0x1;
    public static final int Cancelled = 0x2;
    public static final int Sell = 0x4;
    public static final int Limit = 0x8;
    public static final int FillAtDifferentPrices = 0x10;
    public static final int FillOrKill = 0x20;
    public static final int MarketMakerOrder = 0x40;
    public static final int Hidden = 0x80;
    public static final int CancelUnfilled = 0x100;
    public static final int Iceberg = 0x200;
    public static final int Rejected = 0x400;
    public static final int Linkedorder = 0x10000;
}
