package com.reqserv.requestservice.dto;

import com.reqserv.requestservice.model.Role;
import java.util.Set;

public record UserRolesDTO(

    Set<Role> roles

) {

}
