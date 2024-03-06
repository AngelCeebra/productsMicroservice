package com.microservice.app.users.models.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.microservice.app.commons.users.models.entity.User;

@RepositoryRestResource(path="users")
public interface UserRepository extends PagingAndSortingRepository<User, Long>, CrudRepository<User, Long>{

	@RestResource(path="find-username")
	public User findByUsername(@Param("username") String username);
	
	@Query("select u from User u where u.username=?1")
	public User getByUsername(String username);
	
}
