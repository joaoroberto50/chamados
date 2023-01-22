package br.com.bugbuilder.chamado.repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bugbuilder.chamado.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID>{
	Optional<UserModel> findByUsername(String username);
	
	List<UserModel> findListByUsername(String username);
}
