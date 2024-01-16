package com.leron.api.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leron.api.model.GenericEntities;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecificGroup extends GenericEntities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "name", nullable = false)
    private String name;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "macroGroup_id")
    private MacroGroup macroGroup;

}
