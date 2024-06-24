package api_bd.hardware.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api_bd.hardware.domain.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
       
        Optional<Usuario> findByEmail(String email);
    
}

