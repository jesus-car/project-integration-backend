package com.dh.roomly;

import com.dh.roomly.common.RoleEnum;
import com.dh.roomly.entity.PermissionEntity;
import com.dh.roomly.entity.RoleEntity;
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
			RoleEntity clientRoleEntity = RoleEntity.builder()
					.name(RoleEnum.ROLE_CLIENT)
					.permissions(Set.of(readPermission, updatePermission, deletePermission, writePermission))
					.build();

			RoleEntity sellerRoleEntity = RoleEntity.builder()
					.name(RoleEnum.ROLE_SELLER)
					.permissions(Set.of(readPermission, writePermission, deletePermission, updatePermission))
					.build();

			RoleEntity adminRoleEntity = RoleEntity.builder()
					.name(RoleEnum.ROLE_ADMIN)
					.permissions(Set.of(readPermission, writePermission, deletePermission, updatePermission))
					.build();

			// Create users
			UserEntity client = UserEntity.builder()
					.firstName("John")
					.lastName("Doe")
					.email("jhon.doe@asd.com")
					.password("$2a$10$B2c3eYB/VFal9VptzHDVF.9jwf847aQbXOyJHT4ZfiFa3nwqJwg2K")
					.roleEntities(Set.of(clientRoleEntity))
					.identificationNumber("1234")
					.typeId(Short.parseShort("2"))
					.phoneNumber("12345")
					.cityId(Short.parseShort("1"))
					.isEnabled(true)
					.isLocked(false)
					.accountNonExpired(true)
					.credentialsNonExpired(true)
					.build();

			UserEntity seller = UserEntity.builder()
					.firstName("Admin")
					.lastName("Admin")
					.email("admin.admin")
					.password("$2a$10$B2c3eYB/VFal9VptzHDVF.9jwf847aQbXOyJHT4ZfiFa3nwqJwg2K")
					.roleEntities(Set.of(sellerRoleEntity))
					.identificationNumber("1234")
					.typeId(Short.parseShort("2"))
					.phoneNumber("12345")
					.cityId(Short.parseShort("1"))
					.isEnabled(true)
					.isLocked(false)
					.accountNonExpired(true)
					.credentialsNonExpired(true)
					.build();

			UserEntity admin = UserEntity.builder()
					.firstName("Moderator")
					.lastName("Moderator")
					.email("moderator.moderator")
					.password("$2a$10$B2c3eYB/VFal9VptzHDVF.9jwf847aQbXOyJHT4ZfiFa3nwqJwg2K")
					.roleEntities(Set.of(adminRoleEntity))
					.identificationNumber("1234")
					.typeId(Short.parseShort("2"))
					.phoneNumber("12345")
					.cityId(Short.parseShort("1"))
					.isEnabled(true)
					.isLocked(false)
					.accountNonExpired(true)
					.credentialsNonExpired(true)
					.build();

			userRepository.saveAll(Set.of(client, seller, admin));
		};
	}
};
