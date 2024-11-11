package com.cil.bf.gestion_courriers_back.Auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cil.bf.gestion_courriers_back.Auth.service.Impl.CustomerUserDetailsService;

/**
 * Filtre de sécurité JWT pour vérifier et valider les jetons d'authentification
 * JWT dans les requêtes HTTP entrantes.
 * Ce filtre s'exécute une fois par requête et extrait le jeton JWT de l'en-tête
 * d'autorisation, vérifie sa validité,
 * et configure le contexte de sécurité si l'utilisateur est authentifié.
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JWTServiceInt jwtService; // Service pour manipuler les JWT

  @Autowired
  private CustomerUserDetailsService userDetailsService; // Service pour charger les détails de l'utilisateur

  /**
   * Effectue le filtrage de chaque requête pour vérifier la présence et la
   * validité du JWT.
   *
   * @param request     la requête HTTP
   * @param response    la réponse HTTP
   * @param filterChain la chaîne de filtres de sécurité
   * @throws ServletException en cas d'erreur de servlet
   * @throws IOException      en cas d'erreur d'entrée/sortie
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // Extraction de l'en-tête d'autorisation
    final String authHeader = request.getHeader("Authorization");

    // Vérifie si l'en-tête contient un JWT valide
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String jwt = authHeader.substring(7); // Extrait le token sans le préfixe "Bearer "
      String userEmail = jwtService.extractUsername(jwt); // Extrait le nom d'utilisateur à partir du token

      // Si un utilisateur est trouvé et qu'il n'y a pas déjà d'authentification
      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail); // Charge les détails de
                                                                                    // l'utilisateur

        // Si le token est valide, configure le contexte de sécurité
        if (jwtService.isTokenValid(jwt, userDetails)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    }

    // Passe la requête au filtre suivant dans la chaîne
    filterChain.doFilter(request, response);
  }
}