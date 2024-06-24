package api_bd.hardware.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api_bd.hardware.domain.dto.hardware.HardwareRequestDTO;
import api_bd.hardware.domain.dto.hardware.HardwareResponseDTO;
import api_bd.hardware.domain.service.HardwareService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/hardwares")
public class HardwareController {

    @Autowired
    private HardwareService hardwareService;

    @GetMapping
    public ResponseEntity<List<HardwareResponseDTO>> obterTodos() {
        return ResponseEntity.ok(hardwareService.obterTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HardwareResponseDTO> obterPorId(@PathVariable Long id) {
        return ResponseEntity.ok(hardwareService.obterPorId(id));
    }

    @PostMapping
    public ResponseEntity<HardwareResponseDTO> cadastrar(@RequestBody HardwareRequestDTO dto) {
        HardwareResponseDTO hardware = hardwareService.cadastrar(dto);
        return new ResponseEntity<HardwareResponseDTO>(hardware, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HardwareResponseDTO> atualizar(@PathVariable Long id, @RequestBody HardwareRequestDTO dto) {
        HardwareResponseDTO hardware = hardwareService.atualizar(id, dto);
        return new ResponseEntity<HardwareResponseDTO>(hardware, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        hardwareService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
