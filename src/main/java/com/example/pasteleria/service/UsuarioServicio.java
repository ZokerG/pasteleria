package com.example.pasteleria.service;

import com.example.pasteleria.ex.BenfitException;
import com.example.pasteleria.ex.ErrorMessages;
import com.example.pasteleria.jwt.JwtTokenProvider;
import com.example.pasteleria.model.Usuarios;
import com.example.pasteleria.repository.UsuarioRepositorio;
import com.example.pasteleria.request.AuthRequest;
import com.example.pasteleria.request.CrearUsuarioRequest;
import com.example.pasteleria.response.AuthUserResponse;
import com.example.pasteleria.response.UserInfoResponse;
import com.example.pasteleria.response.UserResponse;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailService userDetailService;
    private final JwtTokenProvider tokenProvider;
    private final I18NService i18NService;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, UserDetailService userDetailService, JwtTokenProvider tokenProvider, I18NService i18NService) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.tokenProvider = tokenProvider;
        this.i18NService = i18NService;
    }

    public Usuarios findById(Long id) throws Exception {
        return usuarioRepositorio.findById(id).orElseThrow(() -> new Exception("Usuario no encontrado"));
    }

    public AuthUserResponse signIn(AuthRequest request){
        userDetailService.setFlag("CLIENT");
        UserResponse user = (UserResponse) userDetailService.loadUserByUsername(request.getUsername());
        validateUserPassword.accept(request, user, "error.invalid.credentials");
        return getAuthResponse(user, request.getUsername(), request.getPassword(), user.getAuthorities());
    }

    private AuthUserResponse getAuthResponse(UserResponse user, String username, String password, Collection<? extends GrantedAuthority> authorities) throws BenfitException {
        Usuarios userRecord = usuarioRepositorio.findByEmail(username).orElseThrow(() -> new BenfitException(i18NService.getMessage(ErrorMessages.ACCOUNT_NOT_MATCH)));
        if (!user.isEnabled() || !user.isAccountNonLocked()) {
            throw new BenfitException(i18NService.getMessage(ErrorMessages.ACCOUNT_LOCKED));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password,
                        authorities
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthUserResponse.AuthUserResponseBuilder authResponse = AuthUserResponse.builder()
                .userInfo(new UserInfoResponse(userRecord.getId(), userRecord.getNombre()))
                .accessToken(tokenProvider.createToken(authentication))
                .provider("JDBC")
                .accountCreated(true);

        return authResponse.build();
    }

    private boolean bCryptPasswordEncoderMatch(String rawPassword, String encodePassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodePassword);
    }

    private final TriConsumer<AuthRequest, UserDetails, String> validateUserPassword = (request, user, message) -> {
        if (!bCryptPasswordEncoderMatch(request.getPassword(), user.getPassword())) {
            throw new BenfitException(message);
        }
    };

    public List<Usuarios> findAll() {
        return usuarioRepositorio.findAll();
    }

    public List<Usuarios> findAllByRole(String role) {
        return usuarioRepositorio.findAllByRol(role);
    }

    public void deleteById(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    public Usuarios crearUsuarios(CrearUsuarioRequest request){
        if (usuarioRepositorio.existsByEmail(request.getEmail())) {
            throw new BenfitException(i18NService.getMessage(ErrorMessages.EMAIL_ALREADY_EXISTS));
        }

        if (!request.getPassword().equals(request.getConfirmarPassword())){
            throw new BenfitException(i18NService.getMessage(ErrorMessages.PASSWORDS_DO_NOT_MATCH));
        }

        Usuarios user = new Usuarios();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setApellido(request.getApellido());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setActivo(true);
        user.setRol(request.getRol());
        return usuarioRepositorio.save(user);
    }
}
