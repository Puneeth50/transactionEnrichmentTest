package model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnrichTransactionsRequest {

    private List<String> enrichments;

    private TransactionForEnrichment transactions;

    public EnrichTransactionsRequest(List<String> enrichments,  TransactionForEnrichment transactions) {
        this.enrichments = enrichments;
        this.transactions = transactions;
    }
}
