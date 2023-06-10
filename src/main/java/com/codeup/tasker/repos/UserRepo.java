package com.codeup.tasker.repos;

import com.codeup.tasker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

}
