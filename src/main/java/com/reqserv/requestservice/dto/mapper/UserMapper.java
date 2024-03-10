package com.reqserv.requestservice.dto.mapper;

import com.reqserv.requestservice.dto.UserRequestDTO;
import com.reqserv.requestservice.dto.UserResponseDTO;
import com.reqserv.requestservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserResponseDTO userToResponseDTO(User user);

  User requestDTOToUser(UserRequestDTO userRequestDTO);

}
