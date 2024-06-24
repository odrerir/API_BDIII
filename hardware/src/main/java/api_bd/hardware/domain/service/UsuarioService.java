package api_bd.hardware.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import api_bd.hardware.domain.dto.usuario.UsuarioRequestDTO;
import api_bd.hardware.domain.dto.usuario.UsuarioResponseDTO;
import api_bd.hardware.domain.exception.BadRequestException;
import api_bd.hardware.domain.exception.ResourceNotFoundException;
import api_bd.hardware.domain.model.Usuario;
import api_bd.hardware.domain.repository.UsuarioRepository;

@Service
public class UsuarioService implements ICRUDService<UsuarioRequestDTO, UsuarioResponseDTO> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<UsuarioResponseDTO> obterTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuario -> mapper.map(usuario, UsuarioResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO obterPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
        return mapper.map(usuario, UsuarioResponseDTO.class);
    }

    @Override
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        validarDTO(dto);

        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Já existe um usuário cadastrado com esse email!");
        }

        Usuario usuario = mapper.map(dto, Usuario.class);
        criptografarSenha(usuario);
        usuario = usuarioRepository.save(usuario);
        return mapper.map(usuario, UsuarioResponseDTO.class);
    }

    @Override
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        validarDTO(dto);

        Usuario usuarioBanco = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));

        usuarioBanco.setNome(dto.getNome());
        usuarioBanco.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            criptografarSenha(usuarioBanco, dto.getSenha());
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioBanco);
        return mapper.map(usuarioAtualizado, UsuarioResponseDTO.class);
    }

    @Override
    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
        usuarioRepository.delete(usuario);
    }

    private void validarDTO(UsuarioRequestDTO dto) {
        if (dto.getEmail() == null || dto.getSenha() == null) {
            throw new BadRequestException("Email e Senha são obrigatórios!");
        }
    }

    private void criptografarSenha(Usuario usuario) {
        String senhaCriptografada = bCryptPasswordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
    }

    private void criptografarSenha(Usuario usuario, String novaSenha) {
        String senhaCriptografada = bCryptPasswordEncoder.encode(novaSenha);
        usuario.setSenha(senhaCriptografada);
    }
}
