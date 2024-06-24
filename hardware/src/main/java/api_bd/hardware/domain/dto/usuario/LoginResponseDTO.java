package api_bd.hardware.domain.dto.usuario;

public class LoginResponseDTO {
    private String token;
    private UsuarioResponseDTO usuario;
    private UsuarioResponseDTO usuarioResponseDTO;
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public UsuarioResponseDTO getUsuario() {
        return usuario;
    }
    public void setUsuario(UsuarioResponseDTO usuario) {
        this.usuario = usuario;
    }
    public UsuarioResponseDTO getUsuarioResponseDTO() {
        return usuarioResponseDTO;
    }
    public void setUsuarioResponseDTO(UsuarioResponseDTO usuarioResponseDTO) {
        this.usuarioResponseDTO = usuarioResponseDTO;
    }
}
