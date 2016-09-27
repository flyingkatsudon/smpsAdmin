package com.humane.smps.service;

import com.humane.smps.model.UserAdmission;
import com.humane.smps.model.UserRole;
import com.humane.smps.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("userService")
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        com.humane.smps.model.User user = userRepository.findOne(userId);
        List<GrantedAuthority> authorities = buildUserAuthority(user.getUserRoles());
        String admissions = buildUserAdmission(user.getUserAdmissions());

        return new CustomUserDetails(user.getUserId(), user.getPassword(), authorities, admissions);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {
        List<GrantedAuthority> authorities = new ArrayList<>(0);
        for (UserRole userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority(userRole.getRoleName()));
        }

        return authorities;
    }

    private String buildUserAdmission(Set<UserAdmission> userAdmissions) {
        if (userAdmissions != null && userAdmissions.size() > 0) {
            UserAdmission[] items = userAdmissions.toArray(new UserAdmission[userAdmissions.size()]);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < items.length; i++) {
                sb.append("'").append(items[i].getAdmission().getAdmissionCd()).append("'");
                if (i != userAdmissions.size() - 1) sb.append(",");
            }
            return sb.toString();
        }
        return null;
    }
}
