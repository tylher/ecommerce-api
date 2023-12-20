package dev.damola.ecommerce.converter;

import dev.damola.ecommerce.Dto.CreateUserDto;
import org.springframework.core.convert.converter.Converter;
import dev.damola.ecommerce.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<CreateUserDto, User> {

    @Override
    public User convert(CreateUserDto source) {
        return User.builder()
                .email(source.email())
                .password(source.password())
                .roles(source.roles())
                .build();
    }
}
