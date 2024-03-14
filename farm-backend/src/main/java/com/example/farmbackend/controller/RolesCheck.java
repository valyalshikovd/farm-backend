package com.example.farmbackend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.Objects;
/**
 * Класс для проверки ролей пользователей.
 */
public class RolesCheck {

    /**
     * Проверяет, имеет ли сотрудник, представленный объектом аутентификации,
     * определенную роль.
     *
     * @param authentication Объект аутентификации пользователя.
     * @param role Роль, которую необходимо проверить (в виде строки).
     * @return True, если пользователь имеет указанную роль, false - otherwise.
     */
    public static boolean authoritiesCheck(Authentication authentication, String role){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if(Objects.equals(authority.getAuthority(), role)){
                return true;
            }
        }
        return false;
    }
}
