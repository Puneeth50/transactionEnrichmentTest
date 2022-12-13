package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionForEnrichment {

    private String id;
    private String date;
    private String amount;
    private String description;

    public TransactionForEnrichment(String id, String date, String amount, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }
}
