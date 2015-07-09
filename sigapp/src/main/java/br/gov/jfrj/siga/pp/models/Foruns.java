package br.gov.jfrj.siga.pp.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

/**
 * @author Herval, Edson da Rocha, e Ruben
 */
@Entity(name = "Foruns")
@Table(name = "Foruns", schema = "SIGAPMP")
public class Foruns extends Objeto {
    private static final long serialVersionUID = -8184741586013224510L;
    public static final ActiveRecord<Foruns> AR = new ActiveRecord<>(Foruns.class);

    @Id()
    @Column(name = "cod_forum", length = 10, nullable = false)
    public int cod_forum;

    @OneToMany(mappedBy = "forumFk")
    // isso não é campo, mas um 'references'.
    public List<Locais> lstLocal;

    @OneToMany(mappedBy = "forumFk")
    // isso não é campo, mas um 'references'.
    public List<UsuarioForum> lstUsuarioForum;

    @Column(name = "descricao_forum", length = 40, nullable = true)
    public String descricao_forum;

    @Column(name = "mural", length = 1000, nullable = true)
    public String mural; // texto com tags html

    public Foruns(int cod_forum_construt, String descricao_construt, String mural_construt) {
        this.cod_forum = cod_forum_construt;
        this.descricao_forum = descricao_construt;
        this.mural = mural_construt;
    }

}
