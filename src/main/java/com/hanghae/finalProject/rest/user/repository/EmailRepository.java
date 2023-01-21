package com.hanghae.finalProject.rest.user.repository;

import com.hanghae.finalProject.rest.user.model.Email;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository
@Component
public interface EmailRepository extends CrudRepository<Email, String> {
}
