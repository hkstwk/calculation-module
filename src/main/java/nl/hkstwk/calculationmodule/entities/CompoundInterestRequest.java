package nl.hkstwk.calculationmodule.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CompoundInterest")
public class CompoundInterestRequest extends BaseRequest {
}
