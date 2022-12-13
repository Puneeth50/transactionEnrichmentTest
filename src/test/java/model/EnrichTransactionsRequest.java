package model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnrichTransactionsRequest {

    private List<String> enrichments;

    private List<TransactionForEnrichment> transactions;

    public EnrichTransactionsRequest(List<String> enrichments, List<TransactionForEnrichment> transactions) {
        this.enrichments = enrichments;
        this.transactions = transactions;
    }
}
