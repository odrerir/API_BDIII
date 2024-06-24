package api_bd.hardware.domain.service;

import api_bd.hardware.domain.dto.hardware.HardwareRequestDTO;
import api_bd.hardware.domain.dto.hardware.HardwareResponseDTO;
import api_bd.hardware.domain.exception.BadRequestException;
import api_bd.hardware.domain.exception.ResourceNotFoundException;
import api_bd.hardware.domain.model.Hardware;
import api_bd.hardware.domain.model.Usuario;
import api_bd.hardware.domain.repository.HardwareRepository;
import api_bd.hardware.domain.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HardwareService implements ICRUDService<HardwareRequestDTO, HardwareResponseDTO> {

    private final HardwareRepository hardwareRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper mapper;

    public HardwareService(HardwareRepository hardwareRepository, UsuarioRepository usuarioRepository, ModelMapper mapper) {
        this.hardwareRepository = hardwareRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Override
    public List<HardwareResponseDTO> obterTodos() {
        Usuario usuarioAutenticado = getUsuarioAutenticado();
        List<Hardware> hardwares = hardwareRepository.findByUsuario(usuarioAutenticado);
        return hardwares.stream()
                .map(hardware -> mapper.map(hardware, HardwareResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public HardwareResponseDTO obterPorId(Long id) {
        Optional<Hardware> optHardware = hardwareRepository.findById(id);
        if (optHardware.isEmpty()) {
            throw new ResourceNotFoundException("Não foi possível encontrar o hardware com o ID: " + id);
        }
        return mapper.map(optHardware.get(), HardwareResponseDTO.class);
    }

    private Usuario getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        } else {
            throw new ResourceNotFoundException("Usuário não autenticado");
        }
    }

    @Override
    public HardwareResponseDTO cadastrar(HardwareRequestDTO dto) {
        if (dto.getNome() == null || dto.getFabricante() == null || dto.getTipo() == null ||
                dto.getAno() == null || dto.getValor() == null) {
            throw new BadRequestException("Nome, Fabricante, Tipo, Ano e Valor são obrigatórios!");
        }

        Hardware hardware = mapper.map(dto, Hardware.class);
        hardware.setId(null);

        Usuario usuarioAutenticado = getUsuarioAutenticado();
        hardware.setUsuario(usuarioAutenticado);

        hardware = hardwareRepository.save(hardware);

        return mapper.map(hardware, HardwareResponseDTO.class);
    }

    @Override
    public HardwareResponseDTO atualizar(Long id, HardwareRequestDTO dto) {
        if (dto.getNome() == null || dto.getFabricante() == null || dto.getTipo() == null ||
                dto.getAno() == null || dto.getValor() == null) {
            throw new BadRequestException("Nome, Fabricante, Tipo, Ano e Valor são obrigatórios!");
        }

        Hardware hardware = mapper.map(dto, Hardware.class);
        hardware.setId(id);

        hardware = hardwareRepository.save(hardware);

        return mapper.map(hardware, HardwareResponseDTO.class);
    }

    @Override
    public void deletar(Long id) {
        Optional<Hardware> optHardware = hardwareRepository.findById(id);
        if (optHardware.isEmpty()) {
            throw new ResourceNotFoundException("Não foi possível encontrar o hardware com o ID: " + id);
        }
        hardwareRepository.deleteById(id);
    }
}
