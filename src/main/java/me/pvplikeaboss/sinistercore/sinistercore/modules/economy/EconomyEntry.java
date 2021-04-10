package me.pvplikeaboss.sinistercore.sinistercore.modules.economy;

import java.math.BigDecimal;
import java.util.UUID;

public class EconomyEntry {
    private final UUID playerUUID;
    private final String playerName;
    private final BigDecimal balance;

    public EconomyEntry(UUID playerUUID, String playerName, BigDecimal balance) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.balance = balance;
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }
}
