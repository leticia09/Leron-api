package com.leron.api.model.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends GenericEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private String owner;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Card> cards;
}
