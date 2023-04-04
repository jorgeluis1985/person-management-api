package io.github.nataelienai.usermanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String cep;
  private String city;
  private String street;
  private Integer number;
  private Boolean main;

  @ManyToOne
  @JoinColumn(name = "person_id", nullable = false)
  private Person person;

  public Address(String cep, String city, String street, Integer number, Boolean main, Person person) {
    this.cep = cep;
    this.city = city;
    this.street = street;
    this.number = number;
    this.main = main;
    this.person = person;
  }
}
