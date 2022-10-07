package br.com.dicasdeumdev.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
//A anotação Data vai fazer implicitaçmente a antoação @getter, @Setter, @ToString, @EgualsAndHashCode.
//Cuidado com o @EqualsAndHashCode, pois pode ficar lento se tiver muitos parâmetros.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TB_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
}
