package br.com.bugbuilder.chamado.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bugbuilder.chamado.models.UserChamadoModel;

public interface UserChamadoRepository extends JpaRepository<UserChamadoModel, UUID> {
	List<UserChamadoModel> findByUsername(String username);
}
