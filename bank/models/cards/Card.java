package bank.models.cards;

import bank.models.enums.CardStatus;
import bank.models.enums.CardType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Card {
    private Integer cardId;
    private Integer accountId;
    private String cardNumberMasked;
    private CardType cardType;
    private String cardHolderName;
    private String expiryDate;
    private String cvcHash;
    private CardStatus status;
    private BigDecimal dailyLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Card(
            Integer cardId,
            Integer accountId,
            String cardNumberMasked,
            CardType cardType,
            String cardHolderName,
            String expiryDate,
            String cvcHash,
            CardStatus status,
            BigDecimal dailyLimit,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.cardId = cardId;
        this.accountId = accountId;
        this.cardNumberMasked = cardNumberMasked;
        this.cardType = cardType;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvcHash = cvcHash;
        this.status = status;
        this.dailyLimit = dailyLimit;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
