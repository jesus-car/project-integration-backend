package com.dh.roomly;

import com.dh.roomly.common.RoleEnum;
import com.dh.roomly.entity.CityEntity;
import com.dh.roomly.entity.PermissionEntity;
import com.dh.roomly.entity.RoleEntity;
import com.dh.roomly.entity.UserEntity;
import com.dh.roomly.repository.ICityRepository;
import com.dh.roomly.entity.*;
import com.dh.roomly.repository.ICategoryRepository;
import com.dh.roomly.repository.ICountryRepository;
import com.dh.roomly.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
public class RoomlyApplication {
	private final IUserRepository IUserRepository;
	private final ICityRepository cityRepository;
	private final ICountryRepository countryRepository;
	private final ICategoryRepository categoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(RoomlyApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {

			// Crear países latinoamericanos
			CountryEntity argentina = new CountryEntity();
			argentina.setName("Argentina");

			CountryEntity mexico = new CountryEntity();
			mexico.setName("México");

			CountryEntity colombia = new CountryEntity();
			colombia.setName("Colombia");

			CountryEntity bolivia = new CountryEntity();
			bolivia.setName("Bolivia");

			countryRepository.saveAll(List.of(argentina, mexico, colombia, bolivia));

			// Crear ciudades y asociarlas con el país
			CityEntity buenosAires = new CityEntity();
			buenosAires.setName("Buenos Aires");
			buenosAires.setCountry(argentina);

			CityEntity cordoba = new CityEntity();
			cordoba.setName("Córdoba");
			cordoba.setCountry(argentina);

			argentina.setCities(new HashSet<>(List.of(buenosAires, cordoba)));

			CityEntity ciudadMexico = new CityEntity();
			ciudadMexico.setName("Ciudad de México");
			ciudadMexico.setCountry(mexico);

			CityEntity guadalajara = new CityEntity();
			guadalajara.setName("Guadalajara");
			guadalajara.setCountry(mexico);

			mexico.setCities(new HashSet<>(List.of(ciudadMexico, guadalajara)));

			CityEntity bogota = new CityEntity();
			bogota.setName("Bogotá");
			bogota.setCountry(colombia);

			CityEntity medellin = new CityEntity();
			medellin.setName("Medellín");
			medellin.setCountry(colombia);

			colombia.setCities(new HashSet<>(List.of(bogota, medellin)));

			CityEntity laPaz = new CityEntity();
			laPaz.setName("La Paz");
			laPaz.setCountry(bolivia);

			CityEntity cochabamba = new CityEntity();
			cochabamba.setName("Cochabamba");
			cochabamba.setCountry(bolivia);

			CityEntity santaCruz = new CityEntity();
			santaCruz.setName("Santa Cruz");
			santaCruz.setCountry(bolivia);

			colombia.setCities(new HashSet<>(List.of(laPaz, cochabamba, santaCruz)));

			cityRepository.saveAll(List.of(buenosAires, cordoba, ciudadMexico, guadalajara, bogota, medellin, laPaz, cochabamba, santaCruz));

			CategoryEntity categoryPlaya = new CategoryEntity();
			categoryPlaya.setTitle("Playa");
			categoryPlaya.setDescription("Propiedades destinadas a actividades de playa.");

			CategoryEntity categoryCampo = new CategoryEntity();
			categoryCampo.setTitle("Campo");
			categoryCampo.setDescription("Propiedades destinadas a actividades de campo.");

			CategoryEntity categoryMontana = new CategoryEntity();
			categoryMontana.setTitle("Montaña");
			categoryMontana.setDescription("Propiedades destinadas a actividades de montaña.");

			categoryRepository.saveAll(List.of(categoryPlaya, categoryCampo, categoryMontana));

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
					.description("Cliente")
					.permissions(Set.of(readPermission, updatePermission, deletePermission, writePermission))
					.build();

			RoleEntity sellerRoleEntity = RoleEntity.builder()
					.name(RoleEnum.ROLE_OWNER)
					.description("Propietario")
					.permissions(Set.of(readPermission, writePermission, deletePermission, updatePermission))
					.build();

			RoleEntity adminRoleEntity = RoleEntity.builder()
					.name(RoleEnum.ROLE_ADMIN)
					.description("Administrador")
					.permissions(Set.of(readPermission, writePermission, deletePermission, updatePermission))
					.build();

			// Create users
			UserEntity client = UserEntity.builder()
					.firstName("John")
					.lastName("Doe")
					.username("pepelucho")
					.email("jhon.doe@asd.com")
					.password("$2a$10$B2c3eYB/VFal9VptzHDVF.9jwf847aQbXOyJHT4ZfiFa3nwqJwg2K")
					.role(clientRoleEntity)
					.identificationNumber(1234L)
					.typeId(Short.parseShort("2"))
					.phoneNumber(12345)
					.city(laPaz)
					.isEnabled(true)
					.isLocked(false)
					.accountNonExpired(true)
					.credentialsNonExpired(true)
					.build();

			UserEntity seller = UserEntity.builder()
					.firstName("Admin")
					.lastName("Admin")
					.username("pepelucho")
					.email("admin.admin")
					.password("$2a$10$B2c3eYB/VFal9VptzHDVF.9jwf847aQbXOyJHT4ZfiFa3nwqJwg2K")
					.role(sellerRoleEntity)
					.identificationNumber(1234L)
					.typeId(Short.parseShort("2"))
					.phoneNumber(12345)
					.city(cochabamba)
					.isEnabled(true)
					.isLocked(false)
					.accountNonExpired(true)
					.credentialsNonExpired(true)
					.build();

			UserEntity admin = UserEntity.builder()
					.firstName("Moderator")
					.lastName("Moderator")
					.username("pepelucho")
					.email("moderator.moderator")
					.password("$2a$10$B2c3eYB/VFal9VptzHDVF.9jwf847aQbXOyJHT4ZfiFa3nwqJwg2K")
					.role(adminRoleEntity)
					.identificationNumber(1234L)
					.typeId(Short.parseShort("2"))
					.phoneNumber(12345)
					.city(santaCruz)
					.isEnabled(true)
					.isLocked(false)
					.accountNonExpired(true)
					.credentialsNonExpired(true)
					.build();

			IUserRepository.saveAll(Set.of(client, seller, admin));
		};
	}
};
