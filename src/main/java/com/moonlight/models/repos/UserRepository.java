package com.moonlight.models.repos;

import com.moonlight.models.sql.Users;

import java.util.Optional;

public enum UserRepository {
	INSTANCE;
	public SqlFinder<Users, Integer> usersFinder = new SqlFinder<>(Users.class);

	public Optional<Users> findUserById(Integer id) {
		return usersFinder.getExpressionList()
				.idEq(id)
				.findOneOrEmpty();
	}

}
