package com.codeup.tasker.repos;

import com.codeup.tasker.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepo extends JpaRepository<Organization, Long > {

}
