package api_bd.hardware.domain.repository;

import api_bd.hardware.domain.model.Hardware;
import api_bd.hardware.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HardwareRepository extends JpaRepository<Hardware, Long> {
    List<Hardware> findByUsuario(Usuario usuario);
}
