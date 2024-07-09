package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    CreateUserDto userToCreateUserDto(User user);

    User createUserDtoToUser(CreateUserDto createUserDto);

    UpdateUserDto userToUpdateUserDto(User user);

    User updateUserDtoToUser(UpdateUserDto updateUserDto);
}
