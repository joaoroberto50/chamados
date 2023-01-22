package br.com.bugbuilder.chamado.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.bugbuilder.chamado.models.ChamadoModel;

@Repository
public interface ChamadoRepository extends JpaRepository<ChamadoModel, UUID> {
	@Query(value = "SELECT * FROM tb_chamados WHERE requester_user_user_id  = ?1", 
			nativeQuery = true)
	List<ChamadoModel> findAllByRequesterUser(UUID id);
	
	@Query(value = "SELECT * FROM tb_chamados WHERE attendant_user_user_id = ?1", 
			nativeQuery = true)
	List<ChamadoModel> findAllByAttendantUser(UUID id);
	
	@Query(value = "SELECT * FROM tb_chamados WHERE open = true", nativeQuery = true)
	List<ChamadoModel> findAllByOpenCall();
	
	@Query(value = "SELECT * from tb_chamados WHERE attendant_user_user_id = ?1 or open = true", 
			nativeQuery = true)
	List<ChamadoModel> findAllByAttendantOrOpen(UUID id);
}
