package softuniBlog.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.thymeleaf.util.StringUtils;
import softuniBlog.entity.User;

import java.util.ArrayList;
import java.util.Collection;

public class BlogUserDetails extends User implements UserDetails {
    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked(){return true;}

    @Override
    public boolean isCredentialsNonExpired(){return true;}

    @Override
    public boolean isEnabled(){return true;}

    private ArrayList<String> roles;
    private User user;

    public BlogUserDetails(User user,ArrayList<String> roles)
    {
        super(user.getEmail(), user.getFullName(),user.getPassword());
     this.roles = roles;
     this.user = user;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
        {//Takes thymeleaf version 3.0.11
            String userRoles = StringUtils.join(this.roles, ",");
            return AuthorityUtils.commaSeparatedStringToAuthorityList(userRoles);
        }

    @Override
    public String getUsername() {
        return user.getFullName();
    }


    public User getUser()
{
    return this.user;
}
}
