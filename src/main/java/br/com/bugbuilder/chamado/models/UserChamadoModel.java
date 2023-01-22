package br.com.bugbuilder.chamado.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TB_USER_CHAMADO")
public class UserChamadoModel {
	@Id
	@Column(nullable = false, unique = true)
	private UUID userId;
	@Column(nullable = false, unique = true)
	private String username;
}
