package br.com.bugbuilder.chamado.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TB_CHAMADOS")
public class ChamadoModel extends RepresentationModel<ChamadoModel>{
	@Id
	@GeneratedValue
	private UUID id;
	@Column(nullable = false, length = 25)
	private String title;
	
	@Column(nullable = false)
	@Lob
	private String description;
	
	@ManyToOne
	private UserChamadoModel requesterUser;
	private boolean open;
	
	@Enumerated(EnumType.STRING)
	private ClassificationName classificationName;
	
	@ManyToOne
	private UserChamadoModel attendantUser;
	
	private String openingCallDate;
	private String closingCallDate;
	private String classificationCallDate;

}
