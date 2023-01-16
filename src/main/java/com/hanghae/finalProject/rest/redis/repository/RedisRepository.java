package com.hanghae.finalProject.rest.redis.repository;

import com.hanghae.finalProject.rest.redis.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<Student, String> {

}
