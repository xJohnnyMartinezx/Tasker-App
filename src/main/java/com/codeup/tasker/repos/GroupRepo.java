package com.codeup.tasker.repos;

import com.codeup.tasker.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepo extends JpaRepository<Group, Long > {

}
