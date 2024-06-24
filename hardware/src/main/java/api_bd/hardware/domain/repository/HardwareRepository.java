package api_bd.hardware.domain.repository;

import java.util.Optional;

import api_bd.hardware.domain.model.Hardware;
import api_bd.hardware.domain.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HardwareRepository extends JpaRepository<Hardware, Long> {

    Optional<Hardware> findByUsuario(Usuario usuario);
    
}
