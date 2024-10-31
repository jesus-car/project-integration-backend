package com.dh.roomly;

import com.dh.roomly.common.RoleEnum;
import com.dh.roomly.entity.PermissionEntity;
import com.dh.roomly.entity.Role;
import com.dh.roomly.entity.UserEntity;
import com.dh.roomly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
public class RoomlyApplication {
	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(RoomlyApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();
			PermissionEntity writePermission = PermissionEntity.builder()
					.name("WRITE")
					.build();
			PermissionEntity deletePermission = PermissionEntity.builder()
					.name("DELETE")
					.build();
			PermissionEntity updatePermission = PermissionEntity.builder()
					.name("UPDATE")
					.build();

			// Create roles
			Role invitedRole = Role.builder()
					.name(RoleEnum.ROLE_INVITED)
					.permissions(Set.of(readPermission))
					.build();

			Role userRole = Role.builder()
					.name(RoleEnum.ROLE_USER)
					.permissions(Set.of(readPermission, writePermission))
					.build();

			Role moderatorRole = Role.builder()
					.name(RoleEnum.ROLE_MODERATOR)
					.permissions(Set.of(readPermission, writePermission, updatePermission))
					.build();

			Role adminRole = Role.builder()
					.name(RoleEnum.ROLE_ADMIN)
					.permissions(Set.of(readPermission, writePermission, deletePermission, updatePermission))
					.build();

			// Create users
			UserEntity user = UserEntity.builder()
					.firstName("John")
					.lastName("Doe")
					.username("john.doe")
					.email("jhon.doe@asd.com")
					.password("$2a$10$B2c3eYB/VFal9VptzHDVF.9jwf847aQbXOyJHT4ZfiFa3nwqJwg2K")
					.roles(Set.of(userRole))
					.identificationNumber("1234")
					.typeId(Short.parseShort("2"))
					.phoneNumber("12345")
					.city("japon")
					.isEnabled(true)
					.isLocked(false)
					.accountNonExpired(true)
					.credentialsNonExpired(true)
					.build();

			UserEntity admin = UserEntity.builder()
					.firstName("Admin")
					.lastName("Admin")
					.username("admin")
					.email("admin.admin")
					.password("$2a$10$B2c3eYB/VFal9VptzHDVF.9jwf847aQbXOyJHT4ZfiFa3nwqJwg2K")
					.roles(Set.of(adminRole))
					.identificationNumber("1234")
					.typeId(Short.parseShort("2"))
					.phoneNumber("12345")
					.city("japon")
					.isEnabled(true)
					.isLocked(false)
					.accountNonExpired(true)
					.credentialsNonExpired(true)
					.build();

			UserEntity moderator = UserEntity.builder()
					.firstName("Moderator")
					.lastName("Moderator")
					.username("moderator")
					.email("moderator.moderator")
					.password("$2a$10$B2c3eYB/VFal9VptzHDVF.9jwf847aQbXOyJHT4ZfiFa3nwqJwg2K")
					.roles(Set.of(moderatorRole))
					.identificationNumber("1234")
					.typeId(Short.parseShort("2"))
					.phoneNumber("12345")
					.city("japon")
					.isEnabled(true)
					.isLocked(false)
					.accountNonExpired(true)
					.credentialsNonExpired(true)
					.build();

			UserEntity invited = UserEntity.builder()
					.firstName("Invited")
					.lastName("Invited")
					.username("invited")
					.email("invited.invited")
					.password("$2a$10$B2c3eYB/VFal9VptzHDVF.9jwf847aQbXOyJHT4ZfiFa3nwqJwg2K")
					.roles(Set.of(invitedRole))
					.identificationNumber("1234")
					.typeId(Short.parseShort("2"))
					.phoneNumber("12345")
					.city("japon")
					.isEnabled(true)
					.isLocked(false)
					.accountNonExpired(true)
					.credentialsNonExpired(true)
					.build();

			userRepository.saveAll(Set.of(user, admin, moderator, invited));
		};
	}
};
