package br.gov.jfrj.siga.pp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "UsuarioForum", schema = "SIGAPMP")
public class UsuarioForum extends Objeto {

    private static final long serialVersionUID = -698697354242184472L;
    public static final ActiveRecord<UsuarioForum> AR = new ActiveRecord<>(UsuarioForum.class);

    @Id()
    @Column(name = "matricula_usu", length = 6, nullable = false, unique = true)
    public String matricula_usu;

    @Id()
    @ManyToOne
    @JoinColumn(name = "cod_forum", nullable = false)
    // fk, e tem que atribuir via objeto forumfK
    public Foruns forumFk; // isso é coluna, mas tem que atribuir como objeto

    @Column(name = "nome_usu", length = 50, nullable = true)
    public String nome_usu;

    public UsuarioForum(String matricula_usu_construt, String nome_usu_construt, Foruns cod_forum_construt) {
        this.matricula_usu = matricula_usu_construt;
        this.nome_usu = nome_usu_construt;
        this.forumFk = cod_forum_construt;
    }
}
