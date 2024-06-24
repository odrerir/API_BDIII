package api_bd.hardware.domain.service;

import api_bd.hardware.domain.dto.hardware.HardwareRequestDTO;
import api_bd.hardware.domain.dto.hardware.HardwareResponseDTO;
import api_bd.hardware.domain.exception.BadRequestException;
import api_bd.hardware.domain.exception.ResourceNotFoundException;
import api_bd.hardware.domain.model.Hardware;
import api_bd.hardware.domain.model.Usuario;
import api_bd.hardware.domain.repository.HardwareRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HardwareService implements ICRUDService<HardwareRequestDTO, HardwareResponseDTO> {

    private final HardwareRepository hardwareRepository;
    private final ModelMapper mapper;

    public HardwareService(HardwareRepository hardwareRepository, ModelMapper mapper) {
        this.hardwareRepository = hardwareRepository;
        this.mapper = mapper;
    }

    @Override
    public List<HardwareResponseDTO> obterTodos() {
        List<Hardware> hardwares = hardwareRepository.findByUsuario(usuario);
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

    @Override
    public HardwareResponseDTO cadastrar(HardwareRequestDTO dto) {
        if (dto.getNome() == null || dto.getFabricante() == null || dto.getTipo() == null ||
                dto.getAno() == null || dto.getValor() == null) {
            throw new BadRequestException("Nome, Fabricante, Tipo, Ano e Valor são obrigatórios!");
        }

        Hardware hardware = mapper.map(dto, Hardware.class);
        hardware.setId(null); 

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
