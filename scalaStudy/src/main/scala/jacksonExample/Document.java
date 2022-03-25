package jacksonExample;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Document {
    public String companyName;
    public String documentId;
    public List<String> types;
    public Long number;
    public Float ctr;
    public Document(
            @JsonProperty("companyName") String companyName,
            @JsonProperty("documentId") String documentId,
            @JsonProperty("types") List<String> types,
            @JsonProperty("number") Long number,
            @JsonProperty("ctr") Float ctr
    ) {
        this.companyName = companyName;
        this.documentId = documentId;
        this.types = types;
        this.number = number;
        this.ctr = ctr;
    }

}
